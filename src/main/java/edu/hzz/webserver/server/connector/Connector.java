package edu.hzz.webserver.server.connector;

import edu.hzz.webserver.server.processor.ServletProcessor;
import edu.hzz.webserver.server.processor.StaticProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.*;
import java.util.Set;

public class Connector implements Runnable{
    private static final int DEFAULT_PORT = 9898;
    private int port;

    public Connector(){this(DEFAULT_PORT);}
    public Connector(int port){this.port = port;}

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public void start(){
        new Thread(this).start();
    }


    @Override
    public void run() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            selector = selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.printf("服务器启动，访问: http://localhost:%s \n",port);

            while(true){

                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey key: selectionKeys){
                    try{
                        handles(key);
                    }catch (IOException exception){
                        System.out.println(key+"异常");
                    }
                }
                selectionKeys.clear();
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }finally {
            close(serverSocketChannel);
        }
    }

    private void handles(SelectionKey key) throws IOException {

        if(key.isAcceptable()){
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = channel.accept();
            System.out.println("接受到新的请求");
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ);
        }else if(key.isReadable()){
            SocketChannel channel = (SocketChannel)key.channel();
            //  取消注册轮询
            key.cancel();
            //  回复成阻塞
            channel.configureBlocking(true);

            Socket socket = channel.socket();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            Request request = new Request(inputStream);
            request.parse();
            Response response = new Response(outputStream);
            response.setRequest(request);

            if(request.getRequestURI().contains("/servlet/")){
                //  处理动态资源
                ServletProcessor processor = new ServletProcessor();
                processor.process(request,response);
            }else{
                //  处理静态资源
                StaticProcessor processor = new StaticProcessor();
                processor.process(request,response);
            }
            close(socket);
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
