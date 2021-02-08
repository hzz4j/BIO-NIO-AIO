package edu.hzz.webserver.server;


import java.io.IOException;
import java.io.InputStream;

/**

 */
public class Request {
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

}
