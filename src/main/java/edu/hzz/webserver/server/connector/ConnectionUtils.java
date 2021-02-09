package edu.hzz.webserver.server.connector;

import java.io.File;

public abstract class ConnectionUtils {
    public static final String WEB_RESOURCE_ROOT;
    public static final String CLASS_ROOT;
    public static final String SERVLET_PACKAGE = "edu.hzz.servlet";
    public static final String REQUEST = "GET /index.html HTTP/1.1";
    public static final String PROTOCOL = "HTTP/1.1";
    public static final String CARRIAGE = "\r";
    public static final String NEWLINE = "\n";
    public static final String SPACE = " ";
    public static final String DOT = ".";


    static{
        WEB_RESOURCE_ROOT = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator).append("src")
                .append(File.separator).append("main")
                .append(File.separator).append("resources")
                .toString();

        //  D:\Code\Java\Java-IO\BIO\src\main\java\edu\hzz\servlet
        CLASS_ROOT = new StringBuilder(System.getProperty("user.dir"))
                .append(File.separator).append("src")
                .append(File.separator).append("main")
                .append(File.separator).append("java")
                .toString();
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
