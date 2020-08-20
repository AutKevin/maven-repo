package com.autumn.cron;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 生成计划的详细描述
 * CreateCron
 * @author: 秋雨
 * 2020-08-20 11:09
 **/
public class CreateCron {

    /**
     * 解析正则表达式
     * @param cronStr
     * @return
     */
    public static CronModule decron(String cronStr){
        CronModule cron = new CronModule();
        String second = "";
        String minute = "";
        String hour = "";
        String dayOfMonth = "";
        String month = "";
        String dayOfWeek = "";

        String[] cronArr = cronStr.split(" ");
        second = cronArr[0];
        minute = cronArr[1];
        hour = cronArr[2];
        dayOfMonth = cronArr[3];
        month = cronArr[4];
        dayOfWeek = cronArr[5];

        /*秒解析*/
        if (second.indexOf(",")>0){    //,号分割
            String[] secondArr = second.split(",");
            Integer[] secondResult = new Integer[secondArr.length];
            for (int i =0;i<secondArr.length;i++){
                secondResult[i] = Integer.parseInt(secondArr[i]);
            }
            cron.setSecond(secondResult);
        }else if(second.indexOf("-")>0){   //范围
            String[] secondArr = second.split("-");
            Integer start = Integer.parseInt(secondArr[0]);
            Integer end = Integer.parseInt(secondArr[1]);
            List<Integer> secondList = new ArrayList<Integer>();
            int index = 0;
            for (int i=start;i<=end;i++){
                secondList.add(i);
            }
            Integer[] secondResult = secondList.toArray(new Integer[end-start+1]);
            cron.setSecond(secondResult);
        }else if (second.indexOf("/")>0){    //递增
            String[] secondArr = second.split("/");
            Integer start = Integer.parseInt(secondArr[0]);
            Integer inc = Integer.parseInt(secondArr[1]);
            List<Integer> secondList = new ArrayList<Integer>();
            for (int i =start;i<=59;i=i+inc){
                secondList.add(i);
            }
            Integer[] secondResult = secondList.toArray(new Integer[secondList.size()]);
            cron.setSecond(secondResult);
        }else if ("*".equals(second)){    //全部
            List<Integer> secondList = new ArrayList<Integer>();
            for (int i =0;i<=59;i=i+1){
                secondList.add(i);
            }
            Integer[] secondResult = secondList.toArray(new Integer[secondList.size()]);
            cron.setSecond(secondResult);
        }else{   //单个
            Integer[] secondResult = new Integer[]{Integer.parseInt(second)};
            cron.setSecond(secondResult);
        }

        /*分解析*/
        if (minute.indexOf(",")>0){    //,号分割
            String[] minuteArr = minute.split(",");
            Integer[] minuteResult = new Integer[minuteArr.length];
            for (int i =0;i<minuteArr.length;i++){
                minuteResult[i] = Integer.parseInt(minuteArr[i]);
            }
            cron.setMinute(minuteResult);
        }else if(minute.indexOf("-")>0){   //范围
            String[] minuteArr = minute.split("-");
            Integer start = Integer.parseInt(minuteArr[0]);
            Integer end = Integer.parseInt(minuteArr[1]);
            List<Integer> minuteList = new ArrayList<Integer>();
            int index = 0;
            for (int i=start;i<=end;i++){
                minuteList.add(i);
            }
            Integer[] minuteResult = minuteList.toArray(new Integer[end-start+1]);
            cron.setMinute(minuteResult);
        }else if (minute.indexOf("/")>0){    //递增
            String[] minuteArr = minute.split("/");
            Integer start = Integer.parseInt(minuteArr[0]);
            Integer inc = Integer.parseInt(minuteArr[1]);
            List<Integer> minuteList = new ArrayList<Integer>();
            for (int i =start;i<=59;i=i+inc){
                minuteList.add(i);
            }
            Integer[] minuteResult = minuteList.toArray(new Integer[minuteList.size()]);
            cron.setMinute(minuteResult);
        }else if ("*".equals(minute)){    //全部
            List<Integer> minuteList = new ArrayList<Integer>();
            for (int i =0;i<=59;i=i+1){
                minuteList.add(i);
            }
            Integer[] minuteResult = minuteList.toArray(new Integer[minuteList.size()]);
            cron.setMinute(minuteResult);
        }else{   //单个
            Integer[] minuteResult = new Integer[]{Integer.parseInt(minute)};
            cron.setMinute(minuteResult);
        }

        /*时解析*/
        if (hour.indexOf(",")>0){    //,号分割
            String[] hourArr = hour.split(",");
            Integer[] hourResult = new Integer[hourArr.length];
            for (int i =0;i<hourArr.length;i++){
                hourResult[i] = Integer.parseInt(hourArr[i]);
            }
            cron.setHour(hourResult);
        }else if(hour.indexOf("-")>0){   //范围
            String[] hourArr = hour.split("-");
            Integer start = Integer.parseInt(hourArr[0]);
            Integer end = Integer.parseInt(hourArr[1]);
            List<Integer> hourList = new ArrayList<Integer>();
            int index = 0;
            for (int i=start;i<=end;i++){
                hourList.add(i);
            }
            Integer[] hourResult = hourList.toArray(new Integer[end-start+1]);
            cron.setHour(hourResult);
        }else if (hour.indexOf("/")>0){    //递增
            String[] hourArr = hour.split("/");
            Integer start = Integer.parseInt(hourArr[0]);
            Integer inc = Integer.parseInt(hourArr[1]);
            List<Integer> hourList = new ArrayList<Integer>();
            for (int i =start;i<=23;i=i+inc){
                hourList.add(i);
            }
            Integer[] hourResult = hourList.toArray(new Integer[hourList.size()]);
            cron.setHour(hourResult);
        }else if ("*".equals(hour)){    //全部
            List<Integer> hourList = new ArrayList<Integer>();
            for (int i =0;i<=23;i=i+1){
                hourList.add(i);
            }
            Integer[] hourResult = hourList.toArray(new Integer[hourList.size()]);
            cron.setHour(hourResult);
        }else{   //单个
            Integer[] hourResult = new Integer[]{Integer.parseInt(hour)};
            cron.setHour(hourResult);
        }

        /*月日期解析*/
        if (dayOfMonth.indexOf(",")>0){    //,号分割
            String[] dayOfMonthArr = dayOfMonth.split(",");
            Integer[] dayOfMonthResult = new Integer[dayOfMonthArr.length];
            for (int i =0;i<dayOfMonthArr.length;i++){
                dayOfMonthResult[i] = Integer.parseInt(dayOfMonthArr[i]);
            }
            cron.setDayOfMonth(dayOfMonthResult);
        }else if(dayOfMonth.indexOf("-")>0){   //范围
            String[] dayOfMonthArr = dayOfMonth.split("-");
            Integer start = Integer.parseInt(dayOfMonthArr[0]);
            Integer end = Integer.parseInt(dayOfMonthArr[1]);
            List<Integer> dayOfMonthList = new ArrayList<Integer>();
            for (int i=start;i<=end;i++){
                dayOfMonthList.add(i);
            }
            Integer[] dayOfMonthResult = dayOfMonthList.toArray(new Integer[end-start+1]);
            cron.setDayOfMonth(dayOfMonthResult);
        }else if (dayOfMonth.indexOf("/")>0){    //递增
            String[] dayOfMonthArr = dayOfMonth.split("/");
            Integer start = Integer.parseInt(dayOfMonthArr[0]);
            Integer inc = Integer.parseInt(dayOfMonthArr[1]);
            List<Integer> dayOfMonthList = new ArrayList<Integer>();
            for (int i =start;i<=31;i=i+inc){
                dayOfMonthList.add(i);
            }
            Integer[] dayOfMonthResult = dayOfMonthList.toArray(new Integer[dayOfMonthList.size()]);
            cron.setDayOfMonth(dayOfMonthResult);
        }else if ("*".equals(dayOfMonth)){    //全部
            List<Integer> dayOfMonthList = new ArrayList<Integer>();
            for (int i =1;i<=31;i=i+1){
                dayOfMonthList.add(i);
            }
            Integer[] dayOfMonthResult = dayOfMonthList.toArray(new Integer[dayOfMonthList.size()]);
            cron.setDayOfMonth(dayOfMonthResult);
        }else{   //单个
            Integer[] dayOfMonthResult = new Integer[]{Integer.parseInt(dayOfMonth)};
            cron.setDayOfMonth(dayOfMonthResult);
        }

        /**月份解析
         * Jan January
         Feb February
         Mar March
         Apr April
         May
         Jun June
         Jul July
         Aug August
         Sep September
         Oct October
         Nov November
         De  December*/
        month = month.toUpperCase().replaceAll("Jan","1");
        month = month.toUpperCase().replaceAll("Feb","2");
        month = month.toUpperCase().replaceAll("Mar","3");
        month = month.toUpperCase().replaceAll("Apr","4");
        month = month.toUpperCase().replaceAll("May","5");
        month = month.toUpperCase().replaceAll("Jun","6");
        month = month.toUpperCase().replaceAll("Jul","7");
        month = month.toUpperCase().replaceAll("Aug","8");
        month = month.toUpperCase().replaceAll("Sep","9");
        month = month.toUpperCase().replaceAll("Oct","10");
        month = month.toUpperCase().replaceAll("Nov","11");
        month = month.toUpperCase().replaceAll("Dec","12");

        if (month.indexOf(",")>0){    //,号分割
            String[] monthArr = month.split(",");
            Integer[] monthResult = new Integer[monthArr.length];
            for (int i =0;i<monthArr.length;i++){
                monthResult[i] = Integer.parseInt(monthArr[i]);
            }
            cron.setMonth(monthResult);
        }else if(month.indexOf("-")>0){   //范围
            String[] monthArr = month.split("-");
            Integer start = Integer.parseInt(monthArr[0]);
            Integer end = Integer.parseInt(monthArr[1]);
            List<Integer> monthList = new ArrayList<Integer>();
            int index = 0;
            for (int i=start;i<=end;i++){
                monthList.add(i);
            }
            Integer[] monthResult = monthList.toArray(new Integer[end-start+1]);
            cron.setMonth(monthResult);
        }else if (month.indexOf("/")>0){    //递增
            String[] monthArr = month.split("/");
            Integer start = Integer.parseInt(monthArr[0]);
            Integer inc = Integer.parseInt(monthArr[1]);
            List<Integer> monthList = new ArrayList<Integer>();
            for (int i =start;i<=12;i=i+inc){
                monthList.add(i);
            }
            Integer[] monthResult = monthList.toArray(new Integer[monthList.size()]);
            cron.setMonth(monthResult);
        }else if ("*".equals(month)){    //全部
            List<Integer> monthList = new ArrayList<Integer>();
            for (int i =1;i<=12;i=i+1){
                monthList.add(i);
            }
            Integer[] monthResult = monthList.toArray(new Integer[monthList.size()]);
            cron.setMonth(monthResult);
        }else{   //单个
            Integer[] monthResult = new Integer[]{Integer.parseInt(month)};
            cron.setMonth(monthResult);
        }

        /*周日期解析SUN=1 MON=2 TUE=3 WED=4 THU=5 FRI=6 SAT=7*/
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("SUN","1");
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("MON","2");
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("TUE","3");
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("WED","4");
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("THU","5");
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("FRI","6");
        dayOfWeek = dayOfWeek.toUpperCase().replaceAll("SAT","7");

        if (dayOfWeek.indexOf(",")>0){    //,号分割
            String[] dayOfWeekArr = dayOfWeek.split(",");
            Integer[] dayOfWeekResult = new Integer[dayOfWeekArr.length];
            for (int i =0;i<dayOfWeekArr.length;i++){
                dayOfWeekResult[i] = Integer.parseInt(dayOfWeekArr[i]);
            }
            cron.setDayOfWeek(dayOfWeekResult);
        }else if(dayOfWeek.indexOf("-")>0){   //范围
            String[] dayOfWeekArr = dayOfWeek.split("-");
            Integer start = Integer.parseInt(dayOfWeekArr[0]);
            Integer end = Integer.parseInt(dayOfWeekArr[1]);
            List<Integer> dayOfWeekList = new ArrayList<Integer>();
            int index = 0;
            for (int i=start;i<=end;i++){
                dayOfWeekList.add(i);
            }
            Integer[] dayOfWeekResult = dayOfWeekList.toArray(new Integer[end-start+1]);
            cron.setDayOfWeek(dayOfWeekResult);
        }else if (dayOfWeek.indexOf("/")>0){    //递增
            String[] dayOfWeekArr = dayOfWeek.split("/");
            Integer start = Integer.parseInt(dayOfWeekArr[0]);
            Integer inc = Integer.parseInt(dayOfWeekArr[1]);
            List<Integer> dayOfWeekList = new ArrayList<Integer>();
            for (int i =start;i<=7;i=i+inc){
                dayOfWeekList.add(i);
            }
            Integer[] dayOfWeekResult = dayOfWeekList.toArray(new Integer[dayOfWeekList.size()]);
            cron.setDayOfWeek(dayOfWeekResult);
        }else if ("*".equals(dayOfWeek)){    //全部
            List<Integer> dayOfWeekList = new ArrayList<Integer>();
            for (int i =1;i<=7;i=i+1){
                dayOfWeekList.add(i);
            }
            Integer[] dayOfWeekResult = dayOfWeekList.toArray(new Integer[dayOfWeekList.size()]);
            cron.setDayOfWeek(dayOfWeekResult);
        }else{   //单个
            Integer[] dayOfWeekResult = new Integer[]{Integer.parseInt(dayOfWeek)};
            cron.setDayOfWeek(dayOfWeekResult);
        }
        return cron;
    }

