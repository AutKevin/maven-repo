package com.autumn.cron;

import java.util.Arrays;

/**
 * scheduleModule
 * corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份
 * @author: 秋雨
 * 2020-08-20 10:54
 **/
public class CronModule {

    /**秒,0~59的整数,支持字符: , - * /  **/
    private Integer[] second;

    /**分,0~59的整数,支持字符: , - * /  **/
    private Integer[] minute;

    /**时,0~23的整数,支持字符: , - * /  **/
    private Integer[] hour;

    /**一个月的哪几天,1~31的整数,支持字符: ,- * / 暂不支持: ? L W C **/
    private Integer[] dayOfMonth;

    /**月,支持字符: , - * /
       1  Jan January
       2  Feb February
       3  Mar March
       4  Apr April
       5  May
       6  Jun June
       7  Jul July
       8  Aug August
       9  Sep September
       10 Oct October
       11 Nov November
       12 De  December**/
    private Integer[] month;

    /**一周的哪几天,1~7的整数SUN-SAT（1=SUN）,支持字符: ,- * / 暂不支持: ? L W C
     * SUN=1 MON=2 TUE=3 WED=4 THU=5 FRI=6 SAT=7 **/
    private Integer[] dayOfWeek;

    //年份,	1970~2099,支持字符: , - * /  **/
    //private Integer[] years;


    public Integer[] getSecond() {
        return second;
    }

    public void setSecond(Integer[] second) {
        this.second = second;
    }

    public Integer[] getMinute() {
        return minute;
    }

    public void setMinute(Integer[] minute) {
        this.minute = minute;
    }

    public Integer[] getHour() {
        return hour;
    }

    public void setHour(Integer[] hour) {
        this.hour = hour;
    }

    public Integer[] getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer[] dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Integer[] getMonth() {
        return month;
    }

    public void setMonth(Integer[] month) {
        this.month = month;
    }

    public Integer[] getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer[] dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /*public Integer[] getYears() {
        return years;
    }

    public void setYears(Integer[] years) {
        this.years = years;
    }*/

    @Override
    public String toString() {
        return "CronModule{" +
                "second=" + Arrays.toString(second) +
                "\r\n, minute=" + Arrays.toString(minute) +
                "\n, hour=" + Arrays.toString(hour) +
                "\n, dayOfMonth=" + Arrays.toString(dayOfMonth) +
                "\n, month=" + Arrays.toString(month) +
                "\n, dayOfWeek=" + Arrays.toString(dayOfWeek) +
                '}';
    }
}