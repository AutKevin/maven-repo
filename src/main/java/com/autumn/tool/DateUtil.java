package com.autumn.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author 秋雨
 */
public class DateUtil {

    public static String getDateFormat(Date date,String format){
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 去除数据库查询出来的时间.0结尾的情况
     * @param date_str 数据库查出来的日期字符串,例如: 2020-12-12 12:12:12.0
     * @return 取出.0结尾的两个字符,例如:2020-12-12 12:12:12
     */
    public static String getdateWithoutEnd(String date_str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(date_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdf.format(date);
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
     * 获取当前时间(时分秒)
     * @return HH:mm:ss
     */
    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        return time;
    }

    /**
     * 获取当前时间(年月日时分秒)
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        return time;
    }

    /**
     * 获取当前时间(年月日时分秒)
     * @return yyyyMMddHHmmss
     */
    public static String getCurrTimeForFile(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = simpleDateFormat.format(new Date());
        return time;
    }

    /**
     * nowDT是否在beginDT和endDT之间
     * @param nowDT yyyy-MM-dd 默认为当天
     * @param beginDT yyyy-MM-dd
     * @param endDT yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static boolean belongDate(String nowDT, String beginDT,String endDT) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (nowDT == null || nowDT.trim().length() ==0){
            nowDT = getCurrDate();
        }
        Date nowDate = simpleDateFormat.parse(nowDT);
        Date beginDate = simpleDateFormat.parse(beginDT);
        Date endDate = simpleDateFormat.parse(endDT);

        if (nowDate.getTime() == beginDate.getTime()
                || nowDate.getTime() == endDate.getTime()) {
            return true;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        if (now.after(begin) && now.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * nowTime是否在beginTime和endTime之间
     * @param nowTime 目前时间 HH:mm:ss 默认当前时间
     * @param beginTime 开始时间 HH:mm:ss
     * @param endTime 结束时间 HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static boolean belongTime(String nowTime, String beginTime,String endTime) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        if (nowTime == null || nowTime.trim().length() ==0){
            nowTime = getNowTime();
        }
        Date nowDate = simpleDateFormat.parse(nowTime);
        Date beginDate = simpleDateFormat.parse(beginTime);
        Date endDate = simpleDateFormat.parse(endTime);

        if (nowDate.getTime() == beginDate.getTime()
                || nowDate.getTime() == endDate.getTime()) {
            return true;
        }

        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        if (now.after(begin) && now.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongDatetime(Date nowTime, Date beginTime,Date endTime) {
        if (nowTime.getTime() == beginTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) throws ParseException {
        //System.out.println(getCurrTime());
        //System.out.println(getCurrDate());

        System.out.println(belongTime(null, "08:00:08", "17:06:50"));
        System.out.println(belongDate(null,"2020-03-17","2020-03-18"));
    }
}
