<%--
  Created by IntelliJ IDEA.
  User: nguyen viet hung
  Date: 10/20/2023
  Time: 12:38 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>API POST</title>
</head>
<body>
<div style="display: flex;align-items: center;justify-content: space-around;">
    <a href="${pageContext.request.contextPath}/api/post/getAllPosts">Get All Post</a>
    <a href="${pageContext.request.contextPath}/api/post/getPostById?id=">Get Posts By ID</a>
    <a href="${pageContext.request.contextPath}/api/post/getPostsByAuthor?author=">Get Posts By Author ID</a>
    <a href="${pageContext.request.contextPath}/api/post/getPostsByCategory?category=">Get Posts By Category</a>
</div>
</body>
</html>
