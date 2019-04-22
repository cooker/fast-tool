package io.grant.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Closeable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * 描述:
 *
 * @author grant
 * @create 2019-04-19 6:12 PM
 */
public class CommonTools {
    /**
     * 对象转string
     **/
    public static String toString(Object obj){
        if(null != obj){
            return obj.toString();
        }else{
            return "";
        }
    }

    /**
     * 判断是否为空
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || "null".equalsIgnoreCase(cs.toString()) || cs.length() == 0;
    }

    /**
     * 去除空格，判断是否为空
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断是否有空
     * @param css
     * @return
     */
    public static boolean isAnyEmpty(CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        } else {
            CharSequence[] var1 = css;
            int var2 = css.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CharSequence cs = var1[var3];
                if (isEmpty(cs)) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * 首尾去空格
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 首尾去空格
     * @param str
     * @return
     */
    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    /**
     * 首尾去空格，null 返回 ""
     * @param str
     * @return
     */
    public static String trimToEmpty(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 判断是否为空
     * 支持类型：
     * String、Collection、Map、Object[]、Iterator、Iterator
     * @return
     */
    public static boolean isEmptyX(Object object) {
        if (null == object) return true;
        if (object instanceof String){
            return ((String) object).length() == 0;
        }else {
            if (object instanceof Collection) {
                return ((Collection) object).isEmpty();
            } else if (object instanceof Map) {
                return ((Map) object).isEmpty();
            } else if (object instanceof Object[]) {
                return ((Object[]) object).length == 0;
            } else if (object instanceof Iterator) {
                return ((Iterator) object).hasNext() == false;
            } else if (object instanceof Enumeration) {
                return ((Enumeration) object).hasMoreElements() == false;
            } else {
                try {
                    return Array.getLength(object) == 0;
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
                }
            }
        }
    }

    /**
     * IO 关闭
     * @param close
     */
    public static void close(Closeable... close){
        if (!ArrayUtils.isEmpty(close)){
            for (Closeable c : close){
                if (c != null){
                    try {
                        c.close();
                    }catch (Exception e){
                        //NOP
                    }
                }
            }
        }
    }

}
