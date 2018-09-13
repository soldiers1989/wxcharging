package com.holley.wxcharging.common.constants;

/**
 * @author LHP
 */
public enum BikePayStatusEnum {
                               UNPAID(1, "未支付"), PAID_UNCHARGED(2, "已支付未充电"), CHARGED(3, "已充电"), REFUND(4, "已退款"), CLOSED(5, "交易关闭");

    private final int    value;
    private final String text;

    BikePayStatusEnum(int value, String text) {
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
        BikePayStatusEnum task = getEnmuByValue(value);
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
    public static BikePayStatusEnum getEnmuByValue(int value) {
        for (BikePayStatusEnum record : BikePayStatusEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
