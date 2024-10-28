package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import validator.RequestParameter;
import validator.NewMatchParamsValidator;

import static utils.NewMatchAttributeNames.*;

@WebFilter("/new-match")
public class NewMatchValidationFilter implements Filter {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        if (request.getMethod().equalsIgnoreCase("POST")) {
            var firstPlayerName = request.getParameter("first-player");
            var secondPlayerName = request.getParameter("second-player");

            if (!requestParametersAreValid(request, firstPlayerName, secondPlayerName)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                request.getRequestDispatcher("/WEB-INF/jsp/new-match.jsp")
                        .forward(request, response);

                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean requestParametersAreValid(HttpServletRequest request,
                                              String firstPlayerValue,
                                              String secondPlayerValue) {

        var firstPlayerParameter = new RequestParameter(FIRST_PLAYER_NAME, firstPlayerValue);
        var secondPlayerParameter = new RequestParameter(SECOND_PLAYER_NAME, secondPlayerValue);

        var requestParameters = new RequestParameter[2];

        requestParameters[0] = firstPlayerParameter;
        requestParameters[1] = secondPlayerParameter;

        return NewMatchParamsValidator.parametersAreValid(request, requestParameters);
    }
}
