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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import util.File;
import util.hashpassword;

@WebServlet(name = "login", urlPatterns = {"/login"})
public class login extends HttpServlet {

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

            query = "select username,password from userDB where username = ?";

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username);
            System.out.println(password);

            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                statement = connection.prepareStatement(query);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
//                System.out.println("server.login.doPost()");
                boolean hasResult = false;
                if (rs.next()) {
                    // read the result set
                    System.out.println(rs.getString("username") + username);
                    System.out.println(rs.getString("password") + hashpassword.checkSHApassword(password, rs.getString("password")));

                    if (rs.getString("username").equals(username) & hashpassword.checkSHApassword(password, rs.getString("password"))) {
                        hasResult = true;
                        HttpSession session = request.getSession();
                        session.setAttribute("user", "Pankaj");
                        //setting session to expiry in 30 mins
                        session.setMaxInactiveInterval(30);
                        Cookie userName = new Cookie("user", username);
                        userName.setMaxAge(30 * 60);
                        response.addCookie(userName);

//                        response.sendRedirect("userPage.jsp");
//                        HttpSession session;
//                        session = request.getSession(true);
//                        System.out.println(session.getId());
//                        session.setAttribute("user",username );
//                        session.setMaxInactiveInterval(30);
//                        System.out.println( "session " +session.getMaxInactiveInterval() );
//                        Date date1 = new Date(session.getCreationTime());
//                        System.out.println(session.getCreationTime());
//                        
//                        Date date = new Date(session.getLastAccessedTime());
//                        System.out.println(date);
//
//                        String na = (String) session.getAttribute("user");
//                        System.out.println(na);
                    } else {
                        response.sendRedirect("index.jsp");
                    }
                    if (hasResult) {
                        List<File> result = new ArrayList<File>();
                        String queryFile = "select usr, permission, status from SECURITY.FILES";
                        PreparedStatement statementFile = connection.prepareStatement(queryFile);
                        ResultSet rsFile = statementFile.executeQuery();
//                System.out.println("server.login.doPost()");

                        while (rsFile.next()) {
                            String userFile = rsFile.getString("usr");
                            String permissionFile = rsFile.getString("permission");
                            String statusFile = rsFile.getString("status");

                            File fileItem = new File(userFile, permissionFile, statusFile, null);
                            result.add(fileItem);
                        }
                        RequestDispatcher dispatcher = request.getRequestDispatcher("userPage.jsp");
                        request.setAttribute("fileList", result);
                        dispatcher.forward(request, response);
                    }
                }
            } else {
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
