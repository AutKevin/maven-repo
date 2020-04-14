package com.autumn.tool;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @program: FileUtil
 * @description: 文件操作工具类
 * @Author 秋雨
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取真实文件名(自动去掉文件路径)
     *
     * @param fileName
     * @return
     */
    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    /**
     * 创建文件
     * @param filePath 文件全路径
     * @return
     */
    public static File createFile(String filePath) {
        File file;
        file = new File(filePath);   //根据路径创建文件
        try {
            File parentDir = file.getParentFile();   //获取文件父目录
            if (!parentDir.exists()) {  //判断上层目录是否存在
                FileUtils.forceMkdir(parentDir);   //创建父级目录
            }
        } catch (IOException e) {
            LOGGER.error("create file failure",e);
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        return file;
    }

    /**
     * 下载url的文件到指定文件路径里面,如果文件父文件夹不存在则自动创建
     * url 下载的http地址
     * path 文件存储地址+文件名
     * return 如果文件大小大于2k则返回true
     */
    public static boolean downloadCreateDir(String url,String path){
        HttpURLConnection connection;
        connection = null;
        InputStream in = null;
        FileOutputStream o=null;
        try{
            URL httpUrl=new URL(url);
            connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestMethod("GET");

            byte[] data=new byte[1024];
            File f=new File(path);
            File parentDir = f.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if(connection.getResponseCode() == 200){
                in = connection.getInputStream();
                o=new FileOutputStream(path);
                int n=0;
                while((n=in.read(data))>0){
                    o.write(data, 0, n);
                    o.flush();
                }
            }
            if(f.length()>2048){  //代表文件大小
                return true;  //如果文件大于2k则返回true
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
            try{o.close();}catch(Exception ex){}
            try{connection.disconnect();}catch(Exception ex){}
        }
        return false;
    }

    /**
     * 把GBK文件转为UTF-8
     * 两个参数值可以为同一个路径
     * @param srcFileName 源文件
     * @param destFileName 目标文件 两个文件可以为一个
     * @param fromCdoe 源文件编码(默认GBK)
     * @param toCode 目标文件编码(默认utf-8)
     * @throws IOException
     */
    private static void transferGBKToUtf8(String srcFileName, String destFileName,String fromCdoe,String toCode) throws IOException {
        String line_separator = System.getProperty("line.separator");
        FileInputStream fis = new FileInputStream(srcFileName);
        StringBuffer content = new StringBuffer();
        DataInputStream in = new DataInputStream(fis);
        if (StringUtil.isEmpty(fromCdoe)){
            fromCdoe = "GBK";
        }
        BufferedReader d = new BufferedReader(new InputStreamReader(in, fromCdoe));  //源文件的编码方式
        String line = null;
        while ((line = d.readLine()) != null) {
            content.append(line + line_separator);
        }
        d.close();
        in.close();
        fis.close();

        if (StringUtil.isEmpty(toCode)){
            toCode = "utf-8";
        }
        Writer ow = new OutputStreamWriter(new FileOutputStream(destFileName), toCode);  //需要转换的编码方式
        ow.write(content.toString());
        ow.close();
    }

    /**
     * byte数组转换成16进制字符串
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            /*&与操作符,只有两个位同时为1,才能得到1,
            0x代表16进制数,0xff表示的数二进制1111 1111 占一个字节.和其进行&操作的数,最低8位*/
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件流判断图片类型
     * @param fis
     * @return jpg/png/gif/bmp
     */
    public static boolean getPicTypeByStream(InputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return true; //jpg
            } else if (type.contains("89504E47")) {
                return true; //png
            } else if (type.contains("47494638")) {
                return true; //gif
            } else if (type.contains("424D")) {
                return true; //bmp
            }else{
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(bytesToHexString(new byte[]{126}));
    }
}
