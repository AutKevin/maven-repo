package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类
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
