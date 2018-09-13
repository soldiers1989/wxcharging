package com.holley.wxcharging.common.constants;

public enum TradeTypeEnum {
                           BIKE_PAY(1, "自行车充电支付"), ACC_RECHARGE(2, "账户充值"), CAR_PAY(3, "汽车充电支付");

    private final int    value;
    private final String text;

    TradeTypeEnum(int value, String text) {
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
        TradeTypeEnum task = getEnmuByValue(value);
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
    public static TradeTypeEnum getEnmuByValue(int value) {
        for (TradeTypeEnum record : TradeTypeEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }

}
