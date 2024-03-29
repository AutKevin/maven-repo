package com.autumn.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间戳工具
 * @author 秋雨
 * 修改历史 
 *  序号------原因------修改人---日期---
 *   1.                               
 *   2.                                
 */
public class TimestampUtil {

    /** 
     * 时间戳转换成日期格式字符串 
     * @param seconds 精确到秒的字符串 
     * @param format 为null时默认yyyy-MM-dd HH:mm:ss
     * @return 返回日期字符串
     */  
    public static String timeStamp2Date(String seconds,String format) {  
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
            return "";  
        }
        
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }   
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //Date是精确到毫秒的13位时间戳,所以秒要*1000
        Date date = new Date(Long.valueOf(seconds+"000"));
        String dateString = sdf.format(date);
        return dateString;  
    }

    /** 
     * 日期格式字符串转换成时间戳 
     * @param date_str 字符串日期
     * @param format 默认：yyyy-MM-dd HH:mm:ss 
     * @return 精确到秒的时间戳
     */
    public static String date2TimeStamp(String date_str,String format){  
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime()/1000);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }

    /** 
     * 取得当前时间戳（精确到秒） 
     * @return 
     */  
    public static String getNowTimeStamp(){  
        long time = System.currentTimeMillis();
        String timestamp = String.valueOf(time/1000);  
        return timestamp;  
    }

    /**
     * 获取当日零点的10位时间戳(精确到秒)
     * @return
     */
    public static long getCurDateTimeStamp(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        java.util.Date date = calendar.getTime();
        long timestamp = date.getTime()/1000;
        return timestamp;
    }
    
    public static void main(String[] args) {
		System.out.println("实时时间戳:"+getNowTimeStamp());
		System.out.println(timeStamp2Date("1637251200", null));
		System.out.println(date2TimeStamp("2021-11-19 00:00:00", null));
	}
}
