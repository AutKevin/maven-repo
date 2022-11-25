package com.autumn.tool;

import org.apache.commons.lang3.StringUtils;

/**
 * 敏感数据脱敏方法
 */
public class MaskUtil {

    /**
     * 用户身份证号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    public static String idCardMask(String idCardNum) {
        String res = "";
        if (!StringUtils.isEmpty(idCardNum)) {
            StringBuilder stringBuilder = new StringBuilder(idCardNum);
            res = stringBuilder.replace(2, 16, "**************").toString();  //前2后2
            //res = stringBuilder.replace(6, 14, "********").toString();  //前6后4
        }
        return res;
    }

    /**
     * 用户电话号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    public static String phoneMask(String phone) {
        String res = "";
        if (!StringUtils.isEmpty(phone)) {
            StringBuilder stringBuilder = new StringBuilder(phone);
            res = stringBuilder.replace(3, 7, "****").toString();
        }
        return res;
    }
}
