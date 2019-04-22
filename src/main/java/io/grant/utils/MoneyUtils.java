package io.grant.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 货币操作工具类
 */
public class MoneyUtils {
    public static final int DEF_RADIX = 2;

    public static String getStrBig(String str){
        BigDecimal bd = new BigDecimal(str);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        bd = bd.setScale(DEF_RADIX, BigDecimal.ROUND_HALF_UP);
        //转化为字符串输出
        return bd.toString();
    }

    /**
     * 金额计算 +
     */
    public static BigDecimal add(BigDecimal a1, BigDecimal b2, int scale){
        return a1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 金额计算 -
     */
    public static BigDecimal subtract(BigDecimal a1, BigDecimal b2, int scale){
        return a1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 金额计算 *
     */
    public static BigDecimal multiply(BigDecimal a1, BigDecimal b2, int scale){
        return a1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 金额计算 /
     */
    public static BigDecimal divide(BigDecimal a1, BigDecimal b2, int scale){
        return a1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal toBig(String str){
        return new BigDecimal(str);
    }

    /**
     * 判断金额是否小于0
     * @param str
     * @return
     */
    public static boolean compareSmallZero(String str){
        boolean bool = false;
        BigDecimal bd = new BigDecimal(str);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        if(bd.compareTo(new BigDecimal(BigInteger.ZERO)) == -1){
            bool = true;
        }
        return bool;
    }

    /**
     * 比较两个金额是否相等
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compare(String str1, String str2){
        boolean bool = false;
        BigDecimal bd1 = new BigDecimal(str1);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal bd2 = new BigDecimal(str2);
        //设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)
        bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
        if(bd1.compareTo(bd2) == 0){
            bool = true;
        }
        return bool;
    }
}
