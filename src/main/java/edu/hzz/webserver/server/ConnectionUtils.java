package edu.hzz.webserver.server;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.File;

public abstract class ConnectionUtils {
//    D:\Code\Java\Java-IO\BIO\src\main\resources\index.html
    //D:\Code\Java\Java-IO\BIO\src\main\java\edu\hzz\webserver\server\ConnectionUtils.java

    public static final String WEB_RESOURCE_ROOT;
    public static final String REQUEST = "GET /index.html HTTP/1.1";
    public static final String PROTOCOL = "HTTP/1.1";
    public static final String CARRIAGE = "\r";
    public static final String NEWLINE = "\n";
    public static final String SPACE = " ";


    static{
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.getProperty("user.dir"))
                .append(File.separator).append("src")
                .append(File.separator).append("main")
                .append(File.separator).append("resources");
        WEB_RESOURCE_ROOT = stringBuilder.toString();
    }

    //  HTTP1.1 200 OK 换行 空一行  http协议
    public static String renderStatus(HttpStatus status){
        //  注意处理StringBuilder的方式
        StringBuilder sb = new StringBuilder(PROTOCOL)
                .append(SPACE)
                .append(status.getStatusCode())
                .append(SPACE)
                .append(status.getReason())
                .append(CARRIAGE).append(NEWLINE)
                .append(CARRIAGE).append(NEWLINE);
        return sb.toString();

    }
}
