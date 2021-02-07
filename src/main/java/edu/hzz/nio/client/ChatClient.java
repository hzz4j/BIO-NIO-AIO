package edu.hzz.nio.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 9897;
    private static final int DEFAULT_CAPACITY = 1024;
    private static final String ENCODE_TYPE = "utf-8";
    private static final String QUIT = "quit";

    private Selector selector;
    private SocketChannel socketChannel;
    private ByteBuffer readerBuffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
    private Charset charset = Charset.forName(ENCODE_TYPE);



    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }

    private void start() {
        try {
            socketChannel = SocketChannel.open();
            //  设置为非阻塞
            socketChannel.configureBlocking(false);
            //  连接服务器
            socketChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
            //  开启selector
            selector = Selector.open();
            //  注册该channel
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while(selector.select()>0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey: selectionKeys){
                    handles(selectionKey);
                }
                selectionKeys.clear();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            close();
            System.out.println("成功关闭");
        }
    }

    private void handles(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel)selectionKey.channel();

        if(selectionKey.isConnectable()){   //  连接
            if(channel.isConnectionPending()){
                channel.finishConnect();
                System.out.println("成功连接到服务器");
                channel.register(selector,SelectionKey.OP_READ);
                //  处理用户的输入
                new Thread(new UserInputHandler(this)).start();
            }
        }else if(selectionKey.isReadable()){

            String msg = receive(channel);
            if(msg.isEmpty()){
                //  服务器异常
                System.out.println("服务器异常"+msg);
            }else{
                System.out.println(msg);
            }
        }
    }

    private String receive(SocketChannel channel) throws IOException {
        readerBuffer.clear();
        channel.read(readerBuffer);
        //  开启读模式
        readerBuffer.flip();
        return String.valueOf(charset.decode(readerBuffer));
    }

    public void send(String msg) throws IOException {
        writeBuffer.clear();
        writeBuffer.put(charset.encode(msg));
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
    }

    public boolean readyToQuit(String msg){
        return QUIT.equals(msg);
    }

    public void close(){
        close(this.selector);
        close(this.socketChannel);
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
