<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Match score</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/icons/page.ico">
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<div id="match-score-block" class="main-block">
    <table>
        <caption>Match score</caption>
        <thead>
        <tr>
            <th>Player</th>
            <th>Points</th>
            <c:if test="${advantage == true}">
                <th></th>
            </c:if>
            <th>Games</th>
            <th>Sets</th>
            <c:if test="${tiebreak == true}">
                <th>TB Points</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${firstPlayerName}</td>
            <td>${firstPlayerScore.getPoints()}</td>
            <c:if test="${advantage == true}">
                <td>
                    <c:if test="${firstPlayerScore.hasAdvantage() == true}">
                        adv.
                    </c:if>
                </td>
            </c:if>
            <td>${firstPlayerScore.getGamePoints()}</td>
            <td>${firstPlayerScore.getSetPoints()}</td>
            <c:if test="${tiebreak == true}">
                <td>${firstPlayerScore.getTiebreakPoints()}</td>
            </c:if>
        </tr>
        <tr>
            <td>${secondPlayerName}</td>
            <td>${secondPlayerScore.getPoints()}</td>
            <c:if test="${advantage == true}">
                <td>
                    <c:if test="${secondPlayerScore.hasAdvantage() == true}">
                        adv.
                    </c:if>
                </td>
            </c:if>
            <td>${secondPlayerScore.getGamePoints()}</td>
            <td>${secondPlayerScore.getSetPoints()}</td>
            <c:if test="${tiebreak == true}">
                <td>${secondPlayerScore.getTiebreakPoints()}</td>
            </c:if>
        </tr>
        </tbody>
    </table>
    <form method="post">
        <button class="win-point-button" type="submit" name="point" value="1">First player wins a point</button>
        <button class="win-point-button" type="submit" name="point" value="2">Second player wins a point</button>
    </form>
</div>
</body>
</html>
