<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Match result</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<div id="match-result-block" class="main-block">
    <h1>Match finished</h1>
    <div id="winner-block">
        <img id="winner-cup-icon" src="${pageContext.request.contextPath}/images/winner-cup.png" alt="cup icon">
        <span id="winner-name">Winner - ${requestScope.winner}</span>
    </div>
    <table>
        <caption>Match score</caption>
        <tbody>
        <tr>
            <td>${requestScope.firstPlayerName}</td>
            <td>${requestScope.firstPlayerScore.getSetPoints()}</td>
        </tr>
        <tr>
            <td>${requestScope.secondPlayerName}</td>
            <td>${requestScope.secondPlayerScore.getSetPoints()}</td>
        </tr>
        </tbody>
    </table>
</div>
<div class="tennis-players-block">
    <img class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player5.png" alt="tennis player"/>
    <img class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player6.jpg" alt="tennis player"/>
    <img class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player7.jpg" alt="tennis player"/>
</div>
</body>
</html>
