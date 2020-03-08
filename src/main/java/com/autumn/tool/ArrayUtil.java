package com.autumn.tool;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @description: 数组工具类
 * @Author 秋雨
 */
public class ArrayUtil {
    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    public static boolean isNotEmpty(Object[] array){
        return !isEmpty(array);
    }

    /**
     * 判断数组是否非空
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array){
        return array==null||array.length==0;
    }
}
