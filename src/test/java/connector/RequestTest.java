package connector;

import edu.hzz.webserver.server.connector.Request;
import org.junit.Assert;
import org.junit.Test;
import utils.TestUtils;

import java.io.IOException;

public class RequestTest {
    private static final String validRequest = "GET /index.html HTTP/1.1";

    @Test
    public void testRequest_getRequestUri() throws IOException {
        Request request = TestUtils.createRequest(validRequest);
        Assert.assertEquals("/index.html",request.getRequestURI());
    }

}

