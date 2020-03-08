package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @program: StreamUtil
 * @description: 流操作常用工具类
 * @author: Created by Autumn
 * @create: 2018-10-24 15:41
 */

public class StreamUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从输入流中获取字符串
     * @param is
     * @return
     */
    public static String getString(InputStream is){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get string failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 将输入流复制到输出流
     * @param inputStream 输入流
     * @param outputStream 输出流
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream){
        try {
            int length;
            byte[] buffer = new byte[4*1024];
            while((length = inputStream.read(buffer,0,buffer.length)) != -1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("copy stream failure",e);
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("close stream failure",e);
            }
        }
    }

}
