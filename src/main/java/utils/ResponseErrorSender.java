package utils;

import exceptions.BadRequestException;
import exceptions.PageNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseErrorSender {

    @SneakyThrows
    public void sendError(HttpServletResponse response, Exception exception) {

        if (exception instanceof BadRequestException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
        }

        if (exception instanceof PageNotFoundException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }
}
