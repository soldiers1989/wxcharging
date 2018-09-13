package com.holley.wxcharging.common.failReason;

/**
 * 短信验证码发送返回
 */
public enum PhoneCodeFailReasonEnum {
    NIL(0, "无"), ERROR_PHONE(1, "手机号有误"), OTHER(2, "其他");

    private final int    value;
    private final String text;

    PhoneCodeFailReasonEnum(int value, String text) {
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
        PhoneCodeFailReasonEnum task = getEnmuByValue(value);
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
    public static PhoneCodeFailReasonEnum getEnmuByValue(int value) {
        for (PhoneCodeFailReasonEnum record : PhoneCodeFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
