/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import sun.misc.BASE64Encoder;
import util.hashpassword;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpSession;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author canku
 */
@WebServlet(name = "DownloadFile", urlPatterns = {"/DownloadFile"})
public class DownloadFile extends HttpServlet {

    private static final int BUFFER_SIZE = 4096;
    Connection connection = null;

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (OutputStream outStream = response.getOutputStream()) {
            String user = null;
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
            } else {
                user = (String) session.getAttribute("user");
            }
            String userName = null;
            String sessionID = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) {
                        userName = cookie.getValue();
                    }
                    if (cookie.getName().equals("JSESSIONID")) {
                        sessionID = cookie.getValue();
                    }
                }
            }
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");

            String fileIdStr = request.getParameter("fileId");
            int fileId = Integer.parseInt(fileIdStr);
            util.File dbFile = GetFile(fileId);

            String fileUsername = dbFile.getUser();
            String filePermission = dbFile.getPermission();

            if (!fileUsername.equals(userName) && !filePermission.equals("public")) {
                response.sendRedirect("userPage");
            }
            byte[] decodedFile = Decode(dbFile.getUploadedFile());
            ByteArrayInputStream bis = new ByteArrayInputStream(decodedFile);
            int fileLength = bis.available();
            System.out.println("fileLength = " + fileLength);

            ServletContext context = getServletContext();

            // sets MIME type for the file download
            Path path = Paths.get(dbFile.getUploadedFile());
            Path fileName = path.getFileName();
            String mimeType = context.getMimeType(fileName.toString());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            // set content properties and header attributes for the response
            response.setContentType(mimeType);
            response.setContentLength(fileLength);
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileName);
            response.setHeader(headerKey, headerValue);

            // writes the file to the client
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = bis.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            bis.close();
            outStream.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    private util.File GetFile(int id) throws SQLException {
        String query = "select id, usr, permission, status, uploadedFile, uploadDate from SECURITY.FILES where id=?";
        PreparedStatement statement = connection.prepareStatement(query);

        String idStr = String.valueOf(id);
        statement.setString(1, idStr);
        ResultSet rsFile = statement.executeQuery();

        util.File result = null;
        while (rsFile.next()) {
            String userFile = rsFile.getString("usr");
            String permissionFile = rsFile.getString("permission");
            String statusFile = rsFile.getString("status");
            String uploadedFile = rsFile.getString("uploadedFile");
            Date uploadDate = rsFile.getDate("uploadDate");

            String idStrRes = rsFile.getString("id");
            int idRes = Integer.parseInt(idStrRes);

            result = new util.File(idRes, userFile, permissionFile, statusFile, uploadedFile, uploadDate);
            break;
        }

        return result;
    }

    public byte[] Decode(String filePath)
            throws Exception {
        File file = new File(filePath);
        BASE64Decoder decoder = new BASE64Decoder();
        InputStream inputStream = new FileInputStream(file.getAbsolutePath());
        byte[] fileDecoded = decoder.decodeBuffer(inputStream);
        inputStream.close();
        return fileDecoded;
    }

}
