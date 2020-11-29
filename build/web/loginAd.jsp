<%-- 
    Document   : loginAd
    Created on : Oct 15, 2020, 2:47:38 PM
    Author     : Christodoulos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Login</title>
    </head>
    <body>
       <h1>Admin Login</h1>
        <form method="POST" action="myservlet">
            <input type="submit" value="Create Database"/>            
        </form>
                <br>
        <br>

        <form method="POST" action="login" >
            <input type="text" name="username"/>
            <input type="password" name="password"/>
            <input type="submit" value="Send"/>            
        </form>        
                <br>
        <br>

        
        <form action="registration.jsp">
            <button name="register">register</button>
        </form>
    </body>
</html>
