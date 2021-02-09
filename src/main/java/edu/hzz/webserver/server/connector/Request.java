package edu.hzz.webserver.server.connector;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 GET /index.html HTTP/1.1
 Host: localhost:9898
 Connection: keep-alive
 Upgrade-Insecure-Requests: 1
 */
public class Request implements ServletRequest {
    private InputStream inputStream;
    private String uri;

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }
    //  GET /index.html HTTP/1.1
    public void parse() throws IOException {

        byte[] buffer = new byte[1024];

/**  如果对方不关闭流则导致，read一直处于一个阻塞状态，为了方便演示，则只读一次，而忽略http协议接受的细节
        StringBuilder stringBuilder = new StringBuilder();
        int length = 0;
        while((length = inputStream.read(buffer)) != -1){
            System.out.println("读取连接中... ... ");
            stringBuilder.append(new String(buffer,0,length));
        }
      String msg = stringBuilder.toString();
*/
    //  只读一次
        inputStream.read(buffer);
        String msg = new String(buffer);

        System.out.println(msg);
        uri = parseUri(msg);
    }

    private String parseUri(String msg){
        int start,end;
        if((start = msg.indexOf(" "))>0
                && ((end = msg.indexOf(" ",start+1))>start)){   //  可以指定起始位置
            return  msg.substring(start+1,end);
        }
        return "";
    }

    public String getRequestURI(){
        return uri;
    }


    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }
}
