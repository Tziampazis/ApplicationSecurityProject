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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
/**
 *
 * @author Christodoulos
 */
@WebServlet(name = "GoogleAuthServlet", urlPatterns = {"/GoogleAuthServlet"})
public class GoogleAuthServlet extends HttpServlet {
    
    Connection connection = null;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String JWT = request.getParameter("custId");
            //System.out.println(JWT);
            
            java.util.Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
            String[] parts = JWT.split("\\."); // split out the "parts" (header, payload and signature)

            String headerJson = new String(decoder.decode(parts[0]));
            String payloadJson = new String(decoder.decode(parts[1]));
            String signatureJson = new String(decoder.decode(parts[2]));
            
            System.out.println(headerJson);
            System.out.println(payloadJson);
            //System.out.println(signatureJson);
            JSONObject obj =new JSONObject(payloadJson);
            System.out.println(obj.get("email"));
            System.out.println(obj.get("given_name"));
            System.out.println(obj.get("family_name"));
            System.out.println(obj.get("iat"));
            System.out.println(obj.get("exp"));
            
            String email = (String) obj.get("email");
            String name  = (String) obj.get("given_name");
            String surname = (String) obj.get("family_name");

            
            
        try (PrintWriter out = response.getWriter()){
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/security;user=security;password=security");
            String query;

            query = "select email from userDB where email = ?";
            PreparedStatement statement;
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            
            boolean exist = false;
            String result = "";
            if (rs.next()) {
                result = rs.getString("email");
                System.out.println(result);
                if (result.equals(email)) {
                    exist = true;
                }
            }
            if(!exist){
                //TODO if not exist then redirect to registration for Google auth
                ArrayList<Customer> requestedDetails = new ArrayList<Customer>(); 
                Customer cust = new Customer(name,surname,email);
                requestedDetails.add(cust); 

                // Setting the attribute of the request object 
                // which will be later fetched by a JSP page 
                request.setAttribute("data", requestedDetails);
                
                // Creating a RequestDispatcher object to dispatch 
                // the request the request to another resource 
                RequestDispatcher rd = request.getRequestDispatcher("registrationGoogle.jsp");
                rd.forward(request, response);
              
                
                //response.sendRedirect("failure.html");
            }else{
                HttpSession session = request.getSession();
                session.setAttribute("user", name);
                //setting session to expiry in 30 mins
                session.setMaxInactiveInterval(30);
                javax.servlet.http.Cookie userName = new javax.servlet.http.Cookie("user", name);
                userName.setMaxAge(30 * 60);
                response.addCookie(userName);
                response.sendRedirect("userPage");
            }
        } catch (SQLException ex) {
            Logger.getLogger(GoogleAuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GoogleAuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
}
