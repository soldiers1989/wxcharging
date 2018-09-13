package com.holley.wxcharging.model.def.rs;

/**
 * 历史充电数据统计rs
 * 
 * @author sc
 */
public class StatChargingInfoRs {

    /**
     * 总充电时长
     */
    private int    totalChaLen;
    /**
     * 总充电度数
     */
    private double totalChaPower;
    /**
     * 总充电次数
     */
    private int    totalChaCount;
    /**
     * 总充电金额
     */
    private double totalChaMoney;

    public int getTotalChaLen() {
        return totalChaLen;
    }

    public void setTotalChaLen(int totalChaLen) {
        this.totalChaLen = totalChaLen;
    }

    public double getTotalChaPower() {
        return totalChaPower;
    }

    public void setTotalChaPower(double totalChaPower) {
        this.totalChaPower = totalChaPower;
    }

    public int getTotalChaCount() {
        return totalChaCount;
    }

    public void setTotalChaCount(int totalChaCount) {
        this.totalChaCount = totalChaCount;
    }

    public double getTotalChaMoney() {
        return totalChaMoney;
    }

    public void setTotalChaMoney(double totalChaMoney) {
        this.totalChaMoney = totalChaMoney;
    }

}
