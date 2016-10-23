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
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </nav>
 
        <article>
            <h1>Your Pics</h1>
        <%
        String UUID = (String) request.getAttribute("UUID");
        LoggedIn lg = (LoggedIn)session.getAttribute("LoggedIn");
        %>
        <img src="/Instagrim/Image/<%=UUID%>"><br/>
        <li><%=UUID%></li>
        <%
        %>
        <h1>Comments:</h1>
        <%
        java.util.LinkedList<Comment> lsComment = (java.util.LinkedList<Comment>) request.getAttribute("Comments");
        if (lsComment == null) {
        %>
        <p>No comments found</p>
        <%
        }
        else {
            Iterator<Comment> iterator;
            iterator = lsComment.iterator();
            
            while (iterator.hasNext()) {
                Comment c = (Comment) iterator.next();

        %>
        <a href="/Instagrim/Profile/<%=c.getUser()%>" ><img src="/Instagrim/Profile/<%=c.getUser()%>/picture"></a><p><%= c.getContent()%></p><br/>
        <li><% int[] time = c.getTime(); %><%= time[0]%>:<%= time[1]%>, <%= c.getDate()%></li><br/>
        <%
                if(lg != null){
                    String username=lg.getUsername();
                    if(username.equals(c.getUser())){
        %>
        <a href="/Instagrim/Comment/<%=UUID%>/Delete" >Delete Comment</a><h2>  </h2>
        <%          }
                }
        %>
        <a href="/Instagrim/Comment/<%=UUID%>" >Permalink</a><br/>
        <%
            }
        }
            
        if(lg != null){
        %>
        <form>
            <p>Comment: <input type="text" name="comment"></p>
        <%
        }
        %>
        </form>
        </article>
        <footer>
            <ul>
            </ul>
        </footer>
    </body>
</html>
