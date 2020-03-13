package com.autumn.tool;

/**
 * StringUtil
 * @description: 字符串工具类
 * @Author 秋雨
 **/
public class StringUtil {

    /**
     * 判断是否为空字符串最优代码
     * @param str
     * @return 如果为空，则返回true
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否非空
     * @param str 如果不为空，则返回true
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    /**
     * 功能：将输入字符串的首字母改成大写
     * @param str
     * @return
     */
    public static String upFirstChar(String str) {
        if (str!=null&&str.length()>0) {
            char[] ch = str.toCharArray();
            if(ch[0] >= 'a' && ch[0] <= 'z'){
                ch[0] = (char)(ch[0] - 32);
            }
            return new String(ch);
        }
        return null;
    }
}
