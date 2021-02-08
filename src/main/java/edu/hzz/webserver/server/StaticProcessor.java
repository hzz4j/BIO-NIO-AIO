package edu.hzz.webserver.server;

import java.io.*;
import java.net.URL;

public class StaticProcessor {

    public void process(Request request,Response response)  {
        try {
            response.sendStaticResource();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
