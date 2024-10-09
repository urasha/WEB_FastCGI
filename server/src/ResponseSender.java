import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseSender {

    private final RequestHandler requestHandler = new RequestHandler();

    public void send() throws IOException {
        var fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            // calculate script running time
            double startTime = System.currentTimeMillis();

            ResponseStatus result = requestHandler.handle();

            double totalTime = (System.currentTimeMillis() - startTime) / 1000;

            String httpResponse = getHttpResponse(result, totalTime);

            System.out.println(httpResponse);
        }
    }

    private String getHttpResponse(ResponseStatus result, double totalTime) {
        String content;
        String statusLine;

        switch (result) {
            case SUCCESS:
            case HIT_FAILED:
                content = """
                        {
                        "isHit": %s,
                        "time": %s
                        }
                        """.formatted(result.isHit(), String.valueOf(totalTime));
                statusLine = "HTTP/1.1 200 OK";
                break;

            case VALIDATION_FAILED:
            default:
                content = """
                        {
                        "error": "Validation Error"
                        }
                        """;
                statusLine = "HTTP/1.1 400 Bad Request";
                break;
        }

        var httpResponse = """
                %s
                Content-Type: application/json
                Content-Length: %d
                                   
                %s
                """.formatted(statusLine, content.getBytes(StandardCharsets.UTF_8).length, content);

        return httpResponse;
    }
}
