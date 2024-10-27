package servlets;

import exceptions.PageNotFoundException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.SneakyThrows;
import models.entities.Match;
import services.FinishedMatchesService;
import utils.HibernateUtil;
import utils.MatchesPaginator;
import utils.ResponseErrorSender;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/matches", "/matches*"})
public class MatchesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var page = request.getParameter("page");
        var filterByPlayerName = request.getParameter("filter_by_player_name");

        if (page == null && filterByPlayerName == null) {
            response.sendRedirect("/matches?page=1");
            return;
        }

        if (page == null) {
            response.sendRedirect("/matches?page=1&filter_by_player_name=" + filterByPlayerName);
            return;
        }

        try {
            showMatches(request, response);
        } catch (NumberFormatException | PageNotFoundException ex) {
            ResponseErrorSender.sendError(response, ex);
        }
    }

    @SneakyThrows
    private void showMatches(HttpServletRequest request, HttpServletResponse response) throws PageNotFoundException {
        var pageNumber = Integer.parseInt(request.getParameter("page"));
        var filterByPlayerName = request.getParameter("filter_by_player_name");

        var finishedMatches = getFinishedMatches(filterByPlayerName);

        var matchesPaginator = new MatchesPaginator(finishedMatches);

        var matchesToShow = matchesPaginator.getMatchesFromPage(pageNumber);

        var pagesRange = matchesPaginator.getPagesRange(pageNumber);

        request.setAttribute("startPageNumber", pagesRange.getStartPageNumber());
        request.setAttribute("endPageNumber", pagesRange.getEndPageNumber());

        request.setAttribute("totalMatches", finishedMatches.size());
        request.setAttribute("currentPage", pageNumber);
        request.setAttribute("matchesToShow", matchesToShow);

        request.getRequestDispatcher("/WEB-INF/jsp/matches.jsp")
                .forward(request, response);
    }

    private List<Match> getFinishedMatches(String filterByPlayerName) {
        @Cleanup
        var session = HibernateUtil.getSession();

        if (filterByPlayerName != null) {
            return new FinishedMatchesService(session).findByPlayerName(filterByPlayerName);
        }

        return new FinishedMatchesService(session).findAll();
    }
}
