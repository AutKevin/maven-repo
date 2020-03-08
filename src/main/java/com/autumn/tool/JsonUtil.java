package org.smart4j.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @program: JsonUtil
 * @description: JSON工具类
 * @author: Created by Autumn
 * @create: 2018-10-24 15:55
 */

public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER =new ObjectMapper();

    /**
     * 将POJO转换为JSON
     */
    public static <T> String toJson(T obj){
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert POJO to JSON failure",e);
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        return json;
    }

    /**
     * 将JSON转为POJO
     */
    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(json,type);
        } catch (IOException e) {
            LOGGER.error("convert JSON to POJO failure",e);
            throw new RuntimeException(e);
            //e.printStackTrace();
        }
        return pojo;

    }
}
