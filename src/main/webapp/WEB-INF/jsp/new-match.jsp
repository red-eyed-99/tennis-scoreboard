<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New match</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<jsp:include page="navigation-bar.jsp"/>
<div id="new-match-block" class="main-block">
    <h1>New match</h1>
    <form action="${pageContext.request.contextPath}/new-match" method="post">
        <div class="form-block">
            <label for="new-match-first-player">First player:</label>
            <input type="text" id="new-match-first-player" name="first-player" placeholder="Novak Djokovic"
                   value="${requestScope.firstPlayerName}" required>
            <c:if test="${not empty requestScope.firstPlayerErrorMessage}">
                <style>
                    #new-match-first-player {
                        border: 2px solid red;
                    }
                </style>
                <span class="error-message">${requestScope.firstPlayerErrorMessage}</span>
            </c:if>
        </div>
        <div class="form-block">
            <label for="new-match-second-player">Second player:</label>
            <input type="text" id="new-match-second-player" name="second-player"
                   value="${requestScope.secondPlayerName}" placeholder="Rafael Nadal" required>
            <c:if test="${not empty requestScope.secondPlayerErrorMessage}">
                <style>
                    #new-match-second-player {
                        border: 2px solid red;
                    }
                </style>
                <span class="error-message">${requestScope.secondPlayerErrorMessage}</span>
            </c:if>
        </div>
        <button type="submit">Start</button>
    </form>
</div>
<div class="tennis-players-block">
    <img id="tennis-player1" class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player1.png" alt="tennis player1"/>
    <img id="tennis-player2" class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player2.jpg" alt="tennis player2"/>
    <img id="tennis-player3" class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player3.jpg" alt="tennis player3"/>
    <img id="tennis-player4" class="tennis-player" src="${pageContext.request.contextPath}/images/tennis-player4.jpg" alt="tennis player4"/>
</div>
</body>
</html>