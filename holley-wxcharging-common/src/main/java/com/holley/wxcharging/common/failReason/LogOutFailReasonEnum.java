package com.holley.wxcharging.common.failReason;

/**
 * 注销
 */
public enum LogOutFailReasonEnum {
    NIL(0, "无"), OTHER(1, "其他");

    private final int    value;
    private final String text;

    LogOutFailReasonEnum(int value, String text) {
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
        LogOutFailReasonEnum task = getEnmuByValue(value);
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
    public static LogOutFailReasonEnum getEnmuByValue(int value) {
        for (LogOutFailReasonEnum record : LogOutFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
