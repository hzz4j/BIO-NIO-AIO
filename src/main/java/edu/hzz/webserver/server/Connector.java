package edu.hzz.webserver.server;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector implements Runnable{
    private static final int DEFAULT_PORT = 9898;
    private int port;

    public Connector(){this(DEFAULT_PORT);}
    public Connector(int port){this.port = port;}

    private ServerSocket serverSocket;

    public void start(){
        new Thread(this).start();
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.printf("服务器启动，访问: http://localhost:%s \n",port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("接受到新的请求");
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                Request request = new Request(inputStream);
                request.parse();
                Response response = new Response(outputStream);
                response.setRequest(request);

                StaticProcessor processor = new StaticProcessor();
                processor.process(request,response);

                close(socket);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            close(serverSocket);
        }
    }

    public void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
