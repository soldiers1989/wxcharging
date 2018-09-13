package com.holley.wxcharging.common.failReason;

/**
 * 短信验证码发送返回
 */
public enum ForgetPasswordFailReasonEnum {
    NIL(0, "无"), ERROR_PHONE(1, "手机号有误"), UNKNOW_PHONE(2, "该手机号未注册"), ERROR_CODE(3, "短信验证码错误"), ERROR_CONFIRM_PASSWORD(4, "两次密码输入不一致"), ERROR_PASSWORD(5, "请输入6-16位数字加英文的密码"),
    OTHER(6, "其他");

    private final int    value;
    private final String text;

    ForgetPasswordFailReasonEnum(int value, String text) {
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
        ForgetPasswordFailReasonEnum task = getEnmuByValue(value);
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
    public static ForgetPasswordFailReasonEnum getEnmuByValue(int value) {
        for (ForgetPasswordFailReasonEnum record : ForgetPasswordFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
