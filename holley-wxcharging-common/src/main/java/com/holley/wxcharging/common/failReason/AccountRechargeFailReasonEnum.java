package com.holley.wxcharging.common.failReason;

/**
 * 账户充值返回
 */
public enum AccountRechargeFailReasonEnum {
    NIL(0, "无"), ERROR_MONEY(1, "充值金额有误"), ERROR_PAY(2, "支付失败"), OTHER(3, "其他");

    private final int    value;
    private final String text;

    AccountRechargeFailReasonEnum(int value, String text) {
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
        AccountRechargeFailReasonEnum task = getEnmuByValue(value);
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
    public static AccountRechargeFailReasonEnum getEnmuByValue(int value) {
        for (AccountRechargeFailReasonEnum record : AccountRechargeFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
