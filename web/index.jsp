<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
       
        <!--<script src="https://www.google.com/recaptcha/api.js" async defer></script>-->
        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <meta name="google-signin-client_id" content="673492077692-d0it5938e4bm0j2paccl2qekqq21bdbu.apps.googleusercontent.com">

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
      
    </head>
    <body>
    <nav class="navbar navbar-light bg-light justify-content-between">
            <a class="navbar-brand">Application</a>
            
          </nav>
        <br>
        <div class="container">
            <div class="row">
              <div class="col">
              </div>
              <div class="col">
                <div class="card" style="width: 18rem;">
                    
                    <div class="card-body">
                      <h5 class="card-title ">Login</h5>
                      <form method="POST" action="login" >
                      <input type="text" class="form-control" name="username" placeholder="Username" aria-label="Username"value="chris" >
                      <br>
                      <input type="password" class="form-control" name="password" placeholder="Username" aria-label="Username" value="1234">                
                      <br>
                        <button type="submit" class="btn btn-primary btn-lg btn-block">Login</button>
                      </form>
                      <br>
                      <form action="registration.jsp">
                          <button class="btn btn-primary btn-lg btn-block" name="Register">Register</button>
                      </form>
                      <br>
                      
                      <form id="theForm" method="POST" action="GoogleAuthServlet">
                          <input type="hidden" id="custId" name="custId"> 
                            <div class="g-signin2" data-onsuccess="onSignIn" type="submit"></div>
                      </form>

                      <!--<div class="g-recaptcha" data-sitekey="6LcuIekZAAAAABOBXQSNqgnKA9EiUJgTvilxaYZU"></div>-->

                     <!-- <br>
                      <a href="success.html">sas</a>
                      <button onclick="signOut()">sign out</button>-->

                    </div>
                     
                  </div>
              </div>
                
              <div class="col">
              </div>
            </div>
          </div>        
       
        <script>
              function onSignIn(googleUser){
                  var id_token = googleUser.getAuthResponse().id_token;
                  console.log(id_token);    
                  var hidden = JSON.stringify(id_token);
//                  var profile = googleUser.getBasicProfile();
//                  console.log(JSON.stringify(profile));
//                  var email = document.querySelector("#email");
//                  email.innerText= googleUser.getBasicProfile().getEmail();
//                  var name = document.querySelector("#name");
//                  name.innerText= googleUser.getBasicProfile().getGivenName();
                    document.getElementById("custId").value = id_token;
                    document.getElementById('theForm').submit();
                    form.submit();
                  }
              
              function signOut(){
                  gapi.auth2.getAuthInstance().signOut().then(function () {
                      console.log("SIGN OUT");
                  });
              }
        </script>

    </body>
</html>