    /**
     * 验证现在是否符合
     * @param cronStr
     * @return
     */
    public static boolean validNow(String cronStr){
        /*解析正则表达式的规范*/
        CronModule decron = decron(cronStr);
        System.out.println("cron表达式:"+decron);

        /*获取当前时间*/
        Calendar calendar = Calendar.getInstance();
        int second_now = calendar.get(Calendar.SECOND);
        int min_now = calendar.get(Calendar.MINUTE);
        int hour_now = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfMonth_now = calendar.get(Calendar.DAY_OF_MONTH);
        int month_now = calendar.get(Calendar.MONTH) + 1;
        int dayOfWeek_now = calendar.get(Calendar.DAY_OF_WEEK);

        System.out.println("当前:"+second_now+" "+min_now+" "+hour_now+" "+dayOfMonth_now+" "+month_now+" "+dayOfWeek_now);
        if(ArrayUtils.contains(decron.getSecond(),second_now)){
            if(ArrayUtils.contains(decron.getMinute(),min_now)){
                if(ArrayUtils.contains(decron.getHour(),hour_now)){
                    if(ArrayUtils.contains(decron.getDayOfMonth(),dayOfMonth_now)){
                        if(ArrayUtils.contains(decron.getMonth(),month_now)){
                            if(ArrayUtils.contains(decron.getDayOfWeek(),dayOfWeek_now)){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    /**
     * 启动
     * @param cronStr cron表达式
     * @param tasks 要执行的任务list
     * @return
     */
    public static boolean start(String cronStr,List<CronTask> tasks){
        while(true){
            /*如果当前时间符合cron表达式*/
            if(validNow(cronStr)){
                //遍历并执行tasks
                if(tasks!=null&&tasks.size()>0){
                    for(CronTask t:tasks){
                        t.doTask();
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //正则表达式
        String cronStr = "0/3 * 16 * * THU";
        //任务列表
        List<CronTask> cronTasks = new ArrayList<CronTask>();
        CronTask cronTask1 = new CronTask() {
            @Override
            public String doTask() {
                System.out.println("任务一");
                return null;
            }
        };

        CronTask cronTask2 = new CronTask() {
            @Override
            public String doTask() {
                System.out.println("任务二");
                return null;
            }
        };
        cronTasks.add(cronTask1);
        cronTasks.add(cronTask2);
        //启动
        start(cronStr,cronTasks);
    }
}
