package filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

@WebFilter(urlPatterns = {"/matches", "/matches*"})
public class MatchesRedirectFilter implements Filter {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        var page = request.getParameter("page");
        var filterByPlayerName = request.getParameter("filter_by_player_name");

        if (page == null && filterByPlayerName == null) {
            response.sendRedirect(request.getContextPath() + "/matches?page=1");
            return;
        }

        if (page == null) {
            response.sendRedirect(request.getContextPath() + "/matches?page=1&filter_by_player_name=" + filterByPlayerName);
            return;
        }

        if (filterByPlayerName != null && filterByPlayerName.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/matches?page=" + page);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
