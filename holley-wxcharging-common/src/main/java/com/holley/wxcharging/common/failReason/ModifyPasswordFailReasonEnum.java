package com.holley.wxcharging.common.failReason;

/**
 * 短信验证码发送返回
 */
public enum ModifyPasswordFailReasonEnum {
    NIL(0, "无"), ERROR_PASSWORD(1, "原密码有误"), ERROR_CONFIRM_PASSWORD(2, "两次密码输入不一致"), OTHER(3, "其他");

    private final int    value;
    private final String text;

    ModifyPasswordFailReasonEnum(int value, String text) {
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
        ModifyPasswordFailReasonEnum task = getEnmuByValue(value);
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
    public static ModifyPasswordFailReasonEnum getEnmuByValue(int value) {
        for (ModifyPasswordFailReasonEnum record : ModifyPasswordFailReasonEnum.values()) {
            if (value == record.getValue()) {
                return record;
            }
        }
        return null;
    }
}
