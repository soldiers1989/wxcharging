package com.holley.wxcharging.common.constants;

/**
 * 返回结果类型 <br>
 */
public enum IsStopChargingEnum {
    STOP_CHARGING(1, "是"), UN_STOP_CHARGING(2, "否");

    private final int    value;
    private final String text;

    IsStopChargingEnum(int value, String text) {
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
        IsStopChargingEnum task = getEnmuByValue(value);
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
    public static IsStopChargingEnum getEnmuByValue(int value) {
        for (IsStopChargingEnum record : IsStopChargingEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
