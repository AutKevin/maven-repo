package com.autumn.tool;

import java.io.*;
import java.util.Map;


/**
 * @program: HttpUtil
 * @description: http请求工具类
 * @Author 秋雨
 */
public class HttpUtil {

    /**
     * http请求- 返回字符串(可带cookie)
     * @param urlString 请求url
     * @param method get/post
     * @param params 参数
     * @param requestProperty request请求头参数 cookie写在这里
     * @param transencode 传输编码
     * @param decode 解析编码
     * @return 返回string类型字符串
     * @throws IOException
     */
    public static String sendUrlByCookie(String urlString, String method, String params, Map<String,String> requestProperty, String transencode, String decode)
            throws IOException {
        BufferedReader in = null;
        java.net.HttpURLConnection conn = null;
        String msg = "";// 保存调用http服务后的响应信息
        try {
            java.net.URL url = new java.net.URL(urlString);
            conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toUpperCase());// 请求的方法get,post,put,delete
            conn.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒
            conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
            conn.setDoOutput(true);// 使用 URL 连接进行输出，则将 DoOutput标志设置为 true

            //从google浏览器请求地址中的cookie里面找到这个
            conn.setRequestProperty("accept".toLowerCase(), "*/*");
            conn.setRequestProperty("connection".toLowerCase(), "keep-alive");
            conn.setRequestProperty("User-Agent".toLowerCase(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            //conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            //conn.setRequestProperty("Content-Length",String.valueOf(params == null ? "" : params.length()));
            //conn.setRequestProperty("Cookie","mstuid=1565660612968_2360; XM_agreement_sure=1; XM_agreement=0; xm_user_www_num=0;");
            //conn.setRequestProperty("Content-Encoding","gzip");
            //conn.setRequestProperty("Referer", "http://www.mi.com/index.jsp");
            if (!(requestProperty == null || requestProperty.isEmpty())){
                for (Map.Entry<String,String> entry:requestProperty.entrySet()){
                    conn.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }

            if (!(params == null || params.trim().length() == 0)) {
                OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
                outStream.write(params.getBytes());
                outStream.close();// 关闭流
            }

            if (transencode == null || transencode.trim().length() == 0){
                transencode = "iso-8859-1";
            }
            if (decode == null || decode.trim().length() == 0){
                decode = "utf-8";
            }

            if (conn.getResponseCode() == 200) {
                // HTTP服务端返回的编码是UTF-8,故必须设置为UTF-8,保持编码统一,否则会出现中文乱码
                in = new BufferedReader(new InputStreamReader(
                        (InputStream) conn.getInputStream(), transencode));
                String temp = "";
                while ((temp = in.readLine())!=null) {
                    msg += new String(temp.getBytes(transencode),decode);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
        return msg;
    }


    /**
     * http请求- 返回字符串(可带cookie)
     * @param urlString 请求url
     * @param method get/post
     * @param params 参数
     * @param requestProperty request请求头参数 cookie写在这里
     * @param filePath 下载文件全路径
     * @return 返回string类型字符串
     * @throws IOException
     */
    public static String sendUrlToDownFileByCookie(String urlString, String method, String params, Map<String,String> requestProperty, String filePath)
            throws IOException {
        java.net.HttpURLConnection conn = null;
        InputStream in = null;
        String result = "";
        try {
            java.net.URL url = new java.net.URL(urlString);
            conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method.toUpperCase());// 请求的方法get,post,put,delete
            conn.setConnectTimeout(5 * 1000);// 设置连接超时时间为5秒
            conn.setReadTimeout(20 * 1000);// 设置读取超时时间为20秒
            conn.setDoOutput(true);// 使用 URL 连接进行输出，则将 DoOutput标志设置为 true

            //从google浏览器请求地址中的cookie里面找到这个
            conn.setRequestProperty("accept".toLowerCase(), "*/*");
            conn.setRequestProperty("connection".toLowerCase(), "keep-alive");
            conn.setRequestProperty("User-Agent".toLowerCase(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            //conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            //conn.setRequestProperty("Content-Length",String.valueOf(params == null ? "" : params.length()));
            //conn.setRequestProperty("Cookie","mstuid=1565660612968_2360; XM_agreement_sure=1; XM_agreement=0; xm_user_www_num=0;");
            //conn.setRequestProperty("Content-Encoding","gzip");
            //conn.setRequestProperty("Referer", "http://www.mi.com/index.jsp");
            if (!(requestProperty == null || requestProperty.isEmpty())){
                for (Map.Entry<String,String> entry:requestProperty.entrySet()){
                    conn.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }

            if (!(params == null || params.trim().length() == 0)) {
                OutputStream outStream = conn.getOutputStream();// 返回写入到此连接的输出流
                outStream.write(params.getBytes());
                outStream.close();// 关闭流
            }
            int responseCode = conn.getResponseCode();
            result += responseCode;
            if ( responseCode == 200) {
                in = conn.getInputStream();
                File file = new File(filePath);
                File parentdir = file.getParentFile();
                if (!parentdir.exists()) {
                    parentdir.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);
                byte[] bytes = new byte[1024 * 20];
                int len = 0;
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
        return result;
    }
}
