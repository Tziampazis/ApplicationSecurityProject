<%-- 
    Document   : registration
    Created on : Oct 1, 2020, 2:04:25 PM
    Author     : Christodoulos
--%>

<%@page import="server.Customer"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration with Google Account</title>
    </head>
    <body>
        <form method="POST" action="registerGoogleUser" >
            <h1>Hello you will be register with the following details</h1>
            <% ArrayList<Customer> details = (ArrayList<Customer>)request.getAttribute("data"); 
                Customer cust = details.get(0);
            %> 
            <tr> 
            <input type="hidden" name="name"  value="<%=cust.getName()%>">
            <input type="hidden" name="surname"  value="<%=cust.getSurname()%>">
            <input type="hidden" name="email"value="<%=cust.getEmail()%>">

                <td>Email : <%=cust.getEmail()%></td> <br>
                <td>Name <%=cust.getName()%></td> 
                <td><%=cust.getSurname()%></td> <br>
            </tr> 
            <input type="submit" value="Send"/>            
        </form>  
    </body>
</html>
