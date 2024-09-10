import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class RequestHandler {

    Logger logger = LoggerConfig.getLogger(this.getClass().getName());

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

        String[] coordinates = request
                .replace("{", "")
                .replace("}", "")
                .split(":");

        double[] values = new double[3];

        try {
            values[0] = Double.parseDouble(coordinates[1].split(",")[0].replace(",", "."));
            values[1] = Double.parseDouble(coordinates[2].split(",")[0].replace(",", "."));
            values[2] = Double.parseDouble(coordinates[3].replace(",", "."));
        } catch (NumberFormatException e) {
            logger.info("NumberFormatException");
            return false;
        }

        logger.info("Before check hit values: " + Arrays.toString(values));

        return HitChecker.checkHit(values[2], values[0], values[1]);
    }
}
