package com.autumn.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 名   称：MD5Util.java
 * 描   述：
 * 作   者：李波
 * 时   间：Dec 9, 2016 10:10:20 AM
 * --------------------------------------------------
 * 修改历史
 * 序号    日期    修改人     修改原因 
 * 1
 * **************************************************
 */
public class MD5Util {

    /**
     * md5加密
     * @param s
     * @return 小写MD5加密字符
     */
    public final static String md5(String s) throws NoSuchAlgorithmException {
        /*1.获得加密后的字节数组*/
        byte[] bytes = s.getBytes();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        md5Digest.update(bytes);
        byte[] digest = md5Digest.digest();  //加密后的字节数组
        /*2.加密后的字节数组转换为Hex*/
        String hexString = digest2HexString(digest);
        return hexString;
    }

    /**
     * 把字节数组转成16进制字符
     * @param digest 字节数组
     * @return 16进制字符(每个字节转为2位16进制)
     */
    private static String digest2HexString(byte[] digest)
    {
        String hexString="";
        int low, high ;

        for(int i=0; i < digest.length; i++)
        {
            low =   digest[i] & 0x0f;  //获取一个字节的低4位字符
            high  =  (digest[i] & 0xf0)>>4 ;  //获取一个字节的高4位字符,因为>>位移运算符优先级大于&与运算,所以一定要带括号
            hexString += Integer.toHexString(high);  //高位字符添加到结果
            hexString += Integer.toHexString(low);  //低位字符添加到结果
        }
        return hexString ;
    }

    /**
     * MD5加密
     * @param s
     * @return 大写MD5加密字符
     */
	public final static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            // 1.将字符串转换为字节数组
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 2.获得密文(字节数组)
            byte[] md = mdInst.digest();
            // 3.把密文(字节数组)转换成十六进制的字符串形式
            int j = md.length;
            //因为每个字节可以用两个16进制字符表示,所以字符串长度时字节长度的二倍
            char[] str = new char[j * 2];
            int k = 0;  //最终字符串的下标
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];  //获取第一个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  //将字节前4位转换为字符,先向右移4位,然后在和f做并运算计算出高四位的值.
                str[k++] = hexDigits[byte0 & 0xf];  //将字节后4位转换为字符
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(MD5("1"));
        System.out.println(md5("1"));

        byte[] bytes = "1".getBytes();
        MessageDigest md5Digest = MessageDigest.getInstance("md5");
        md5Digest.update(bytes);
        byte[] digest = md5Digest.digest();
        String result = "";
        for (int i=0;i<digest.length;i++){
            byte b = digest[i];
            int high = (b & 0xf0) >> 4;
            int low = (b & 0x0f);
            String hexHigh = Integer.toHexString(high);
            String hexlow = Integer.toHexString(low);
            result+= hexHigh;
            result+= hexlow;
        }
        System.out.println(result);

    }
}