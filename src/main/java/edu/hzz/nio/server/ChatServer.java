package edu.hzz.nio.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

public class ChatServer {
    private static final int DEFAULT_PORT = 9898;
    private static final int DEFAULT_CAPACITY = 1024;
    private static final String ENCODE_TYPE = "utf-8";
    private static final String QUIT = "quit";

    private int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(DEFAULT_CAPACITY);
    private Charset charset = Charset.forName(ENCODE_TYPE);


    public ChatServer(){
        this(DEFAULT_PORT);
    }

    public ChatServer(int port){
        this.port = port;
    }


    private void start() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            //  变成非阻塞
            serverSocketChannel.configureBlocking(false);
            //  绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.printf("启动服务器，监听端口【%d】... ...\n",port);
            //  获取到selector
            selector = Selector.open();
            //  channel注册到selecor 监听accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            //  轮询检测“准备就绪的事件”
            while(true){
                //  阻塞检测
//                int selector.select();
                System.out.println("检测到准备就绪的事件 "+selector.select()+"已注册的事件"+selector.keys().size());
                //  获取到所有的准备selectionKey
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println("selectionKeys: "+selectionKeys.size());
                for(SelectionKey selectionKey: selectionKeys){
                    System.out.println("执行事件");
                    //  处理触发的事件
                    handles(selectionKey);
                }
                //  清除已经处理过的所有准备的事件
                System.out.println("清空");
                selectionKeys.clear();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            close(selector);
            close(serverSocketChannel);
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
    //  读取数据
    private String receive(SocketChannel socketChannel){
        try{
            readBuffer.clear();
            socketChannel.read(readBuffer);
            //  切换到读模式
            readBuffer.flip();
            //  解码
            return String.valueOf(charset.decode(readBuffer));
        }catch (IOException e){
            System.out.println("客户端异常");
            return null;
        }

    }

    private String getClientName(Socket socket){
        return String.format("客户端【%d】",socket.getPort());
    }

    private boolean readyToQuit(String msg){
        return QUIT.equals(msg);
//        return "special".equals(msg);
    }

    private void handles(SelectionKey selectionKey) throws IOException{

        if(selectionKey.isAcceptable()){    //  服务端接收客户端连接事件

            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
            //  接收连接
            SocketChannel socketChannel = serverSocketChannel.accept();
            if(socketChannel!=null){
                //  设置为非阻塞
                socketChannel.configureBlocking(false);
                //  注册该客户端的channel
                socketChannel.register(selector,SelectionKey.OP_READ);
                System.out.printf("%s连接成功\n",getClientName(socketChannel.socket()));
            }
        }else if(selectionKey.isReadable()){    //  客户端可读事件
            //  获取出事件关联的channel
            SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
            String msg = receive(socketChannel);
            System.out.printf("%s: %s\n",getClientName(socketChannel.socket()),msg);
            if(msg == null || (msg != null) && msg.isEmpty()){  //  客户端异常或者已关闭
                //  取消该注册key
                selectionKey.cancel();
                System.out.println("empty wakeup");
                selector.wakeup();
            }else{
                //  群发
                forwardMsg(socketChannel,getClientName(socketChannel.socket())+msg);
                //  检测是否退出
                if(readyToQuit(msg)){
                    selectionKey.cancel();
                    System.out.println("quit wake up");
                    selector.wakeup();
                }
            }

        }

    }

    private void forwardMsg(SocketChannel socketChannel, String msg) throws IOException {
        //  所有注册的selectionKey
        Set<SelectionKey> keys = selector.keys();
        for(SelectionKey key: keys){
            Channel channel = key.channel();
            if(channel instanceof ServerSocketChannel){ //  过滤掉ServerSocketChannel
                continue;
            }

            if(key.isValid() && !socketChannel.equals(channel)){    //  发送信息
                writeBuffer.clear();
                writeBuffer.put(charset.encode(msg+"\n"));
                //  开启读模式
                writeBuffer.flip();
                ((SocketChannel)channel).write(writeBuffer);
            }
        }
    }

    public static void main(String[] args) {

        ChatServer server = new ChatServer(9897);
        server.start();
    }
}
