package edu.hzz.aio.echo.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

public class Server {
    private final int port = 9898;
    private AsynchronousServerSocketChannel serverChannel;


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private void start() {
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(port));
            while(true) {
                //  异步接收新的连接
                serverChannel.accept(serverChannel, new AdapterHandler());
                //  模拟阻塞，不让server程序结束
                System.in.read();
                System.out.println("---------------------------");
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            close(serverChannel);
        }
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
                System.out.println("关闭" + closable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
