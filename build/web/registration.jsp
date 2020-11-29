<%-- 
    Document   : registration
    Created on : Oct 1, 2020, 2:04:25 PM
    Author     : Christodoulos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
    </head>
    <body>
        <h1>Register</h1>
        <form method="POST" id="form" action="registerUser" >
            <input type="text" id="username" name="username" value=""/>
            <input type="password" name="password"/>
            <button onclick="sub()">Send</button>         
        </form>

        <%String msg = (String)request.getAttribute("msg");%>
        <%if (msg == null){%>
         <p></p>
        <%}else{%>
         <p><%=msg%></p>
        <%}%>
        <script type="text/javascript">
             
           function sub(){
                var RexStr = /\<|\>|\(|\)|\;|\/|\"|\'|\&/g  
                var val = document.getElementById("username").value.replace(RexStr, '');
                document.getElementById("username").value = val;
                console.log(document.getElementById("username").value);
                document.getElementById("form").submit()
            }
           
        </script>
    </body>
</html>