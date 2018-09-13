package com.holley.wxcharging.common.failReason;

public enum StationDetailFailReasonEnum {
                                         NIL(0, "无"), WRONG_PARAM(1, "参数错误"), OTHER(2, "其他");

    private final int    value;
    private final String text;

    StationDetailFailReasonEnum(int value, String text) {
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
        StationDetailFailReasonEnum task = getEnmuByValue(value);
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
    public static StationDetailFailReasonEnum getEnmuByValue(int value) {
        for (StationDetailFailReasonEnum record : StationDetailFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
