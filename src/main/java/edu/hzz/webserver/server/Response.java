package edu.hzz.webserver.server;

import java.io.*;

/**
 HTTP/1.1 200 OK
 */
public class Response {
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
            write(file,HttpStatus.SC_OK);
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
}
