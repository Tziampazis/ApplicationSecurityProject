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
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;
import util.EncryptionDecryptionAES;
import util.hashpassword;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class login extends HttpServlet {
    EncryptionDecryptionAES encdyc = new EncryptionDecryptionAES();
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
        request.getSession(false).invalidate();
        Connection connection = null;
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {    
            String query;
            PreparedStatement statement;
            
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");

            String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
            System.out.println("THE CATPCH "+gRecaptchaResponse);

            //boolean verified = VerifyRecaptcha. check ( gRecaptchaResponse ) ;
            
            query = "select username,password from userDB where username = ?";

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username);
            System.out.println(password);
            System.out.println("enc : " +encdyc.encrypt(username));
            System.out.println("dec : " +encdyc.decrypt(encdyc.encrypt(username)));
            
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                statement = connection.prepareStatement(query);
                statement.setString(1, encdyc.encrypt(username));
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    // read the result set
                    System.out.println(rs.getString("username")+username);
                    System.out.println(rs.getString("password")+ hashpassword.checkSHApassword(password,rs.getString("password")));

                    if (rs.getString("username").equals(encdyc.encrypt(username)) & hashpassword.checkSHApassword(password,rs.getString("password"))) {
                            HttpSession session = request.getSession();
                            session.setAttribute("user", username);
                            //setting session to expiry in 30 mins
                            session.setMaxInactiveInterval(30);
                            Cookie userName = new Cookie("user", username);
                            userName.setMaxAge(30*60);
                            response.addCookie(userName);
                            //response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
                            response.sendRedirect("userPage");
                    } else {
                        response.sendRedirect("index.jsp");
                    }
                }else{
                    response.sendRedirect("index.jsp");
                }
            } else {
                response.sendRedirect("index.jsp");
            }
            
        } catch (Exception e) {
            System.err.println("Problem!");
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
