package com.holley.wxcharging.common.constants;

/**
 * 返回结果类型 <br>
 */
public enum HasUnpaidFeeEnum {
    UN_HAS(0, "无"), HAS(1, "有");

    private final int    value;
    private final String text;

    HasUnpaidFeeEnum(int value, String text) {
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
        HasUnpaidFeeEnum task = getEnmuByValue(value);
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
    public static HasUnpaidFeeEnum getEnmuByValue(int value) {
        for (HasUnpaidFeeEnum record : HasUnpaidFeeEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
