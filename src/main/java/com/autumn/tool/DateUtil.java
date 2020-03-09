package com.autumn.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: DateUtil
 * @description: 日期工具类
 * @Author 秋雨
 */
public class DateUtil {

    public static String getDateFormat(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取当前日期(年月日)
     * @return 例如: 2020-03-09
     */
    public static String getCurrDate(){
        Calendar calendar = Calendar.getInstance();
        return getDateFormat(calendar.getTime(),"yyyy-MM-dd");
    }

    /**
     * 获取当前时间(年月日时分秒)
     * @return
     */
    public static String getCurrTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        return time;
    }

    public static void main(String[] args) {
        System.out.println(getCurrTime());
    }
}
