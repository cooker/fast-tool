package io.grant.utils;

import com.xforceplus.apollo.utils.JacksonUtil;

import java.util.List;

/**
 * Json 工具类
 */
public class JsonUtils {
    final static JacksonUtil ref = JacksonUtil.getInstance();

    public <T> T fromJson(String json, Class<T> clazz) {
        return ref.fromJson(json, clazz);
    }

    public  <T> List<T>  fromJson2List(String json, Class<T> cl){
        return ref.fromJsonToList(json, cl);
    }

    public String toJson(Object object) {
        return ref.toJson(object);
    }

}
