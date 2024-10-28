package filters;

import exceptions.BadRequestException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import utils.ResponseErrorSender;

@WebFilter("/match-score")
public class MatchScoreValidationFilter implements Filter {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        try {
            var uuid = request.getParameter("uuid");
            var point = request.getParameter("point");

            if (uuid == null || !uuidIsValid(uuid)) {
                throw new BadRequestException("Invalid UUID parameter");
            }

            if (request.getMethod().equalsIgnoreCase("POST")) {
                validatePointParameter(point);
            }

        } catch (BadRequestException ex) {
            ResponseErrorSender.sendError(response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean uuidIsValid(String uuid) {
        return uuid.matches("^[a-fA-F\\d]{8}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{4}-[a-fA-F\\d]{12}$");
    }

    private void validatePointParameter(String point) throws BadRequestException {
        var errorMessage = "Invalid point parameter";

        try {
            var pointValue = Integer.parseInt(point);

            if (pointValue != 1 && pointValue != 2) {
                throw new BadRequestException(errorMessage);
            }

        } catch (NumberFormatException ex) {
            throw new BadRequestException(errorMessage);
        }
    }
}
