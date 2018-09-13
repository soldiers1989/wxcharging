package com.holley.wxcharging.common.constants;

/**
 * 支付方式 <br>
 */
public enum LoginStatusEnum {
    LOGIN(1, "登录"), LOGIN_OUT(2, "未登录");

    private final int    value;
    private final String text;

    LoginStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static String getText(int value) {
        LoginStatusEnum task = getEnmuByValue(value);
        return task == null ? null : task.getText();
    }

    public Short getShortValue() {
        Integer obj = value;
        return obj.shortValue();
    }

    /**
     * 通过传入的值匹配枚举
     * 
     * @param value
     * @return
     */
    public static LoginStatusEnum getEnmuByValue(int value) {
        for (LoginStatusEnum record : LoginStatusEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
