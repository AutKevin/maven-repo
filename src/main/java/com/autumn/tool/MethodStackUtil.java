package com.autumn.tool;

import java.text.MessageFormat;

/**
 * MethodStackUtil
 * 方法栈获取工具类
 * @author: 秋雨
 * 2021-07-23 10:04
 **/
public class MethodStackUtil {
    /**
     * 打印全部方法栈
     * 包括
     * java.lang.Thread.getStackTrace() 1,589 <-
     * MethodStackUtil.getStackTrace() 20 <-
     * @return
     */
    public static String getStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if(stackTrace == null) {
            return "no stack...";
        }
        StringBuffer stackTraceSB = new StringBuffer();
        for(StackTraceElement stackTraceElement : stackTrace) {
            if(stackTraceSB.length() > 0) {
                stackTraceSB.append(" <- ");
                stackTraceSB.append(System.getProperty("line.separator"));
            }
            stackTraceSB.append(MessageFormat.format("{0}.{1}() {2}"
                    ,stackTraceElement.getClassName()
                    ,stackTraceElement.getMethodName()
                    ,stackTraceElement.getLineNumber()));
        }
        return stackTraceSB.toString();
    }

    /**
     * 打印方法栈 - 此方法的上层所有调用者
     * 不包括ava.lang.Thread.getStackTrace()和MethodStackUtil.getStackTrace()
     * @return
     */
    public static String getParentStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if(stackTrace == null) {
            return "no stack...";
        }
        StringBuffer stackTraceSB = new StringBuffer();
        String classname = Thread.currentThread().getStackTrace()[1].getClassName();  //静态方法获取类名
        for(StackTraceElement stackTraceElement : stackTrace) {
            if ("getStackTrace".contains(stackTraceElement.getMethodName()) || classname.contains(stackTraceElement.getClassName())){
                continue;
            }
            if(stackTraceSB.length() > 0) {
                stackTraceSB.append(" <- ");
                stackTraceSB.append(System.getProperty("line.separator"));
            }
            stackTraceSB.append(MessageFormat.format("{0}.{1}() {2}"
                    ,stackTraceElement.getClassName()
                    ,stackTraceElement.getMethodName()
                    ,stackTraceElement.getLineNumber()));
        }
        return stackTraceSB.toString();
    }

    /**
     * 打印方法栈 - 只打印指定包名或者类名的下的方法
     * @param packageName 包名 - 打印指定包名下的方法栈 ,可以写包名的一部分或者类名,例如: com.bus
     * @return
     */
    public static String getStackTraceByPackage(String packageName) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if(stackTrace == null) {
            return "no stack...";
        }
        StringBuffer stackTraceSB = new StringBuffer();
        for(StackTraceElement stackTraceElement : stackTrace) {
            if (!stackTraceElement.getClassName().contains(packageName)){  //如果类全名不含packageName则不打印
                continue;
            }
            if(stackTraceSB.length() > 0) {
                stackTraceSB.append(" <- ");
                stackTraceSB.append(System.getProperty("line.separator"));
            }
            stackTraceSB.append(MessageFormat.format("{0}.{1}() {2}"
                    ,stackTraceElement.getClassName()
                    ,stackTraceElement.getMethodName()
                    ,stackTraceElement.getLineNumber()));
        }
        return stackTraceSB.toString();
    }
}
