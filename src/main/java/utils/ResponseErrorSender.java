package utils;

import exceptions.PageNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseErrorSender {

    public static void sendError(HttpServletResponse response, Exception exception) throws IOException {

        if (exception instanceof NumberFormatException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page number");
        }

        if (exception instanceof PageNotFoundException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, exception.getMessage());
        }
    }
}
