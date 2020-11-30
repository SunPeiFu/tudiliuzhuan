package com.tudiliuzhuan.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * json工具类
 *
 * @author Zengzhong on 2019/9/19
 */
@Slf4j
public final class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    static {
        OBJECT_MAPPER = new ObjectMapper();
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN)));

        javaTimeModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String date = parser.getText();
                return LocalDateTimeUtils.str2LocalDate(date);
            }
        });

        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                String date = parser.getText();
                return LocalDateTimeUtils.str2LocalDateTime(date);
            }
        });
        OBJECT_MAPPER.registerModule(javaTimeModule);
    }


    public static JSONObject parseObject(String text) {
        try {
            return JSON.parseObject(text);
        } catch (Exception e) {
            log.info("Json解析失败,字符串:{},失败说明:{}", text, e.getMessage());
        }
        return null;
    }

    public static JSONObject parseObject(String text, Feature... features) {
        return JSON.parseObject(text, features);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        try {
            return JSON.parseObject(text, clazz, new Feature[0]);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> type, Feature... features) {
        try {
            return JSON.parseObject(text, type, features);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    //---------------------------------------------------------------------
    //                              jackson
    //---------------------------------------------------------------------

    /**
     * json转List
     *
     * @param json   json字符串
     * @param aClass 传List里的类型
     * @author liuwenjie
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> aClass) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, aClass);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            log.error("fromJsonArray error: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * jackson 对象转json
     *
     * @param obj 要转换的对象
     * @author liuwenjie
     */
    public static String object2Json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json异常：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * jackson 对象转json
     *
     * @param obj                  要转换的对象
     * @param serializationFeature 序列化规则
     * @author liuwenjie
     */
    public static String object2Json(Object obj, SerializationFeature serializationFeature) {
        try {
            ObjectMapper mapper = OBJECT_MAPPER.copy();
            mapper.enable(serializationFeature);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json异常：{}", e.getMessage(), e);
            throw new RuntimeException("objectToJson error " + e.getMessage());
        }
    }

    /**
     * jackson 对象转json,忽略为空的
     *
     * @param obj 要转换的对象
     * @author liuwenjie
     */
    public static String object2JsonNonNull(Object obj) {
        try {
            ObjectMapper mapper = OBJECT_MAPPER.copy();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json异常：{}", e.getMessage(), e);
            throw new RuntimeException("objectToJson error " + e.getMessage());
        }
    }

    /**
     * jackson 对象转json
     *
     * @param obj       要转换的对象
     * @param allExcept 序列化时要排除的字段
     * @author liuwenjie
     */
    public static String object2Json(Object obj, String... allExcept) {
        try {
            ObjectMapper mapper = OBJECT_MAPPER.copy();
            SimpleBeanPropertyFilter propertyFilter = SimpleBeanPropertyFilter.serializeAllExcept(allExcept);
            mapper.setFilterProvider(new SimpleFilterProvider().addFilter("fieldFilter", propertyFilter))
                    .addMixIn(obj.getClass(), FieldFilterMixIn.class);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转json异常：{}", e.getMessage(), e);
            throw new RuntimeException("objectToJson error " + e.getMessage());
        }
    }

    /**
     * 定义一个类
     */
    @JsonFilter("fieldFilter")
    interface FieldFilterMixIn {
    }

    /**
     * jackson json转对象
     *
     * @param json   要转换的字符串
     * @param aClass 要转换的对象类型
     * @author liuwenjie
     */
    public static <T> T json2Object(String json, Class<T> aClass) {
        try {
            return OBJECT_MAPPER.readValue(json, aClass);
        } catch (IOException e) {
            log.error("json转对象异常：{}", e.getMessage(), e);
            throw new RuntimeException("json转对象异常：" + e.getMessage());
        }
    }

    /**
     * jackson json转对象
     *
     * @param json          要转换的字符串
     * @param typeReference 要转换的对象类型
     * @author liuwenjie
     */
    public static <T> T json2Object(String json, com.fasterxml.jackson.core.type.TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("jsonToObject error: {}", e.getMessage(), e);
            throw new RuntimeException("jsonToObject error " + e.getMessage());
        }
    }
}