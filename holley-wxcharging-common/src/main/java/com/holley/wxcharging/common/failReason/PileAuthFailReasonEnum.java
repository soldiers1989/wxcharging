package com.holley.wxcharging.common.failReason;

/**
 * 设备认证返回
 */
public enum PileAuthFailReasonEnum {
    NIL(0, "无"), UNKNOW_PILE(1, "未知设备"), OFFLINE_PILE(2, "设备离线"), FAIL_PILE(3, "设备故障"), UN_READY(4, "此设备尚未插枪"), CHARGING(5, "设备充电中"), HOLD_PILE(6, "设备占用稍后重试"), OTHER(7, "其他");

    private final int    value;
    private final String text;

    PileAuthFailReasonEnum(int value, String text) {
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
        PileAuthFailReasonEnum task = getEnmuByValue(value);
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
    public static PileAuthFailReasonEnum getEnmuByValue(int value) {
        for (PileAuthFailReasonEnum record : PileAuthFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
