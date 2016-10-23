<%-- 
    Document   : usersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.ej.instagrim.stores.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" type="text/css" href="/Instagrim/Styles.css" />
    </head>
    <body>
        <header>
        
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        
        <nav>
        </nav>
 
        <article>
            <h2>Your profile</h2>
            
        <%
        
        LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
        String username=lg.getUsername();

        if((boolean)request.getAttribute("haspic"))
        {
        %>
        
        <a href="/Instagrim/Profile/<%= username%>/picture" ><img src ="/Instagrim/Profile/<%= username%>/picture" height="256" width="256" ></a>
        <br/>
        
        <%
            }
            else
            {
        %>
        <h2>No profile picture!</h2>
        <%
            }
        %>
        
        <br/>
        <form method="POST" enctype="multipart/form-data" action="Profile">
                <input type="file" name="upfile"><br/>

                <br/>
                <input type="submit" value="Press"> to upload your profile picture!
            </form>
        <li>First name:<%= request.getAttribute("first")%></li>
        <li>Last name:<%= request.getAttribute("last") %></li>
        <li>E-mail:<%= request.getAttribute("email") %></li>
        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
