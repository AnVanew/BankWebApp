<%--
  Created by IntelliJ IDEA.
  User: naic infa
  Date: 11/24/2020
  Time: 3:46 PM
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
<%AccManager accManager = new AccManager();
    Class.forName("org.postgresql.Driver");
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    try {
        accManager.removeAccount(username, password);
        response.getWriter().println("Аккаунт удален");
    }
    catch (AccException e) {
        response.getWriter().println("Удаление невозможно");}
%>
</body>
</html>
