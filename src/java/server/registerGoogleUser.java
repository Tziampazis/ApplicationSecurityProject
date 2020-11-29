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
import javax.servlet.http.HttpSession;
import util.hashpassword;

@WebServlet(name = "registerGoogleUser", urlPatterns = {"/registerGoogleUser"})
public class registerGoogleUser extends HttpServlet {

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
           
            
            
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String email = request.getParameter("email");

            System.out.println(name);
            System.out.println(surname);
            System.out.println(email);

            
            query = "insert into userDB values(?, ?, ?, ?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, ""); 
            statement.setString(3, "normal");
            statement.setString(4, email);
            statement.setString(5, surname);
            statement.executeUpdate(); 
            HttpSession session = request.getSession();
            session.setAttribute("user", name);
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30);
            javax.servlet.http.Cookie userName = new javax.servlet.http.Cookie("user", name);
            userName.setMaxAge(30 * 60);
            response.addCookie(userName);
//            response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
            response.sendRedirect("userPage.jsp");

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
}
