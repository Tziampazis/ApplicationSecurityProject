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

@WebServlet(name = "myservlet", urlPatterns = {"/myservlet"})
public class myservlet extends HttpServlet {

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

            query = "create table userDB (\n" +
                    "        username     varchar (128) NOT NULL,\n" +
                    "        password     varchar (128) NOT NULL,\n" +
                    "primary key (id)\n" +
                    ")";

            statement = connection.prepareStatement(query);
            statement.executeUpdate();
            // With preparedStatement, SQL Injection and other problems when inserting values in the database can be avoided
                        
            query = "insert into userDB values(?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(2, "Silvia");
            statement.setString(3, "12345");        
            statement.executeUpdate();      
            
            query = "insert into userDB values(?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(2, "Jaime");
            statement.setString(3, "54321");            
            statement.executeUpdate(); 

            /* 
               END COMMENT 
            */            
            
            // Select information from images and show in the web
            query = "select * from userDB";
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();                                    

            while (rs.next()) {
                // read the result set
                out.println("<br>Id user = " + rs.getInt("id"));
                out.println("Username = " + rs.getString("username"));
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
