package com.holley.wxcharging.common.constants;

/**
 * 自行车付费模式 <br>
 */
public enum BikeFeeTypeEnum {
                             TYPE1(1, "模式1"), TYPE2(2, "模式2"), TYPE3(3, "模式3");

    private final int    value;
    private final String text;

    BikeFeeTypeEnum(int value, String text) {
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
        BikeFeeTypeEnum task = getEnmuByValue(value);
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
    public static BikeFeeTypeEnum getEnmuByValue(int value) {
        for (BikeFeeTypeEnum record : BikeFeeTypeEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
