import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class RequestHandler {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ResponseStatus handle() throws IOException {
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

        if (!validateInputValues(inputValues)) {
            logger.info("Validation failed!");
            return ResponseStatus.VALIDATION_FAILED;
        }

        double[] values = new double[3];

        values[0] = Integer.parseInt(inputValues[0]);
        values[1] = Double.parseDouble(inputValues[1]);
        values[2] = Double.parseDouble(inputValues[2]);

        logger.info("Before check hit values: " + Arrays.toString(values));

        boolean isHit = HitChecker.checkHit(values[2], values[0], values[1]);

        return isHit ? ResponseStatus.SUCCESS : ResponseStatus.HIT_FAILED;
    }

    private boolean validateInputValues(String[] inputValues) {
        int[] validXValues = {-4, -3, -2, -1, 0, 1, 2, 3, 4};
        try {
            int x = Integer.parseInt(inputValues[0]);
            if (Arrays.stream(validXValues).noneMatch(v -> v == x)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        try {
            double y = Double.parseDouble(inputValues[1]);
            if (y < -5 || y > 5) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        double[] validRValues = {1, 1.5, 2, 2.5, 3};
        try {
            double r = Double.parseDouble(inputValues[2]);
            if (Arrays.stream(validRValues).noneMatch(v -> v == r)) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
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
