package com.holley.wxcharging.common.failReason;

/**
 * 注销
 */
public enum IsLoginFailReasonEnum {
    NIL(0, "无"), OTHER(1, "其他");

    private final int    value;
    private final String text;

    IsLoginFailReasonEnum(int value, String text) {
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
        IsLoginFailReasonEnum task = getEnmuByValue(value);
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
    public static IsLoginFailReasonEnum getEnmuByValue(int value) {
        for (IsLoginFailReasonEnum record : IsLoginFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
