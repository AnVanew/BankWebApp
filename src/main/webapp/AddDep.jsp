<%--
  Created by IntelliJ IDEA.
  User: naic infa
  Date: 11/24/2020
  Time: 3:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@page import="DepManager.DepManager" %>
<%@ page import="DataOfBank.Client" %>
<%@ page import="java.util.Date" %>
<%@ page import="ManageExeptions.DepositeException" %>
<%DepManager depManager = new DepManager();
    Class.forName("org.postgresql.Driver");
    String token = null;
    Client client = new Client("Ivan", "Andreev", "1234566662");
    Cookie[] cookies = request.getCookies();
    for (Cookie cok : cookies){
        if (cok.getName().equals("token")) token = cok.getValue();
    }
    try {
        depManager.addDeposit(client, 100000, 0.08, 0.05, 365, new Date(), true, token);
        response.getWriter().println("Депозит создан");
    } catch (DepositeException e) {
        response.getWriter().println("Депозит не создан");
    }
%>
</body>
</html>
