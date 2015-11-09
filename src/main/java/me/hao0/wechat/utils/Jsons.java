package me.hao0.wechat.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * Json Util based on Jackson
 * Author: haolin
 * On: 11/8/14
 */
public class Jsons {

    private static Logger logger = LoggerFactory.getLogger(Jsons.class);

    /**
     * 忽略对象中值为NULL或""的属性
     */
    public static final Jsons EXCLUDE_EMPTY = new Jsons(JsonInclude.Include.NON_EMPTY);

    /**
     * 忽略对象中值为默认值的属性
     */
    public static final Jsons EXCLUDE_DEFAULT = new Jsons(JsonInclude.Include.NON_DEFAULT);

    /**
     * 默认不排除任何属性
     */
    public static final Jsons DEFAULT = new Jsons();

    private ObjectMapper mapper;

    private Jsons() {
        mapper = new ObjectMapper();
        // ignore attributes exists in json string, but not in java object when deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private Jsons(JsonInclude.Include include) {
        mapper = new ObjectMapper();
        // set serialization feature
        mapper.setSerializationInclusion(include);
        // ignore attributes exists in json string, but not in java object when deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * convert an object(POJO, Collection, ...) to json string
     * @param target target object
     * @return json string
     */
    public String toJson(Object target) {
        try {
            return mapper.writeValueAsString(target);
        } catch (IOException e) {
            logger.error("write to json string error:" + target, e);
            return null;
        }
    }

    /**
     * deserialize a json to target class object
     * @param json json string
     * @param target target class
     * @param <T>
     * @return target object
     */
    public <T> T fromJson(String json, Class<T> target) {
        if (isNullOrEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            logger.warn("parse json string error:" + json, e);
            return null;
        }
    }

    private boolean isNullOrEmpty(String json) {
        return json == null || "".equals(json);
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     *
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * read a json to JsonNode Tree
     * @param json source json string
     * @return JsonNode Tree
     * @throws java.io.IOException
     */
    public JsonNode treeFromJson(String json) throws IOException {
        return mapper.readTree(json);
    }

    /**
     * convert a JsonNode to target class object
     * @param node source node
     * @param target target class
     * @param <T>
     * @return target class object
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    public <T> T treeToValue(JsonNode node, Class<T> target) throws JsonProcessingException {
        return mapper.treeToValue(node, target);
    }

    /**
     * construct collection type
     * @param collectionClass collection class, such as ArrayList, HashMap, ...
     * @param elementClasses element class
     *     ArrayList<T>:
     *                  createCollectionType(ArrayList.class, T.class)
     *     HashMap<String, T>:
     *                  createCollectionType(HashMap.class, String.class, T.class)
     * @return JavaType
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * update a target object's attributes from json
     * @param json source json string
     * @param target target object
     * @param <T>
     * @return updated target object
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String json, T target) {
        try {
            return (T) mapper.readerForUpdating(target).readValue(json);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + json + " to object:" + target + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + json + " to object:" + target + " error.", e);
        }
        return null;
    }

    /**
     * output JSONP style string
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * enable enumable, make enum attribute read or write as string
     */
    public void enumable() {
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    /**
     * return a common json mapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}