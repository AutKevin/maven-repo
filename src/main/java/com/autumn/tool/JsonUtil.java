package com.autumn.tool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON工具类
 * @author 秋雨
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

    /**
     * List转换为Json字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJsonS(List<T> obj){
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert POJO to JSON failure",e);
            throw new RuntimeException(e);
        }
        return json;
    }

    /**
     * Json字符转换为List
     * 实体需要使用@JsonIgnoreProperties(ignoreUnknown = true)注解
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonS(String json, final Class<T> type){
        List<T> list = null;
        try {
            CollectionType listType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class,type);
            list = OBJECT_MAPPER.readValue(json,listType);
        } catch (IOException e) {
            LOGGER.error("convert JSON to POJO failure",e);
            throw new RuntimeException(e);
        }
        return list;
    }

}
