package com.holley.wxcharging.model.def.rs;

/**
 * 启动充电rs
 * 
 * @author sc
 */
public class StartChargingRs extends FailReasonBase {

    private String tradeNo;  // 订单编号
    private String pileCode; // 充电桩编码
    private long   delayTime; // 启动充电延迟时间

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

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

}
