/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.hashpassword;

/**
 *
 * @author canku
 */
@WebServlet(name = "RemoveFile", urlPatterns = {"/RemoveFile"})
public class RemoveFile extends HttpServlet {

    Connection connection = null;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
                    String user = null;
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
            } else {
                user = (String) session.getAttribute("user");
            }
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");
            String fileIdStr = request.getParameter("fileId");
            int idRes = Integer.parseInt(fileIdStr);
            RemoveFile(idRes);
            response.sendRedirect("userPage");
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

    private void RemoveFile(int fileId) throws SQLException, IOException {
        util.File fileToRemove = GetFile(fileId);
        Files.deleteIfExists(Paths.get(fileToRemove.getUploadedFile()));
        DeleteInDb(fileId);

    }

    private void DeleteInDb(int fileId) throws SQLException {
        String query = "Delete From SECURITY.FILES where id=?";
        PreparedStatement statement = connection.prepareStatement(query);

        String idStr = String.valueOf(fileId);
        statement.setString(1, idStr);
        statement.executeUpdate();
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

}
