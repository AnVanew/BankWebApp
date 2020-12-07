<%--
  Created by IntelliJ IDEA.
  User: naic infa
  Date: 11/24/2020
  Time: 12:13 PM
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
    <%@ page import="java.sql.SQLException" %>
    <%AccManager accManager = new AccManager();
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    Class.forName("org.postgresql.Driver");
    try {
      accManager.addAccount(username, password);
      response.getWriter().println("Аккаунт зарегестрирован");
    } catch (EqualAccException e){
        response.getWriter().println("Такой аккаунт уже есть");}
      catch (AccException e) {
          response.getWriter().println("Поля username/passwort пустые");}

    %>
</body>
</html>
