<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Application</h1>
        <form method="POST" action="myservlet">
            <input type="submit" value="Create Database"/>            
        </form>
                <br>
        <br>

        <form method="POST" action="login" >
            <input type="text" name="username" value="chris"/>
            <input type="password" name="password" value="1234"/>
            <input type="submit" value="Send"/>            
        </form>        
                <br>
        <br>

        
        <form action="registration.jsp">
            <button name="register">register</button>
        </form>
        
    </body>
</html>
