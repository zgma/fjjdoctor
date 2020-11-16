package com.qingeng.fjjdoctor.enums;

public enum EditEnum {
    DOCTOR_REAL_NAME("realName", "真实姓名"),
    DOCTOR_ASK_PRICE("askPrice", "咨询费用"),
    DOCTOR_RIW_PRICE("riwPrice", "预约费用"),

    ;
    private String value;
    private String name;

    EditEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String name(String value) {
        for (EditEnum vs : values()) {
            if (vs.getValue().equals(value)) {
                return vs.getName();
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
