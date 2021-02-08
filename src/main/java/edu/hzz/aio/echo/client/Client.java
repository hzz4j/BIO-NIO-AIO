package edu.hzz.aio.echo.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Client {

    private AsynchronousSocketChannel socketChannel;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }


    private void start() {
        try {
            socketChannel = AsynchronousSocketChannel.open();
            Future<Void> connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 9898));
            connect.get();
            System.out.println("连接到服务器");
            Scanner scanner = new Scanner(System.in);
            while(true){
                //  写

                String msg = scanner.nextLine();
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                Future<Integer> echo = socketChannel.write(buffer);
                echo.get();

                //  读
                ByteBuffer buffer2 = ByteBuffer.allocate(1024);
                buffer2.clear();
                Future<Integer> read = socketChannel.read(buffer2);
                read.get();
                System.out.println(new String(buffer.array()));
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }finally{
            close(socketChannel);
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
