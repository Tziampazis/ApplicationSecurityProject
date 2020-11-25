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
import javax.servlet.http.Part;
import util.File;

/**
 *
 * @author Christodoulos
 */
@WebServlet(name = "userPage", urlPatterns = {"/userPage"})
public class userServlet extends HttpServlet {

    Connection connection = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            //allow access only if session exists
            String user = null;
            HttpSession session = request.getSession();
            if (session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
            } else {
                user = (String) session.getAttribute("user");
            }
            String userName = null;
            String sessionID = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("user")) {
                        userName = cookie.getValue();
                    }
                    if (cookie.getName().equals("JSESSIONID")) {
                        sessionID = cookie.getValue();
                    }
                }
            }

            Class.forName("org.apache.derby.jdbc.ClientDriver");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");

            String status = "active";
            List<File> files = GetFiles(user, status);

            RequestDispatcher dispatcher = request.getRequestDispatcher("userPage.jsp");
            request.setAttribute("userName", userName);
            request.setAttribute("sessionID", sessionID);
            request.setAttribute("fileList", files);
            dispatcher.include(request, response);

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

    private List<File> GetFiles(String username, String status) throws SQLException {
        List<File> result = new ArrayList<File>();
        String query = "select id, usr, permission, status, uploadedFile, uploaddate from SECURITY.FILES where usr=? and status=?";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, username);
        statement.setString(2, status);

        ResultSet rsFile = statement.executeQuery();

        while (rsFile.next()) {
            String userFile = rsFile.getString("usr");
            String permissionFile = rsFile.getString("permission");
            String statusFile = rsFile.getString("status");
            String idStr = rsFile.getString("id");
            String filePath = rsFile.getString("uploadedFile");
            Date uploadDate = rsFile.getDate("uploadDate");

            int id = Integer.parseInt(idStr);

            File fileItem = new File(id, userFile, permissionFile, statusFile, filePath, uploadDate);
            result.add(fileItem);
        }

        return result;
    }
}
