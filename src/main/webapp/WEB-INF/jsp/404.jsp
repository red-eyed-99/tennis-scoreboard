<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 Page Not Found</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/icons/page.ico">
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<h1>404 Page Not Found</h1>
<img class="error-tennis-player" src="${pageContext.request.contextPath}/images/error-tennis-players/404.jpg" alt=""/>
</body>
</html>
