package com.qingeng.fjjdoctor.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MoneyUtils {

    public static int yuanString2FenInt(String yuanStr) {
        BigDecimal b = new BigDecimal(yuanStr);
        return b.multiply(BigDecimal.valueOf(100)).intValue();
    }

    public static String fenInt2YuanString(Integer fenInt) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((float) fenInt / 100f);
    }

}
