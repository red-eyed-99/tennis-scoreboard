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
import paginator.MatchesPaginator;
import utils.ResponseErrorSender;

import java.util.List;

@WebServlet(urlPatterns = {"/matches", "/matches*"})
public class MatchesServlet extends HttpServlet {

    @Override
    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
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

        request.setAttribute("totalMatches", finishedMatches.size());
        request.setAttribute("matchesToShow", matchesToShow);

        request.setAttribute("filterPlayerName", filterByPlayerName);

        request.setAttribute("startPageNumber", pagesRange.getStartPageNumber());
        request.setAttribute("endPageNumber", pagesRange.getEndPageNumber());
        request.setAttribute("currentPage", pageNumber);
        request.setAttribute("totalPages", matchesPaginator.getTotalPages());

        request.getRequestDispatcher("/WEB-INF/jsp/matches.jsp")
                .forward(request, response);
    }

    private List<Match> getFinishedMatches(String filterByPlayerName) {
        @Cleanup
        var session = HibernateUtil.getSession();

        if (filterByPlayerName != null) {
            if (!filterByPlayerName.isBlank()) {
                return new FinishedMatchesService(session).findByPlayerName(filterByPlayerName);
            }
        }

        return new FinishedMatchesService(session).findAll();
    }
}
