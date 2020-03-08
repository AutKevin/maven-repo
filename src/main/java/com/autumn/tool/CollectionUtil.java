package org.smart4j.framework.util;

import java.util.Collection;
import java.util.Map;

/**
 * @program: chapter2->CollectionUtil
 * @description: 集合工具类
 * @author: qiuyu
 * @create: 2018-08-15 21:19
 **/
public class CollectionUtil {
    /**
     * 判断collection是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection){
        //return CollectionUtils.isEmpty(collection);
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断Collection是否非空
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * 判断map是否为空
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?,?> map){
        //return MapUtils.isEmpty(map);
        return map == null || map.isEmpty();
    }

    /**
     * 判断map是否非
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }
}
