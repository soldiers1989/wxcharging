package com.holley.wxcharging.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 统一工具类
 * 
 * @author sc
 */
public class WxchargingUtil {

    /**
     * 判断是否超时
     * 
     * @param starttime
     * @param timeout 超时时间秒
     * @return
     */
    public static boolean isTimeOut(long starttime, long timeout) {
        long timeoutlong = timeout * 1000;
        long curr = System.currentTimeMillis();
        return curr - starttime > timeoutlong;
    }

    /**
     * 得到某个月的最后一秒(yyyy-MM-31 23:59:59)
     * 
     * @param date 传入的时间
     * @return
     */
    public static Date getLastSecondOfMonth(Date date) {
        Calendar a = Calendar.getInstance();
        a.setTime(date);
        // 把日期设置为当月第一天
        a.set(Calendar.DATE, 1);
        // 把月份+1，进入下月第一天
        a.add(Calendar.MONTH, 1);
        // 时分秒清零
        a.set(Calendar.HOUR, 0);
        a.set(Calendar.MINUTE, 0);
        a.set(Calendar.SECOND, 0);
        // 减1秒
        a.add(Calendar.SECOND, -1);
        return a.getTime();
    }

    /**
     * 获取当前时间戳，单位秒
     * 
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static BigDecimal getDefaultBigDecimal(BigDecimal data) {
        return data != null ? data : BigDecimal.ZERO;
    }

    public static Double getDefaultDouble(Double data) {
        return data != null ? data : 0.0;
    }

    public static Integer getDefaultInteger(Integer data) {
        return data != null ? data : 0;
    }
}
