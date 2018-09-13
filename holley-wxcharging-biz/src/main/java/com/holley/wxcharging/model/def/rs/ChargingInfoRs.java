package com.holley.wxcharging.model.def.rs;

import java.math.BigDecimal;

/**
 * 充电过程rs
 * 
 * @author sc
 */
public class ChargingInfoRs {

    private String     pileCode;      // 桩编码
    private String     tradeNo;       // 订单编号
    private int        pileStatus;    // 桩状态
    private String     pileStatusDesc; // 桩状态描述
    private int        chaLen;        // 充电时长
    private int        bikeChaLen;    // 预计充电时长
    private BigDecimal chaMoney;      // 充电金额
    private double     chaPower;      // 充电电量
    private String     startTime;     // 充电开始时间
    private double     soc;           // 电量百分比
    private double     currentA;      // 电流
    private double     currentV;      // 电压
    private BigDecimal elecMoney;     // 充电金额
    private BigDecimal serviceMoney;  // 服务费金额

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public double getSoc() {
        return soc;
    }

    public void setSoc(double soc) {
        this.soc = soc;
    }

    public double getCurrentA() {
        return currentA;
    }

    public void setCurrentA(double currentA) {
        this.currentA = currentA;
    }

    public double getCurrentV() {
        return currentV;
    }

    public void setCurrentV(double currentV) {
        this.currentV = currentV;
    }

    public BigDecimal getElecMoney() {
        return elecMoney;
    }

    public void setElecMoney(BigDecimal elecMoney) {
        this.elecMoney = elecMoney;
    }

    public BigDecimal getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(BigDecimal serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public int getBikeChaLen() {
        return bikeChaLen;
    }

    public void setBikeChaLen(int bikeChaLen) {
        this.bikeChaLen = bikeChaLen;
    }

}
