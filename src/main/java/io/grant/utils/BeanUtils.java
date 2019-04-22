package io.grant.utils;

import com.xforceplus.apollo.utils.BeanMapperUtils;
import com.xforceplus.apollo.utils.code.FieldCopyUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体类操作
 */
public class BeanUtils {
    /**
     * 获取利用反射获取类里面的值和名称
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> toMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        for(Field field : clazz.getDeclaredFields()){
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 获取字段
     * @param cl
     * @return
     */
    public static Map<String, Field> getAllField(Class cl){
        Map<String, Field> map = new HashMap<>();
        for(Field field : cl.getDeclaredFields()){
            field.setAccessible(true);
            String fieldName = field.getName();
            map.put(fieldName, field);
        }
        return map;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     * @param obj
     * @return
     */
    public static Map<String, String> toStrMap(Object obj){
        return FieldCopyUtils.beanMap(obj);
    }

    /*************属性拷贝*********************/
    /**
     * 基于Dozer转换对象的类型.
     * @param source
     * @param dest
     * @param <T>
     * @return
     */
    public static <T> T map(Object source, Class<T> dest) {
        return BeanMapperUtils.map(source, dest);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     * @param source
     * @param dest
     * @param <T>
     * @return
     */
    public static <T> List<T> mapList(Collection source, Class<T> dest) {
       return BeanMapperUtils.mapList(source, dest);
    }

    /**
     *
     * 基于Dozer将对象A的值拷贝到对象B中.
     *
     * @param source
     * @param destObj
     */
    public static void copy(Object source, Object destObj) {
        BeanMapperUtils.copy(source, destObj);
    }

    /**
     * 对象拷贝，忽略类型不匹配字段
     * @param source
     * @param destObj
     */
    public static void copyIngore(Object source, Object destObj){
        FieldCopyUtils.copyFields(source, destObj);
    }
}
