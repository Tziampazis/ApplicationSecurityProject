<%-- 
    Document   : registration
    Created on : Oct 1, 2020, 2:04:25 PM
    Author     : Christodoulos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    <body>
        <form method="POST" action="registerUser" >
            <input type="text" name="username"/>
            <input type="password" name="password"/>
            <input type="submit" value="Send"/>            
        </form>  
    </body>
</html>
