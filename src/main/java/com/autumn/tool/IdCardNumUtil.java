package com.autumn.tool;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 身份证工具类
 * @author 秋雨
 */
public class IdCardNumUtil {
    //省份
    private String province;
    private String city;
    private String region;
    //性别
    private String gender;
    //生日
    private Date birthday;
    //出生 - 年
    private int year;
    //出生 - 月
    private int month;
    //出生 - 日
    private int day;
    /**
     * 年龄：根据年份来计算
     */
    private int age;

    private Map<String, String> cityCodeMap = new HashMap<String, String>() {
        {
            this.put("11", "北京");
            this.put("12", "天津");
            this.put("13", "河北");
            this.put("14", "山西");
            this.put("15", "内蒙古");
            this.put("21", "辽宁");
            this.put("22", "吉林");
            this.put("23", "黑龙江");
            this.put("31", "上海");
            this.put("32", "江苏");
            this.put("33", "浙江");
            this.put("34", "安徽");
            this.put("35", "福建");
            this.put("36", "江西");
            this.put("37", "山东");
            this.put("41", "河南");
            this.put("42", "湖北");
            this.put("43", "湖南");
            this.put("44", "广东");
            this.put("45", "广西");
            this.put("46", "海南");
            this.put("50", "重庆");
            this.put("51", "四川");
            this.put("52", "贵州");
            this.put("53", "云南");
            this.put("54", "西藏");
            this.put("61", "陕西");
            this.put("62", "甘肃");
            this.put("63", "青海");
            this.put("64", "宁夏");
            this.put("65", "新疆");
            this.put("71", "台湾");
            this.put("81", "香港");
            this.put("82", "澳门");
            this.put("91", "国外");
        }
    };

    /**
     * 验证码权值
     * 身份证前17位数字校验码权值,权值*位数 累加后模11取余数,校验码表[余数]
     */
    private static final int[] VERIFY_CODE_WEIGHT = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    /**
     * 18位身份证中最后一位校验码
     */
    private static final char[] VERIFY_CODE = {'1','0','X','9','8','7','6','5','4','3','2'};

    /**
     * 校验码计算 (第十八位)
     * @param idcard 身份证号
     * @return char 校验位值(第18位)
     * 十七位数字本体码加权求和公式 S = Sum(ID[i] * Wi), i = 0...16 ，先对前17位数字的权求和
     * ID[i]:表示第i位置上的身份证号码数字值
     * Wi:表示第i位置上的系数 Wi:
     *      位数	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17
     *      系数	7	9	10	5	8	4	2	1	6	3	7	9	10	5	8	4	2
     * 计算模 Y = mod(S, 11)
     * 通过模得到对应的校验码,校验位对应表：
     *      0 1 2 3 4 5 6 7 8 9 10
     *      1 0 X 9 8 7 6 5 4 3 2
     */
    private static char calculateVerifyCode(CharSequence idcard) {
        int sum = 0;
        char c;
        int c_i;
        for (int i = 0; i < 17; i++) {  //遍历身份证前17位
            c = idcard.charAt(i);   //身份证字符
            c_i = (int) (c - '0');      //字符转为数字
            sum += c_i * VERIFY_CODE_WEIGHT[i];  //累加
        }
        return VERIFY_CODE[sum % 11];  //计算校验位
    }


    /**
     * 验证身份证 idcard
     * 长度验证 18位
     * 数字验证 全数字匹配
     * 校验位验证 最后一位
     *     位数	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	16	17
     *     系数	7	9	10	5	8	4	2	1	6	3	7	9	10	5	8	4	2
     *     [位数]*系数结果相加,然后对11取余,得到一个0-10的数字尾数
     *     校验位对应表：
     *          0 1 2 3 4 5 6 7 8 9 10
     *          1 0 X 9 8 7 6 5 4 3 2
     * @param idcard
     * @return 身份证号是否正确 true:正确 false：不正确
     */
    public static boolean checkIdCard(String idcard){
        //为空或者长度不为18，返回false
        if(idcard == null || "".equals(idcard) || idcard.length()!=18){
            return false;
        }
        //是否为全数字
        if(!idcard.matches("^[0-9]*$")){
            return false;
        }
        //验证位验证
        char verifyCode = calculateVerifyCode(idcard);  //计算出最后一位
        if(verifyCode == idcard.charAt(17)){   //如果校验位和最后一位数字一样
            return true;  //身份证正确
        }else{
            return false;  //身份证不正确
        }

    }

    public IdCardNumUtil(String idcard) {
        try {
            if (checkIdCard(idcard)) {  //校验身份证证
                //省份判断 - 前2位
                String provinceId = idcard.substring(0, 2);
                Set<String> keySet = this.cityCodeMap.keySet();
                Iterator provinceIterator = keySet.iterator();
                String id;
                while(provinceIterator.hasNext()) {
                    id = (String)provinceIterator.next();
                    if (id.equals(provinceId)) {
                        this.province = this.cityCodeMap.get(id);
                        break;
                    }
                }

                //性别判断 - 第17位奇偶性
                id = idcard.substring(16, 17);
                this.gender = Integer.parseInt(id) % 2 == 1 ? "男" : "女";
                //出生年月 - 第6-14位
                String birthday = idcard.substring(6, 14);
                Date birthdate = (new SimpleDateFormat("yyyyMMdd")).parse(birthday);
                this.birthday = birthdate;
                GregorianCalendar currentDay = new GregorianCalendar();
                currentDay.setTime(birthdate);
                this.year = currentDay.get(1);
                this.month = currentDay.get(2) + 1;
                this.day = currentDay.get(5);
                //年龄计算（根据年份来计算）
                int currentYear = Calendar.getInstance().get(1);
                this.age = currentYear - this.year;
            }else{
                throw new RuntimeException("身份证不正确！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getProvince() {
        return this.province;
    }

    public String getCity() {
        return this.city;
    }

    public String getRegion() {
        return this.region;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public String getGender() {
        return this.gender;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toString() {
        return "省份：" + this.province +
                ",性别：" + this.gender +
                ",年龄："+this.age +
                ",出生日期：" + this.birthday;
    }

    public static void main(String[] args) {
        String idcard = "XXXXXX199XXXXXXXXX";
        IdCardNumUtil id = new IdCardNumUtil(idcard);
        System.out.println(id.toString());
    }

}
