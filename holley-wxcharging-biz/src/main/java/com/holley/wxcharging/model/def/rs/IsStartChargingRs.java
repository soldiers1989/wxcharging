package com.holley.wxcharging.model.def.rs;

/**
 * 启动充电rs
 * 
 * @author sc
 */
public class IsStartChargingRs {

    private String pileCode;                 // 充电桩编码
    private String tradeNo;                  // 订单编号
    private int    isStartChargingStatus;
    private String isStartChargingStatusDesc;
    private long   delayTime;                // 启动充电延迟时间

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode;
    }

    public int getIsStartChargingStatus() {
        return isStartChargingStatus;
    }

    public void setIsStartChargingStatus(int isStartChargingStatus) {
        this.isStartChargingStatus = isStartChargingStatus;
    }

    public String getIsStartChargingStatusDesc() {
        return isStartChargingStatusDesc;
    }

    public void setIsStartChargingStatusDesc(String isStartChargingStatusDesc) {
        this.isStartChargingStatusDesc = isStartChargingStatusDesc;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

}
