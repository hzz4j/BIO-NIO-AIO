import edu.hzz.webserver.server.connector.ConnectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9898);


            OutputStream outputStream = socket.getOutputStream();
//            outputStream.write(ConnectionUtils.REQUEST.getBytes());

            outputStream.write("GET /servlet/TimeServlet HTTP/1.1".getBytes());
            //  关闭输出流，两个效果
            //  1. 对客户端而言，节省资源
            //  2. 对服务端而言，不用一直阻塞在read
            socket.shutdownOutput();

            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while((length = inputStream.read(buffer)) != -1){
                stringBuilder.append(new String(buffer,0,length));
            }
            System.out.println(stringBuilder);
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
