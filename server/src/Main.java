import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ResponseSender responseSender = new ResponseSender();

        while (true) {
            responseSender.send();
        }
    }
}
