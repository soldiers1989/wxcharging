package com.holley.wxcharging.common.constants;

/**
 * 返回结果类型 <br>
 */
public enum IsStartChargingEnum {
    CHARGING(1, "是"), UN_CHARGING(2, "否"), RUN_CHARGING(3, "充电启动中");

    private final int    value;
    private final String text;

    IsStartChargingEnum(int value, String text) {
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
        IsStartChargingEnum task = getEnmuByValue(value);
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
    public static IsStartChargingEnum getEnmuByValue(int value) {
        for (IsStartChargingEnum record : IsStartChargingEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
