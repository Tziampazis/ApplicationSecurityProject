/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import util.File;
import util.hashpassword;

/**
 *
 * @author canku
 */
@WebServlet(name = "DownloadFile", urlPatterns = {"/DownloadFile"})
public class DownloadFile extends HttpServlet {

    private static final int BUFFER_SIZE = 4096;

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
        request.getSession(false).invalidate();
        Connection connection = null;
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");
            String query = "select username,password from userDB where username = ?";
            List<InputStream> inputStreams = new ArrayList<InputStream>();
            String queryFile = "select * from SECURITY.FILES";
            PreparedStatement statementFile = connection.prepareStatement(queryFile);
            ResultSet rsFile = statementFile.executeQuery();

            while (rsFile.next()) {
                String userFile = rsFile.getString("usr");
                String permissionFile = rsFile.getString("permission");
                String statusFile = rsFile.getString("status");
                Blob blobFile = (Blob) rsFile.getBlob("uploadedFile");
                InputStream inputStream= blobFile.getBinaryStream();

                int fileLength = inputStream.available();

                System.out.println("fileLength = " + fileLength);

                ServletContext context = getServletContext();

                // sets MIME type for the file download
                String fileName = "aa.jpg";
                String mimeType = context.getMimeType(fileName);
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
                OutputStream outStream = response.getOutputStream();

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outStream.close();
            }

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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
