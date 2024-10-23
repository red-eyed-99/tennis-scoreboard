<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Match score</title>
</head>
<body>
<div class="match-score-block">
    <table>
        <caption>Match score</caption>
        <thead>
        <tr>
            <th>Player</th>
            <th>Sets</th>
            <th>Games</th>
            <th>Points</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${firstPlayerName}</td>
            <td>${firstPlayerScore.getPlayerPoints()}</td>
            <td>${firstPlayerScore.getPlayerGames()}</td>
            <td>${firstPlayerScore.getPlayerSets()}</td>
        </tr>
        <tr>
            <td>${secondPlayerName}</td>
            <td>${secondPlayerScore.getPlayerPoints()}</td>
            <td>${secondPlayerScore.getPlayerGames()}</td>
            <td>${secondPlayerScore.getPlayerSets()}</td>
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
