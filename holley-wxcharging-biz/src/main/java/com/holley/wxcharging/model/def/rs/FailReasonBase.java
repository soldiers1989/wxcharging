package com.holley.wxcharging.model.def.rs;

public abstract class FailReasonBase {

    private int    failReason;    // 失败原因
    private String failReasonDesc; // 失败原因描述

    public int getFailReason() {
        return failReason;
    }

    public void setFailReason(int failReason) {
        this.failReason = failReason;
    }

    public String getFailReasonDesc() {
        return failReasonDesc;
    }

    public void setFailReasonDesc(String failReasonDesc) {
        this.failReasonDesc = failReasonDesc;
    }

}
