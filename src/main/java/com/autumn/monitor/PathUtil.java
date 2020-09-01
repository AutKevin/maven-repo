package com.autumn.monitor;

import java.io.File;

/**
 * PathUtil
 * 文件夹设置为环境变量Path工具类
 * @author: 秋雨
 * 2020-08-28 15:16
 **/
public class PathUtil {

    /**
     * 判断是否为linux系统
     * @return
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 判断是否为Windows
     * @return
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * 获取系统类型
     * @return linux/windows/other system
     */
    public String getSystem() {
        if (isLinux()) {
            return "linux";
        } else if (isWindows()) {
            return "windows";
        } else {
            return "other system";
        }
    }

    /**
     * 设置项目下的某一个文件夹为系统环境path变量
     * 注意:
     * 1.要部署后运行
     * 2.不能打成jar包运行
     * @param pathFolder
     */
    public static void setSystemPathVariable(String pathFolder) throws Exception {
        if (pathFolder==null || pathFolder.trim().isEmpty()){
            pathFolder = "conf";
        }
        try {
            String classPath = System.getProperty("user.dir") + File.separator + pathFolder;
            String path = System.getProperty("java.library.path");
            if (isWindows()) {
                path += ";" + classPath;
            } else {
                path += ":" + classPath;
            }
            System.setProperty("java.library.path", path);
        } catch (Exception e) {
            throw new Exception("设置环境变量Path失败",e);
        }
    }

    public static void main(String[] args) throws Exception {

        String classPath = System.getProperty("user.dir") + File.separator + "conf";
        System.out.println(classPath);
        String path = System.getProperty("java.library.path");
        System.out.println(path);
        setSystemPathVariable("");
        String path2 = System.getProperty("java.library.path");
        System.out.println(path2);
    }
}
