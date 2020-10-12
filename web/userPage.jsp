<%-- 
    Document   : userPage
    Created on : Oct 4, 2020, 1:11:46 PM
    Author     : Christodoulos
--%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Date"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
            <br>
          <%
         //allow access only if session exists
         String user = null;
         if(session.getAttribute("user") == null){
                 response.sendRedirect("index.jsp");
         }else user = (String) session.getAttribute("user");
         String userName = null;
         String sessionID = null;
         Cookie[] cookies = request.getCookies(); 
        if(cookies !=null){
         for(Cookie cookie : cookies){
                 if(cookie.getName().equals("user")) userName = cookie.getValue();
                 if(cookie.getName().equals("JSESSIONID")) sessionID = cookie.getValue();
         }
         }
         %>
         <h3>Hi <%=userName %>, Login successful. Your Session ID=<%=sessionID %></h3>   
        <br>
        <button name="show" value="ShowFiles">Show Files</button>
        <br>
                <br>

        <button name="show" value="ShowFiles">Download Files</button>
                <br>
                        <br>

        <form method="POST" action="fileUpload" enctype="multipart/form-data" >
            File:
            <input type="file" name="file"  /> <br/>
            </br>
            <input type="submit" value="upload" name="upload" />
        </form>
        
        <form action="LogoutServlet" method="post">
            <input type="submit" value="Logout" >
        </form>
    </body>
</html>
