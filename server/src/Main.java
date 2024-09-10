import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        ResponseSender responseSender = new ResponseSender(requestHandler);

        while (true) {
            responseSender.send();
        }
    }
}
