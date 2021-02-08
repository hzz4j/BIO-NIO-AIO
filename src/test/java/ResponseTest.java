import edu.hzz.webserver.server.ConnectionUtils;
import edu.hzz.webserver.server.Request;
import edu.hzz.webserver.server.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResponseTest {
    private static final String validRequest = "GET /index.html HTTP/1.1";
    private static final String inValidRequest = "GET /notfound.html HTTP/1.1";

    private static final String status200 = "HTTP/1.1 200 OK\r\n\r\n";
    private static final String status404 = "HTTP/1.1 404 File Not Found\r\n\r\n";

    //  测试有效的请求
    @Test
    public void givenValidRequest_thenReturnStaticResource() throws IOException {
        Request request = TestUtils.createRequest(validRequest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(out);
        response.setRequest(request);
        response.sendStaticResource();

        String resource = TestUtils.readFile2String(ConnectionUtils.WEB_RESOURCE_ROOT+request.getRequestURI());
        Assert.assertEquals(status200+resource,out.toString());

    }

    @Test
    public void givenInValidRequest_thenReturnError() throws IOException {
        Request request = TestUtils.createRequest(inValidRequest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Response response = new Response(out);
        response.setRequest(request);
        response.sendStaticResource();

        String resource = TestUtils.readFile2String(ConnectionUtils.WEB_RESOURCE_ROOT+"/404.html");
        Assert.assertEquals(status404+resource,out.toString());

    }
}

