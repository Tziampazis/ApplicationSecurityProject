<%-- 
    Document   : upload
    Created on : Oct 1, 2020, 6:48:24 PM
    Author     : Christodoulos
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>upload</title>
    </head>
    
    <body>
        <form method="POST" action="fileUpload" enctype="multipart/form-data" >
            File:
            <input type="file" name="file"  /> <br/>
            </br>
            <input type="submit" value="file" name="upload" />
        </form>
    </body>
</html>
