package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;
import models.entities.Match;
import models.entities.Player;
import services.OngoingMatchesService;
import services.PlayerService;
import utils.HibernateUtil;
import utils.NewMatchAttributeNames;

import java.util.Optional;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        request.getRequestDispatcher("/WEB-INF/jsp/new-match.jsp")
                .forward(request, response);
    }

    @Override
    @SneakyThrows
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        var firstPlayerName = request.getParameter("first-player");
        var secondPlayerName = request.getParameter("second-player");

        var players = getPlayers(firstPlayerName, secondPlayerName);

        var firstPlayer = players[0];
        var secondPlayer = players[1];

        var uuid = startNewMatch(firstPlayer, secondPlayer);

        String path = request.getContextPath() + "/match-score?uuid=" + uuid;
        request.setAttribute(NewMatchAttributeNames.FIRST_PLAYER_NAME, firstPlayer);
        request.setAttribute(NewMatchAttributeNames.SECOND_PLAYER_NAME, secondPlayer);

        response.sendRedirect(path);
    }

    private Player[] getPlayers(String firstPlayerName, String secondPlayerName) {
        @Cleanup
        var session = HibernateUtil.getSession();

        var playerService = new PlayerService(session);

        var firstPlayer = playerService.findByName(firstPlayerName);
        var secondPlayer = playerService.findByName(secondPlayerName);

        if (firstPlayer.isEmpty()) {
            firstPlayer = Optional.of(new Player(firstPlayerName));
            playerService.persist(firstPlayer.get());
        }

        if (secondPlayer.isEmpty()) {
            secondPlayer = Optional.of(new Player(secondPlayerName));
            playerService.persist(secondPlayer.get());
        }

        return new Player[]{firstPlayer.get(), secondPlayer.get()};
    }

    private UUID startNewMatch(Player firstPlayer, Player secondPlayer) {
        var match = new Match(firstPlayer, secondPlayer);

        var uuid = UUID.randomUUID();

        OngoingMatchesService.addNewMatch(uuid, match);

        return uuid;
    }
}

