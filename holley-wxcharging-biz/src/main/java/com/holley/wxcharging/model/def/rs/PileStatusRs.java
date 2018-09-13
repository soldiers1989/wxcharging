package com.holley.wxcharging.model.def.rs;

/**
 * 请求桩状态rs
 * 
 * @author sc
 */
public class PileStatusRs {

    private String pileCode;       // 桩编码
    private int    pileStatus;     // 桩状态
    private String pileStatusDesc; // 桩状态描述

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode;
    }

    public int getPileStatus() {
        return pileStatus;
    }

    public void setPileStatus(int pileStatus) {
        this.pileStatus = pileStatus;
    }

    public String getPileStatusDesc() {
        return pileStatusDesc;
    }

    public void setPileStatusDesc(String pileStatusDesc) {
        this.pileStatusDesc = pileStatusDesc;
    }

}
