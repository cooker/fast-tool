package io.grant.utils.makers;

import io.grant.core.IMaker;
import io.grant.utils.BeanUtils;
import io.grant.utils.Preconditions;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 对象赋值
 */
public class BeanMaker<M extends Object> implements IMaker {
    private M obj;
    private boolean isMap = false;
    private boolean isObj = false;
    private Map<String, Field> fieldMap = null;

    private BeanMaker(Object target){
        this.obj = (M)target;
    }

    public static BeanMaker builder(Object target){
        return new BeanMaker(target);
    }

    @Override
    public IMaker make(String key, Object val) {
        if (isMap){
            ((Map)obj).put(key, val);
        }else if (isObj){
            Field field = fieldMap.get(key);
            Preconditions.checkNotNull(field, "找不到字段" + key);
            field.setAccessible(true);
            try {
                field.set(obj, val);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return this;
    }

    private void check(){
        if (obj instanceof Map){
            isMap = true;
        }else if (obj instanceof Object){
            isObj = true;
            fieldMap = BeanUtils.getAllField(obj.getClass());
        }else{
            Preconditions.checkState(false, "无法解析的对象");
        }
    }
}
