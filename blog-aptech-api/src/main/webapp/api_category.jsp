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
    <title>API CATEGORY</title>
</head>
<body>
<div style="display: flex;align-items: center;justify-content: space-around;">
    <a href="${pageContext.request.contextPath}/api/category/getAllCategorys">Get All Category</a>
    <a href="${pageContext.request.contextPath}/api/category/getCategoryById?id=">Get Category By ID</a>
</div>
</body>
</html>

