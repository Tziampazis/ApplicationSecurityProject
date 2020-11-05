<%-- 
    Document   : userPage
    Created on : Oct 4, 2020, 1:11:46 PM
    Author     : Christodoulos
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="util.File"%>
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.util.Date"%>   

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </head>
    <style>
    </style>
    <body>
        <%
            //allow access only if session exists
            String user = null;
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

        %>
        <nav class="navbar navbar-light bg-light justify-content-between">
            <a class="navbar-brand">Application</a>
            <form class="form-inline" action="newsfeed" method="get">
                <button class="btn btn-outline-success my-2 my-sm-0" value="Main" >MainPage</button>
            </form>
            <form class="form-inline" action="LogoutServlet" method="post">
                <button class="btn btn-outline-success my-2 my-sm-0" value="Logout" type="submit">Logout</button>
            </form>
        </nav>

        <div class="container">
            <div class="row">
                Hi <%=userName%>, Your Session ID=<%=sessionID%>           
            </div>
            <br>
            <div class="row">
                <div class="col-4">
                    <table>
                        <thead>
                            <tr>
                                <th>User</th>
                                <th>Permission</th>
                                <th>Status</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach items="${fileList}" var="file">
                                <tr>
                                    <td>${file.user}</td>
                                    <td>${file.permission}</td>
                                    <td>${file.status}</td>
                                </tr>
                            </c:forEach>   
                        </tbody>
                    </table>
                    <br>
                    <form method="GET" action="DownloadFile" enctype="multipart/form-data" >
                        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Download Files</button>
                    </form>
                </div>
                <div class="col-8">
                    <form method="POST" action="fileUpload" enctype="multipart/form-data" >
                        File:
                        <br>
                        <input type="file" name="file"/> 
                        <br>
                        <button class="btn btn-outline-success my-2 my-sm-0" type="submit" value="upload" name="upload">Upload File</button>
                        <br>
                        <label for="cars">Document Status</label>
                        <br>
                        <div class="custom-control custom-radio">
                            <input type="radio" id="customRadio1" name="public_private" value="private" class="custom-control-input">
                            <label class="custom-control-label"  name="public_private" value="private" for="customRadio1">Private</label>
                        </div>
                        <div class="custom-control custom-radio">
                            <input type="radio" id="customRadio2" name="public_private" value="public" class="custom-control-input">
                            <label class="custom-control-label" name="public_private" value="public" for="customRadio2">Public</label>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </body>
</html>
