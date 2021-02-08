import edu.hzz.webserver.server.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {
    public static Request createRequest(String requestStr) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(requestStr.getBytes());
        Request request = new Request(inputStream);
        request.parse();
        return  request;
    }

    public static String readFile2String(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
