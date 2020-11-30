/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.hashpassword;

@WebServlet(name = "registerUser", urlPatterns = {"/registerUser"})
public class registerUser extends HttpServlet {

    util.EncryptionDecryptionAES encdyc = new util.EncryptionDecryptionAES();

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
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println("Username re : " + username);
            System.out.println(password);

            query = "select username from userDB where username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, encdyc.encrypt(username));
            ResultSet rs = statement.executeQuery();
            System.out.println("is null " + rs.next());

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

            if (!exist) {
                //System.out.println("works");

                if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {

                    String hashpass = hashpassword.hashString(password);
                    PreparedStatement statement1;
                    query = "insert into userDB values(?, ?, ?, ?, ?)";
                    //System.out.println("before0");

                    statement1 = connection.prepareStatement(query);
                    //System.out.println("before1");

                    statement1.setString(1, encdyc.encrypt(username));
                    statement1.setString(2, hashpass);
                    statement1.setString(3, encdyc.encrypt("normal"));
                    statement1.setString(4, null);
                    statement1.setString(5, null);
                    //System.out.println("before2");

                    statement1.executeUpdate();
                    //System.out.println("after");

                    response.sendRedirect("index.jsp");
                }
            } else {
                request.setAttribute("msg", "User exist");
                RequestDispatcher rd = request.getRequestDispatcher("registration.jsp");
                rd.forward(request, response);
                //response.sendRedirect("registration.jsp");
            }

        } catch (Exception e) {
            System.err.println("the Problem 1 " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println("the Problem " + e.getMessage());
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
