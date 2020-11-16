package com.qingeng.fjjdoctor.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
		总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
		*/
        String telRegex = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static boolean PwdFormat(String pwd) {
        // ^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\(\)])+$)([^(0-9a-zA-Z)]|[\(\)]|[a-zA-Z]|[0-9]){6,20}$
        // ^(?![A-Za-z]+$)(?![A-Z\d]+$)(?![A-Z\W]+$)(?![a-z\d]+$)(?![a-z\W]+$)(?![\d\W]+$)\S{6,20}$
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)([^(0-9a-zA-Z)]|[\\(\\)]|[a-zA-Z]|[0-9]){6,20}$");
        Matcher m = p.matcher(pwd);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static boolean realName(String name) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]+$|^[\\u4e00-\\u9fa5]+[·][\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(name);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static boolean realEnglishName(String str) {
        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;

    }


    public static boolean mainlandId(String mainlanId) {
        Pattern p = Pattern.compile("(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$)");
        Matcher m = p.matcher(mainlanId);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean hongKongAoMenId(String hongKongAoMenId) {
        Pattern p = Pattern.compile("([A-Z][0-9]{6}\\([0-9A]\\))|([157][0-9]{6}\\([0-9A-Za-z]\\))");
        Matcher m = p.matcher(hongKongAoMenId);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean taiwanId(String other) {
        Pattern p = Pattern.compile("^[A-Z][0-9]{9}$");
        Matcher m = p.matcher(other);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static boolean email(String s){
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher m = p.matcher(s);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
