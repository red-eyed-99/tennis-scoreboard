<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Matches</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<h1>History of matches</h1>
<div class="main-block">
    <form class="player-name-filter-form">
        <label for="player-name-filter">Player name</label>
        <input type="text" id="player-name-filter" name="filter_by_player_name"
               placeholder="Search match by player name" value="${filterPlayerName}">
        <button type="submit" name="filter_by_player_name">Search</button>
    </form>
    <table class="matches-table">
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
    <div class="pagination-block">

        <c:set var="filterByPlayerName" value="&amp;filter_by_player_name=${filterPlayerName}"/>

        <c:if test="${matchesToShow.size() < totalMatches}">

            <c:if test="${totalPages > 5}">
                <a href="?page=1<c:if test='${not empty filterPlayerName}'>${filterByPlayerName}</c:if>">|<-</a>
            </c:if>

            <c:forEach var="index" begin="${requestScope.startPageNumber}" end="${requestScope.endPageNumber}">

                <a href="?page=${index}<c:if test='${not empty filterPlayerName}'>${filterByPlayerName}</c:if>"
                   <c:if test="${requestScope.currentPage == index}">class="currentPage"</c:if>>${index}</a>

            </c:forEach>

            <c:if test="${totalPages > 5}">
                <a href="?page=${totalPages}<c:if test='${not empty filterPlayerName}'>${filterByPlayerName}</c:if>">->|</a>
            </c:if>

        </c:if>
    </div>
</div>
</body>
</html>
