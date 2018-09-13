package com.holley.wxcharging.model.def.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;
import com.holley.wxcharging.common.constants.ChargePayStatusEnum;
import com.holley.wxcharging.common.constants.PayWayEnum;

public class RechargeRecordVo {

    private String     tradeNo;
    private BigDecimal rechargeMoney;
    private int        payWay;
    private Date       date;
    private int        rechargeStatus;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getPayWayDesc() {
        return PayWayEnum.getText(payWay);
    }

    public String getRechargeTime() {
        return DateUtil.DateToStr(date, DateUtil.TIME_LONG);
    }

    public int getRechargeStatus() {
        return rechargeStatus;
    }

    public void setRechargeStatus(int rechargeStatus) {
        this.rechargeStatus = rechargeStatus;
    }

    public String getRechargeStatusDesc() {
        return ChargePayStatusEnum.getText(rechargeStatus);
    }

}
