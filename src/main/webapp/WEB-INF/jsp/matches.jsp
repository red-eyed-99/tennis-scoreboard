<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Matches</title>
</head>
<body>
<h1>History of matches</h1>
<div>
    <form method="get">
        <label for="player-name-filter">Player name</label>
        <input type="text" id="player-name-filter" name="filter_by_player_name"
               placeholder="Search match by player name">
        <button type="submit" name="filter_by_player_name">Find</button>
    </form>
    <form>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>First player</th>
                <th>Second player</th>
                <th>Winner</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${matchesToShow.size() == 0}">
                <tr>
                    <td>No matches</td>
                </tr>
            </c:if>
            <c:if test="${matchesToShow.size() > 0}">
                <c:forEach var="match" items="${matchesToShow}">
                    <tr>
                        <td><c:out value="${match.getId()}"/></td>
                        <td><c:out value="${match.getFirstPlayer().getName()}"/></td>
                        <td><c:out value="${match.getSecondPlayer().getName()}"/></td>
                        <td><c:out value="${match.getWinner().getName()}"/></td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
        <c:if test="${matchesToShow.size() < totalMatches}">
            <c:forEach var="index" begin="${requestScope.startPageNumber}" end="${requestScope.endPageNumber}">
                <button
                        <c:if test="${requestScope.currentPage == index}">class="currentPage"</c:if>
                        type="submit" name="page" value="${index}">${index}</button>
            </c:forEach>
        </c:if>
    </form>
</div>
</body>
</html>
