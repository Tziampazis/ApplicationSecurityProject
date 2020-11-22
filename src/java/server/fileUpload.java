/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import sun.misc.BASE64Encoder;

@WebServlet(name = "fileUpload", urlPatterns = {"/fileUpload"})
@MultipartConfig
public class fileUpload extends HttpServlet {

    final String SaveLocation = "C:\\temp";
    static Cipher cipher;
    Connection connection = null;

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionTracking sessionTracking = new SessionTracking("");

//      if (SessionTracking.checkTimeValid(request.getSession().getCreationTime(),request.getSession().getLastAccessedTime(),request.getSession().getMaxInactiveInterval())){
        System.out.println("Session Valid");
        System.out.println(request.getSession().getId());

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            PreparedStatement statement;

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");

            String state_of_file = request.getParameter("public_private");
            Part file = request.getPart("file");
            String name = file.getSubmittedFileName();

            String filePath = SaveLocation + "\\" + name;
            Encrypt(file, filePath);
            InsertToDB(state_of_file, filePath);
            response.sendRedirect("userPage.jsp");

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

    public void InsertToDB(String state_of_file, String filePath)
            throws SQLException {
        String query = "insert into files (uploadedfile, usr, status, permission) values(?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement = connection.prepareStatement(query);
        statement.setString(1, filePath);
        statement.setString(2, "aaa");
        statement.setString(3, "active");
        statement.setString(4, state_of_file);
        statement.executeUpdate();
    }

    public void Encrypt(Part file, String filePath)
            throws Exception {
        String name = file.getSubmittedFileName();
        BASE64Encoder encoder = new BASE64Encoder();
        File encryptedFile = new File(filePath);
        FileOutputStream fos = new FileOutputStream(encryptedFile.getAbsoluteFile());
        InputStream inputStream = file.getInputStream();
        encoder.encode(inputStream, fos);
    }

//    private void SaveFile(String filePath, InputStream inputStream) {
//        try {
//            OutputStream os = new FileOutputStream(filePath);
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            //read from is to buffer
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            inputStream.close();
//            //flush OutputStream to write any buffered data to file
//            os.flush();
//            os.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
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
