<%-- 
    Document   : newsfeed
    Created on : Oct 26, 2020, 2:56:24 PM
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
        <title>News Feed</title>

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <meta name="google-signin-client_id" content="673492077692-d0it5938e4bm0j2paccl2qekqq21bdbu.apps.googleusercontent.com">
    </head>
    <body>
        <nav class="navbar navbar-light bg-light justify-content-between">
            <a class="navbar-brand">Application</a>
            <form class="form-inline" action="userPage" method="get">
                <button class="btn btn-outline-success my-2 my-sm-0" value="Main" >User Page</button>
            </form>
            <form class="form-inline" action="LogoutServlet" method="post">
                <button class="btn btn-outline-success my-2 my-sm-0" value="Logout" onclick="signOut()" type="submit">Logout</button>
                <input class="g-signin2" type="hidden" >
            </form>
        </nav>
        <div class="container">
            <h4>Uploaded files by users</h4>
            <br>
            <div class="row border-bottom">
                <div class="col-3"><h5>File</h5></div>
                <div class="col-3"><h5>Owner</h5></div>
                <div class="col-3"><h5>Upload date</h5></div>
                <div class="col-3"><h5>Operation</h5></div>
            </div>
            <c:forEach items="${fileList}" var="file">
                <div class="row">
                    <div class="col-3">${file.name}</div>
                    <div class="col-3">${file.user}</div>
                    <div class="col-3">${file.uploadDate}</div>
                    <div class="col-3"> 
                        <a href="DownloadFile?fileId=${file.id}" class="btn btn-primary" id="btnDownload"
                           target="_blank">Download</a>
                        <a href="#" class="btn btn-success" id="btnDisplay" data-image="${file.uploadedFile}" >Display</a>

                    </div>
                </div>
                <br>
            </c:forEach>   
        </div>
        <!-- Modal -->
        <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <img src="" class="img-fluid" id="imageFrame" alt="Responsive image">
                </div>
            </div>
        </div>
        <script>
            function signOut() {
                gapi.auth2.getAuthInstance().signOut().then(function () {
                    console.log("SIGN OUT");
                });
            }
            $("#btnDisplay").click(function () {
                let atributeVal = $("#btnDisplay").attr("data-image");
                atributeVal = "data:image/png;base64, " + atributeVal;
                console.log(atributeVal);
                $("#imageFrame").attr("src", atributeVal);
                $('#exampleModal').modal()
            });

        </script>
    </body>
</html>
