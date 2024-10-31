<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Matches</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/icons/page.ico">
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<div id="matches-block" class="main-block">
    <h1>History of matches</h1>
    <form class="player-name-filter-form">
        <label for="player-name-filter">Search matches by player name</label>
        <input type="text" id="player-name-filter" name="filter_by_player_name"
               placeholder="Alexander Zverev" value="${filterPlayerName}">
        <button type="submit" name="filter_by_player_name">Search</button>
    </form>
    <table id="matches-table">
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
                <td colspan="4">No matches</td>
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
                <div class="page-block">
                    <a href="?page=1<c:if test='${not empty filterPlayerName}'>${filterByPlayerName}</c:if>">
                        <img class="page-icon" src="${pageContext.request.contextPath}/images/icons/first-page-icon.png" alt="first page"/>
                    </a>
                </div>
            </c:if>

            <c:forEach var="index" begin="${requestScope.startPageNumber}" end="${requestScope.endPageNumber}">

                <div class="page-block">
                    <a href="?page=${index}<c:if test='${not empty filterPlayerName}'>${filterByPlayerName}</c:if>">
                        <img class="page-icon" src="${pageContext.request.contextPath}/images/icons/tennis-ball-icon.png" alt=""/>
                        <span class="overlay-page-number" <c:if test="${requestScope.currentPage == index}">id="current-page"</c:if>>
                                ${index}
                        </span>
                    </a>
                </div>

            </c:forEach>

            <c:if test="${totalPages > 5}">
                <div class="page-block">
                    <a href="?page=${totalPages}<c:if test='${not empty filterPlayerName}'>${filterByPlayerName}</c:if>">
                        <img class="page-icon" src="${pageContext.request.contextPath}/images/icons/last-page-icon.png" alt="last page"/>
                    </a>
                </div>
            </c:if>

        </c:if>
    </div>
</div>
</body>
</html>
