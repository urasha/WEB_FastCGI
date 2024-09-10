import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResponseSender {

    private final RequestHandler requestHandler = new RequestHandler();

    public void send() throws IOException {
        var fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            boolean result = requestHandler.handle();

            var content = """
                    {
                    "isHit": %s
                    }
                    """.formatted(result ? "true" : "false");

            var httpResponse = """
                    HTTP/1.1 200 OK
                    Content-Type: application/json
                    Content-Length: %d
                                       
                    %s
                    """.formatted(content.getBytes(StandardCharsets.UTF_8).length, content);

            System.out.println(httpResponse);
        }
    }
}
