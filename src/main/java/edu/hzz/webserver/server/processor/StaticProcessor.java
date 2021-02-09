package edu.hzz.webserver.server.processor;

import edu.hzz.webserver.server.connector.Request;
import edu.hzz.webserver.server.connector.Response;

import java.io.*;

public class StaticProcessor {

    public void process(Request request, Response response)  {
        try {
            response.sendStaticResource();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
