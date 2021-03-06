package com.meiling.component.utils.string;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author marisareimu
 * @time 2021-05-19 15:33
 */
public class CommonStringUtils {
    /**
     * 1、检查身份证
     * 2、检查网络地址
     */


    /**
     * ********************************************************************************************************************************************
     * 校验身份证号码是否符合规范
     */
    private static final String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    public static boolean isIDCardNumber(String IDCardNumber) {
        if (TextUtils.isEmpty(IDCardNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）

        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾
        boolean matches = IDCardNumber.matches(regularExpression);
        //判断第18位校验值
        if (matches) {
            if (IDCardNumber.length() == 18) {
                try {
                    char[] charArray = IDCardNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return matches;
    }

    /**
     * ***************************************************************************************************************************************
     * 检查路径是否是网络链接
     */

    public static boolean isNetUrlPath(String checkUrlPath) {
        if (TextUtils.isEmpty(checkUrlPath)) {
            return false;
        }
        if (checkUrlPath.toLowerCase().startsWith("http") || checkUrlPath.toLowerCase().startsWith("https")) {
            return true;
        }
        return false;
    }

    /**
     * ***************************************************************************************************************************************
     * 检查检查是否是手机
     */

    public static boolean isMobilePhone(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        String regEx = "^1\\d{10}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * ***************************************************************************************************************************************
     * 检查检查是否是邮箱
     */

    public static boolean isEmail(CharSequence string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
//        String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{1,}$";
        String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{1,}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * ***************************************************************************************************************************************
     * 检查检查是否是都是中文字符
     */

    public static boolean isChinese(String paramValue) {
        String str = "";
        String regex = "([\u4e00-\u9fa5]+)";
        Matcher matcher = Pattern.compile(regex).matcher(paramValue);
        return matcher.matches();
    }
}
