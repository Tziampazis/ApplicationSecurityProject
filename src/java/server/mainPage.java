/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sun.misc.BASE64Decoder;
import util.File;

/**
 *
 * @author Christodoulos
 */
@WebServlet(name = "newsfeed", urlPatterns = {"/newsfeed"})
public class mainPage extends HttpServlet {

    util.EncryptionDecryptionAES encdyc = new util.EncryptionDecryptionAES();

    Connection connection = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");

            String status = "active";
            String permission = "public";
            List<File> files = GetFiles(permission, status);

            RequestDispatcher dispatcher = request.getRequestDispatcher("newsfeed.jsp");
            request.setAttribute("fileList", files);
            dispatcher.include(request, response);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    private List<File> GetFiles(String permission, String status) throws SQLException, Exception {
        List<File> result = new ArrayList<File>();
        String query = "select id, usr, permission, status, uploadedFile, uploadDate from SECURITY.FILES where permission=? and status=?";
        PreparedStatement statement = connection.prepareStatement(query);

        permission = encdyc.encrypt(permission);
        status = encdyc.encrypt(status);

        statement.setString(1, permission);
        statement.setString(2, status);

        ResultSet rsFile = statement.executeQuery();

        while (rsFile.next()) {
            String userFile = rsFile.getString("usr");
            String permissionFile = rsFile.getString("permission");
            String statusFile = rsFile.getString("status");
            String idStr = rsFile.getString("id");
            String filePath = rsFile.getString("uploadedFile");
            int id = Integer.parseInt(idStr);
            Date uploadDate = rsFile.getDate("uploadDate");

            userFile = encdyc.decrypt(userFile);
            permissionFile = encdyc.decrypt(permissionFile);
            statusFile = encdyc.decrypt(statusFile);
            filePath = encdyc.decrypt(filePath);

            byte[] fileContent = Decode(filePath);
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedText = encoder.encodeToString(fileContent);

            File fileItem = new File(id, userFile, permissionFile, statusFile, filePath, uploadDate, encryptedText);
            result.add(fileItem);
        }

        return result;
    }

    public byte[] Decode(String filePath)
            throws Exception {
        java.io.File file = new java.io.File(filePath);
        BASE64Decoder decoder = new BASE64Decoder();
        InputStream inputStream = new FileInputStream(file.getAbsolutePath());
        byte[] fileDecoded = decoder.decodeBuffer(inputStream);
        inputStream.close();

        return fileDecoded;
    }

}
