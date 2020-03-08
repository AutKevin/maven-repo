package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @program: CodecUtil
 * @description: 编码与解码操作工具类
 * @author: Created by Autumn
 * @create: 2018-10-24 15:49
 */
public class CodecUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将URL编码
     */
    public static String encodeURL(String source){
        String target;
        try {
            target = URLEncoder.encode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        return target;
    }

    /**
     * 将URL解码
     */
    public static String decodeURL(String source){
        String target;
        try {
            target = URLDecoder.decode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        return target;
    }
}
