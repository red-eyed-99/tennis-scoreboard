<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Match score</title>
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<div class="match-score-block">
    <table>
        <caption>Match score</caption>
        <thead>
        <tr>
            <th>Player</th>
            <th>Points</th>
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
            <td>${firstPlayerScore.getGamePoints()}</td>
            <td>${firstPlayerScore.getSetPoints()}</td>
            <c:if test="${tiebreak == true}">
                <td>${firstPlayerScore.getTiebreakPoints()}</td>
            </c:if>
        </tr>
        <tr>
            <td>${secondPlayerName}</td>
            <td>${secondPlayerScore.getPoints()}</td>
            <td>${secondPlayerScore.getGamePoints()}</td>
            <td>${secondPlayerScore.getSetPoints()}</td>
            <c:if test="${tiebreak == true}">
                <td>${secondPlayerScore.getTiebreakPoints()}</td>
            </c:if>
        </tr>
        </tbody>
    </table>
    <form method="post">
        <button type="submit" name="point" value="1">First player wins a point</button>
        <button type="submit" name="point" value="2">Second player wins a point</button>
    </form>
</div>
</body>
</html>
