<%--
  Created by IntelliJ IDEA.
  User: nguyen viet hung
  Date: 10/20/2023
  Time: 12:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>API USER</title>
</head>
<body>
<div style="display: flex;align-items: center;justify-content: space-around;">
    <a href="${pageContext.request.contextPath}/api/user/getAllUsers">Get All Users</a>
    <a href="${pageContext.request.contextPath}/api/user/getUsersById?id=">Get Users By Id</a>
    <a href="${pageContext.request.contextPath}/api/user/getUsersByRole?role=">Get Users By Role</a>
</div>
</body>
</html>

