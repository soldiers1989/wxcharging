package com.holley.wxcharging.common.failReason;

/**
 * 注册返回
 */
public enum RegisterFailReasonEnum {
    NIL(0, "无"), ERROR_PHONE(1, "手机号有误"), REPEAT_PHONE(2, "该手机号已注册"), ERROR_CODE(3, "验证码错误"), ERROR_CONFIRM_PASSWORD(4, "两次密码输入不一致"), REPEAT_USERNAME(5, "用户名已存在"),
    PASSWORD_ERROR(6, "请输入6-16位数字加英文的密码"), USERNAME_ERROR(7, "请输入6-17位英文加数字组合的用户名"), OTHER(8, "其他");

    private final int    value;
    private final String text;

    RegisterFailReasonEnum(int value, String text) {
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
        RegisterFailReasonEnum task = getEnmuByValue(value);
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
    public static RegisterFailReasonEnum getEnmuByValue(int value) {
        for (RegisterFailReasonEnum record : RegisterFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
