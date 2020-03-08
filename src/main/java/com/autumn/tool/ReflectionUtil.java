package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射工具
 * 实现bean容器
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     * @param cls 已经加载的类
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance = null;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     * @param obj 调用方法的实例对象
     * @param method 方法
     * @param args 方法参数
     * @return
     */
    public static Object invokeMethod(Object obj, Method method,Object... args){
        Object result = null;
        try {
            boolean originalAccess = method.isAccessible();   //先获取原本的权限
            method.setAccessible(true);   //设为可获取
            result = method.invoke(obj,args);
            method.setAccessible(originalAccess);  //恢复到原本的权限
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error("invoke method failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     * @param obj 实例对象
     * @param field 字段
     * @param value 字段值
     */
    public static void setField(Object obj, Field field,Object value){
        try {
            boolean originalAccess = field.isAccessible();   //先获取原本的权限
            field.setAccessible(true);
            field.set(obj,value);
            field.setAccessible(originalAccess);  //恢复到原本的权限
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }
    }
}
