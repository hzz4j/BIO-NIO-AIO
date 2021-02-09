package edu.hzz.webserver.server.connector;

import edu.hzz.webserver.server.connector.ConnectionUtils;
import edu.hzz.webserver.server.connector.HttpStatus;
import edu.hzz.webserver.server.connector.Request;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.Locale;

/**
 HTTP/1.1 200 OK
 */
public class Response implements ServletResponse {
    private Request request;
    private OutputStream outputStream;

    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(Request request){
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        //  FIle = path+文件名
        File file = new File(ConnectionUtils.WEB_RESOURCE_ROOT,request.getRequestURI());
        try {
            write(file, HttpStatus.SC_OK);
        } catch (IOException exception) {
            File errorFile = new File(ConnectionUtils.WEB_RESOURCE_ROOT,"404.html");
            write(errorFile,HttpStatus.SC_NOT_FOUND);
        }

    }
    private void write(File file,HttpStatus httpStatus) throws IOException {
        //  处理关闭流
        try(InputStream inputStream = new FileInputStream(file)){
            //  写http返回的状态
            outputStream.write(ConnectionUtils.renderStatus(httpStatus).getBytes());
            //  写文件数据
            byte[] buffer = new byte[1024];
            int length = 0;
            while((length = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }
        }
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(outputStream,true);
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
