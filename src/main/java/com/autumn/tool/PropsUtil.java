package com.autumn.tool;/**
 * Created by Administrator on 2018/8/14.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: PropsUtil
 * @description: 属性文件工具类
 * @Author 秋雨
 */
public class PropsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     * @param fileName fileName一定要在class下面及java根目录或者resource跟目录下
     * @return
     */
    public static Properties loadProps(String fileName){
        Properties props = new Properties();   //一定要初始化
        InputStream is = null;
        try {
            //将资源文件加载为流
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            //is = PropsUtil.class.getClassLoader().getResourceAsStream(fileName);
            props.load(is);
            if(is==null){
               throw new FileNotFoundException(fileName+"file is not Found");
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("load properties file filure",e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is !=null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure",e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符型属性（默认值为空字符串）
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props,String key){
        return getString(props,key,"");
    }

    /**
     * 获取字符型属性（可制定默认值）
     * @param props
     * @param key
     * @param defaultValue 当文件中无此key对应的则返回defaultValue
     * @return
     */
    public static String getString(Properties props,String key,String defaultValue){
        String value = defaultValue;
        if (props.containsKey(key)){
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性（默认值为0）
     * @param props
     * @param key
     * @return
     */
    public static int getInt(Properties props,String key){
        return getInt(props,key,0);
    }

    /**
     * 获取数值型属性（可指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties props,String key,int defaultValue){
        int value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取布尔型属性（默认值为false）
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }

    /**
     * 获取布尔型属性（可指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Properties props,String key,Boolean defaultValue){
        boolean value = defaultValue;
        if (props.containsKey(key)){
            value = CastUtil.castBoolean(props.getProperty(key).trim());
        }
        return value;
    }

    public static void main(String[] args) throws IOException {
        /*获取文件路径*/
        String path = PropsUtil.class.getClassLoader().getResource("dbconfig.properties").getPath();
        System.out.println(path);
        /*手动加载properties文件*/
        /*InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbconfig.properties");
        Properties properties = new Properties();
        properties.load(is);
        String val = getString(properties,"jdbc.driver");
        System.out.println(val);*/
        /*测试工具类加载properties文件*/
        Properties properties = loadProps("dbconfig.properties");
        String val = getString(properties,"jdbc.driver");
        System.out.println(val);
        /*测试日志*/
        //LOGGER.error("text");
    }
}
