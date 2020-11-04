/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import util.hashpassword;

@WebServlet(name = "fileUpload", urlPatterns = {"/fileUpload"})
@MultipartConfig
public class fileUpload extends HttpServlet {

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

      
        Connection connection = null;
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {        
            String query;
            PreparedStatement statement;
            
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");
            
            /*
            // SQL Commands to create the database can be found in file database.sql		 		  
            */            
            String name = "";
            String type  = "";
            long size =0;
            InputStream streamfile = null;       
            String state_of_file = request.getParameter("public_private");
            System.out.println("State " +state_of_file);
            
            Part file = request.getPart("file");
            if (file != null){
                 name = file.getName();
                 type = file.getContentType();
                 size = file.getSize();
                 streamfile = file.getInputStream();
            }
            
            System.out.println("file : " + name +" "+type+" "+size);
            
            if (file != null & file.getSize() != 0) {
                          query = "insert into files (uploadedfile, usr, status, permission) values(?, ?, ?, ?)";
                          statement = connection.prepareStatement(query);
                          statement.setBlob(1, streamfile);
                          statement.setString(2, "aaa");
                          statement.setString(3, "normal");
                          statement.setString(4,state_of_file);
                          statement.executeUpdate(); 
                          response.sendRedirect("userPage.jsp");
                  
            }else{
                  response.sendRedirect("userPage.jsp");
              }
            
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
//    }else{
//          System.out.println("Session Ended");
//          request.getSession(false).invalidate();
//          response.sendRedirect("index.jsp");
//      }
      
      
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
