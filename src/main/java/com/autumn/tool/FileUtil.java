package org.smart4j.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @program: FileUtil
 * @description: 文件操作工具类
 * @author: Created by Autumn
 * @create: 2018-12-19 13:03
 */
public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取真实文件名(自动去掉文件路径)
     *
     * @param fileName
     * @return
     */
    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    public static File createFile(String filePath) {
        File file;
        file = new File(filePath);   //根据路径创建文件
        try {
            File parentDir = file.getParentFile();   //获取文件父目录
            if (!parentDir.exists()) {  //判断上层目录是否存在
                FileUtils.forceMkdir(parentDir);   //创建父级目录
            }
        } catch (IOException e) {
            LOGGER.error("create file failure",e);
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        return file;
    }
}
