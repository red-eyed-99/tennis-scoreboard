package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.entities.Match;
import services.OngoingMatchesService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var uuid = request.getParameter("uuid");

        if (!uuid.matches("^[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}$")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        var ongoingMatches = OngoingMatchesService.getOngoingMatches();

        var match = ongoingMatches.get(UUID.fromString(uuid));

        configureRequestAttributes(request, match);

        request.getRequestDispatcher("/WEB-INF/jsp/match-score.jsp")
                .forward(request, response);
    }

    private void configureRequestAttributes(HttpServletRequest request, Match match) {
        request.setAttribute("firstPlayerName", match.getFirstPlayer().getName());
        request.setAttribute("secondPlayerName", match.getSecondPlayer().getName());

        request.setAttribute("firstPlayerScore", match.getMatchScore().getFirstPlayerScore());
        request.setAttribute("secondPlayerScore", match.getMatchScore().getSecondPlayerScore());
    }
}

