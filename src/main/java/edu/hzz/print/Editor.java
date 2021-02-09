package edu.hzz.print;

import java.io.*;
import java.util.Scanner;

public class Editor {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("请输入文件名: ");
        String fileName = scanner.nextLine();

        /**
         * 当创建PW时第一个参数为一个流时，
         * 那么久可以再传入一个boolean值类型的参数，
         * 若该值为true，那么当前PW久具有自动行刷新的功能，
         * 即：每当使用println方法写出一行字符串后就会自动调用flush
         * 注：使用自动行刷新可以提高写出数据的即时性，
         * 但是由于会提高写出次数，必然会导致写效率降低。
         */
         PrintWriter printWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter( new FileOutputStream(fileName),"UTF-8")
                ),true);

        while(scanner.hasNext()){
           String content = scanner.nextLine();
           if("quit".equals(content)){
               break;
           }else{
//               printWriter.println(content);
               printWriter.write(content);
               printWriter.flush();
           }
        }
        printWriter.close();
        System.out.println("退出");
    }
}
