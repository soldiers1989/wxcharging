package com.holley.wxcharging.common.failReason;

/**
 * 停止充电返回
 */
public enum StopChargingFailReasonEnum {
    NIL(0, "无"), UNKNOW_PILE(1, "未知设备"), OFFLINE_PILE(2, "设备离线"), FAIL_PILE(3, "设备故障"), OTHER_USER(4, "其他用户正在充电"), OTHER(5, "其他");

    private final int    value;
    private final String text;

    StopChargingFailReasonEnum(int value, String text) {
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
        StopChargingFailReasonEnum task = getEnmuByValue(value);
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
    public static StopChargingFailReasonEnum getEnmuByValue(int value) {
        for (StopChargingFailReasonEnum record : StopChargingFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
