package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

abstract class Handler implements HttpHandler {

    protected final String badRequest = "Bad request";
    protected final String notFound = "Not Found";
    protected final String notAllowed = "Method not Allowed";
    protected final String notAcceptable = "Not Acceptable";
    protected final String serverError = "Internal Server Error";
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }
}
