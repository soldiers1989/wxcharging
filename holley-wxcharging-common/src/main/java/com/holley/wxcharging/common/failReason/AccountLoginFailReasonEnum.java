package com.holley.wxcharging.common.failReason;

/**
 * 账号登录返回
 */
public enum AccountLoginFailReasonEnum {
    NIL(0, "无"), ERROR_ACCOUNT(1, "账号或密码有误"), UNKONWN_ACCOUNT(2, "账号不存在"), OTHER(3, "其他");

    private final int    value;
    private final String text;

    AccountLoginFailReasonEnum(int value, String text) {
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
        AccountLoginFailReasonEnum task = getEnmuByValue(value);
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
    public static AccountLoginFailReasonEnum getEnmuByValue(int value) {
        for (AccountLoginFailReasonEnum record : AccountLoginFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
