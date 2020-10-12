/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.hashpassword;

@WebServlet(name = "registerUser", urlPatterns = {"/registerUser"})
public class registerUser extends HttpServlet {

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


            String username = request.getParameter("usernameR");
            String password = request.getParameter("passwordR");
            System.out.println("Username re : " + username);
            System.out.println(password);
            
            
            query = "select username from userDB where username = ?";

            statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            System.out.println(rs.next());

            boolean exist = false;
            String result = "";
            if (rs.next()) {
                result = rs.getString("username");
                System.out.println(result);
                if (result.equals(username)) {
                    exist = true;
                }
            }
            
            
            System.out.println("Username exist : " + exist);

            if (!exist){
                if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                          String hashpass = hashpassword.hashString(password);
                          query = "insert into userDB values(?, ?, ?)";
                          statement = connection.prepareStatement(query);
                          statement.setString(1, username);
                          statement.setString(2, hashpass);    
                          statement.setString(3, "normal");
                          statement.executeUpdate(); 
                          response.sendRedirect("index.jsp");
                  }
            }else {
                  response.sendRedirect("failure.html");
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
