package com.holley.wxcharging.common.failReason;

/**
 * 充电支付返回
 */
public enum PaymentFailReasonEnum {
    NIL(0, "无"), ERROR_TRADER(1, "订单编号有误"), ALREADY_PAID(2, "订单已经支付"), LESS_USABLE_MONEY(3, "账户余额不足"), ERROR_PAY(4, "支付失败"), OTHER(5, "其他");

    private final int    value;
    private final String text;

    PaymentFailReasonEnum(int value, String text) {
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
        PaymentFailReasonEnum task = getEnmuByValue(value);
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
    public static PaymentFailReasonEnum getEnmuByValue(int value) {
        for (PaymentFailReasonEnum record : PaymentFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
