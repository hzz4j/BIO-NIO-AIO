package edu.hzz.copyfile;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFile {
    private CopyFileRunner bufferedStreamCopy;
    private CopyFileRunner bufferedChannelCopy;
    private final int EOF = -1;
    private static final int TOTAL_TIMES = 5;

    public CopyFile(){

        this.bufferedStreamCopy = new CopyFileRunner() {
            @Override
            public void copyFile(File source, File target) {
                FileInputStream in=null;
                FileOutputStream out=null;
                try {
                    in = new FileInputStream(source);
                    out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length = 0;
                    while((length = in.read(buffer)) != EOF){
                        out.write(buffer,0,length);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }finally {
                    close(in);
                    close(out);
                }
            }

            @Override
            public String toString() {
                return "bufferedStreamCopy";
            }
        };
        this.bufferedChannelCopy = new CopyFileRunner() {
            @Override
            public void copyFile(File source, File target){
                FileChannel inChannel=null,outChannel=null;
                try {
                     inChannel = new FileInputStream(source).getChannel();
                     outChannel = new FileOutputStream(target).getChannel();

                     ByteBuffer buffer = ByteBuffer.allocate(1024);
                     int length=0;
                     while((length = inChannel.read(buffer))!=EOF){
                            buffer.flip();
                            outChannel.write(buffer);
                            buffer.clear();
                     }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }finally {
                    close(inChannel);
                    close(outChannel);
                }
            }

            @Override
            public String toString() {
                return "bufferedChannelCopy";
            }
        };
    }

    //  测试运行的时间
    public static void benMark(CopyFileRunner test,File source,File target){
        long elasped = 0L;
        for (int i = 0; i < TOTAL_TIMES; i++) {
            long startTime = System.currentTimeMillis();
            test.copyFile(source,target);
            elasped += System.currentTimeMillis()-startTime;
            target.delete();
        }
        System.out.printf("【%s】: %d\n",test,elasped/TOTAL_TIMES);
    }
    private static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //  1.16GB
        File source = new File("C:\\Users\\11930\\Videos\\李志 2014_15 i_O 跨年音樂會.mp4");
        File target = new File("C:\\Users\\11930\\Videos\\李志 2014_15 i_O 跨年音樂會COPY.mp4");

        CopyFile copyFile = new CopyFile();
        benMark(copyFile.bufferedStreamCopy,source,target);
        System.out.println("-------------------------------");
        benMark(copyFile.bufferedChannelCopy,source,target);
    }
}
/**
 【bufferedStreamCopy】: 10089
 -------------------------------
 【bufferedChannelCopy】: 9858
 */