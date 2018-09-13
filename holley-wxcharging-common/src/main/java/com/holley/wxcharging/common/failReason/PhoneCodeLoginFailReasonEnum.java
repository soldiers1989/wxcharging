package com.holley.wxcharging.common.failReason;

/**
 * 短信验证码登录返回
 */
public enum PhoneCodeLoginFailReasonEnum {
    NIL(0, "无"), ERROR_PHONE(1, "手机号有误"), UNKNOW_PHONE(2, "该手机号未注册"), ERROR_CODE(3, "短信验证码有误"), OTHER(4, "其他");

    private final int    value;
    private final String text;

    PhoneCodeLoginFailReasonEnum(int value, String text) {
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
        PhoneCodeLoginFailReasonEnum task = getEnmuByValue(value);
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
    public static PhoneCodeLoginFailReasonEnum getEnmuByValue(int value) {
        for (PhoneCodeLoginFailReasonEnum record : PhoneCodeLoginFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
