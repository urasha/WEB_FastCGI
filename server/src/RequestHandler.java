import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class RequestHandler {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public boolean handle() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();

        String request = new String(requestBodyRaw, StandardCharsets.UTF_8);
        String[] inputValues = parseRequest(request);

        // try to get numbers (coordinates' value) from user input
        double[] values = new double[3];

        try {
            values[0] = Double.parseDouble(inputValues[0]);
            values[1] = Double.parseDouble(inputValues[1]);
            values[2] = Double.parseDouble(inputValues[2]);
        } catch (NumberFormatException e) {
            logger.info("Numbers exception!");
            return false;
        }

        logger.info("Before check hit values: " + Arrays.toString(values));

        return HitChecker.checkHit(values[2], values[0], values[1]);
    }

    private String[] parseRequest(String request) {
        String[] coordinates = request
                .replace("{", "")
                .replace("}", "")
                .split(":");

        String xValue = coordinates[1].split(",")[0].replace(",", ".");
        String yValue = coordinates[2].split(",")[0].replace(",", ".");
        String rValue = coordinates[3].split(",")[0].replace(",", ".");

        return new String[]{xValue, yValue, rValue};
    }
}
