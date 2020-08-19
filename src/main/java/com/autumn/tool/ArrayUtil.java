package com.autumn.tool;

/**
 *  数组工具类
 * @author 秋雨
 */
public class ArrayUtil {
    /**
     * 判断数组是否为空
     * @param array 数组参数
     * @return
     */
    public static boolean isNotEmpty(Object[] array){
        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     * @param array 数组参数
     * @return
     */
    public static boolean isEmpty(Object[] array){
        return array==null||array.length==0;
    }
}
