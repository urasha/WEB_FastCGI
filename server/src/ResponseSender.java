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

            boolean result = requestHandler.handle();

            double totalTime = (System.currentTimeMillis() - startTime) / 1000;

            String httpResponse = getHttpResponse(result, totalTime);

            System.out.println(httpResponse);
        }
    }

    private String getHttpResponse(boolean result, double totalTime) {
        var content = """
                {
                "isHit": %s,
                "time": %s
                }
                """.formatted(result ? "true" : "false", String.valueOf(totalTime));

        var httpResponse = """
                HTTP/1.1 200 OK
                Content-Type: application/json
                Content-Length: %d
                                   
                %s
                """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);

        return httpResponse;
    }
}
