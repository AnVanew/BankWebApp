<%--
  Created by IntelliJ IDEA.
  User: naic infa
  Date: 11/24/2020
  Time: 12:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%@page import="AccManager.AccManager" %>
<%@ page import="java.util.List" %>

<%AccManager accManager = new AccManager();

        List<String> accs= accManager.getAllAccounts();
        response.getWriter().println("Имеющиеся аккаунты:");
        for (String acc:accs) response.getWriter().println(acc);

%>

</body>
</html>
