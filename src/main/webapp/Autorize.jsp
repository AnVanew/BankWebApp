<%--
  Created by IntelliJ IDEA.
  User: naic infa
  Date: 11/24/2020
  Time: 3:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@page import="AccManager.AccManager" %>
<%@ page import="ManageExeptions.EqualAccException" %>
<%@ page import="ManageExeptions.AccException" %>
<%@ page import="java.util.Date" %>
<%AccManager accManager = new AccManager();
    Class.forName("org.postgresql.Driver");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    try {
        String token = accManager.authorize (username, password, new Date());
        response.getWriter().println("Токен возвращен ");
        Cookie tokenCookie = new Cookie("token", token);
        response.addCookie(tokenCookie );
    }
    catch (AccException e) {
        response.getWriter().println("Поля username/passwort пустые");}
%>
</body>
</html>
