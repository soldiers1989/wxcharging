package com.holley.wxcharging.model.def.rs;

import java.math.BigDecimal;

/**
 * 启动充电rs
 * 
 * @author sc
 */
public class IsStopChargingRs {

    private String     pileCode;                // 充电桩编码
    private String     tradeNo;                 // 订单编号
    private int        chaLen;                  // 充电时长
    private int        bikeChaLen;              // 预计充电时长
    private BigDecimal chaMoney;                // 充电金额
    private double     chaPower;                // 充电电量
    private String     startTime;               // 充电开始时间
    private String     endTime;                 // 充电结束时间
    private int        isStopChargingStatus;
    private String     isStopChargingStatusDesc;
    private int        paymentId;               // 订单ID

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

    public int getIsStopChargingStatus() {
        return isStopChargingStatus;
    }

    public void setIsStopChargingStatus(int isStopChargingStatus) {
        this.isStopChargingStatus = isStopChargingStatus;
    }

    public String getIsStopChargingStatusDesc() {
        return isStopChargingStatusDesc;
    }

    public void setIsStopChargingStatusDesc(String isStopChargingStatusDesc) {
        this.isStopChargingStatusDesc = isStopChargingStatusDesc;
    }

    public int getChaLen() {
        return chaLen;
    }

    public void setChaLen(int chaLen) {
        this.chaLen = chaLen;
    }

    public BigDecimal getChaMoney() {
        return chaMoney;
    }

    public void setChaMoney(BigDecimal chaMoney) {
        this.chaMoney = chaMoney;
    }

    public double getChaPower() {
        return chaPower;
    }

    public void setChaPower(double chaPower) {
        this.chaPower = chaPower;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBikeChaLen() {
        return bikeChaLen;
    }

    public void setBikeChaLen(int bikeChaLen) {
        this.bikeChaLen = bikeChaLen;
    }

}
