package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;
import models.CalculationMethod;
import models.MatchScore;
import models.PlayerScore;
import models.entities.Match;
import services.FinishedMatchesService;
import services.MatchScoreCalculationService;
import services.OngoingMatchesService;
import utils.HibernateUtil;

import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var uuid = request.getParameter("uuid");

        var match = OngoingMatchesService.findMatch(UUID.fromString(uuid));

        var match = ongoingMatches.get(UUID.fromString(uuid));

        configureRequestAttributes(request, match.get());

        request.getRequestDispatcher("/WEB-INF/jsp/match-score.jsp")
                .forward(request, response);
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var uuid = request.getParameter("uuid");
        var wonPointPlayerNumber = Integer.parseInt(request.getParameter("point"));

        var match = OngoingMatchesService.getOngoingMatches()
                .get(UUID.fromString(uuid));
        var match = OngoingMatchesService.findMatch(UUID.fromString(uuid));

        updateMatchScore(match.get(), wonPointPlayerNumber);

        if (isMatchWinnerDetermined(match.get())) {
            processFinishedMatch(request, response, match.get(), uuid);
        }

        configureRequestAttributes(request, match.get());

        request.getRequestDispatcher("/WEB-INF/jsp/match-score.jsp")
                .forward(request, response);
    }

    private void updateMatchScore(Match match, int wonPointPlayerNumber) {
        var matchScore = match.getMatchScore();

        var wonPointPlayerScore = getWonPointPlayerScore(matchScore, wonPointPlayerNumber);

        var calculationService = new MatchScoreCalculationService(matchScore, wonPointPlayerScore);

        calculationService.updateMatchScore();
    }

    private PlayerScore getWonPointPlayerScore(MatchScore matchScore, int wonPointPlayerNumber) {
        if (wonPointPlayerNumber == 1) {
            return matchScore.getFirstPlayerScore();
        } else {
            return matchScore.getSecondPlayerScore();
        }
    }

    private boolean isMatchWinnerDetermined(Match match) {
        var matchScore = match.getMatchScore();

        var firstPlayerSetPoints = matchScore.getFirstPlayerScore().getSetPoints();
        var secondPlayerSetPoints = matchScore.getSecondPlayerScore().getSetPoints();

        if (firstPlayerSetPoints == 2 || secondPlayerSetPoints == 2) {

            if (firstPlayerSetPoints > secondPlayerSetPoints) {
                match.setWinner(match.getFirstPlayer());
            } else {
                match.setWinner(match.getSecondPlayer());
            }

            return true;
        }

        return false;
    }

    @SneakyThrows
    private void processFinishedMatch(HttpServletRequest request, HttpServletResponse response, Match match, String uuid) {
        OngoingMatchesService.removeFinishedMatch(UUID.fromString(uuid));

        saveFinishedMatch(match);

        configureRequestAttributes(request, match);

        request.getRequestDispatcher("/WEB-INF/jsp/match-result.jsp")
                .forward(request, response);
    }

    private void configureRequestAttributes(HttpServletRequest request, Match match) {
        request.setAttribute("firstPlayerName", match.getFirstPlayer().getName());
        request.setAttribute("secondPlayerName", match.getSecondPlayer().getName());

        var matchScore = match.getMatchScore();

        request.setAttribute("firstPlayerScore", matchScore.getFirstPlayerScore());
        request.setAttribute("secondPlayerScore", matchScore.getSecondPlayerScore());

        if (matchScore.getCalculationMethod() == CalculationMethod.TIEBREAK) {
            request.setAttribute("tiebreak", true);
        }

        if (match.getWinner() != null) {
            request.setAttribute("winner", match.getWinner().getName());
        }
    }

    private void saveFinishedMatch(Match match) {
        @Cleanup
        var session = HibernateUtil.getSession();

        var finishedMatchesService = new FinishedMatchesService(session);

        finishedMatchesService.persist(match);
    }
}

