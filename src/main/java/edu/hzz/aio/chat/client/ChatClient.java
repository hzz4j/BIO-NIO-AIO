package edu.hzz.aio.chat.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ChatClient {
    private AsynchronousSocketChannel socketChannel;
    private ByteBuffer buffer;
    private Charset charset;
    private String QUIT = "quit";

    public ChatClient(){
        buffer = ByteBuffer.allocate(1024);
        charset = Charset.forName("utf-8");
    }

    public void start() {
        try {
            socketChannel = AsynchronousSocketChannel.open();
            Future<Void> connect = socketChannel.connect(new InetSocketAddress("127.0.0.1", 9898));
            connect.get();
            System.out.println("成功连接到服务器");

            //  处理用户输入
            new Thread(new UserInputHandler(this)).start();

            //  接收服务器数据
            while (true) {
                //  接收服务器数据
                buffer.clear();
                Future<Integer> read = socketChannel.read(buffer);

                if (read.get() > 0) {
                    buffer.flip();
                    CharBuffer mgs = charset.decode(buffer);
                    System.out.println(mgs);
                } else {
                    //  退出
                    break;
                }
            }
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            System.out.println("退出");
            close(socketChannel);
        }
    }

    public void close(){
        close(socketChannel);
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

    public void sendMsg(String msg) throws ExecutionException, InterruptedException {
        ByteBuffer encodeMsg = charset.encode(msg);
        Future<Integer> result = socketChannel.write(encodeMsg);
        result.get();
    }

    public boolean readyToQuit(String msg) {
        return QUIT.equals(msg);
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }
}
