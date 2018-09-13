package com.holley.wxcharging.model.def.rs;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.common.constants.ChargePayStatusEnum;
import com.holley.wxcharging.common.constants.PayWayEnum;
import com.holley.wxcharging.common.constants.UseToTypeEnum;

/**
 * 订单详细信息
 * 
 * @author sc
 */
public class ChargingRecordDetailRs {

    private String     tradeNo;    // 订单编号
    private String     stationName; // 充电站名称
    private String     pileName;   // 桩名称
    private BigDecimal chaMoney;   // 充电金额
    private int        chaLen;     // 充电时长（分）
    private int        payStatus;  // 1.未缴费2.缴费中3.缴费成功4.缴费失败
    private int        pileToType; // 1.电动汽车2.电动自行车
    private Date       start;      // 充电开始时间
    private Date       end;        // 充电结束时间
    private int        bikeChaLen; // 预计时长
    private int        payWay;     // 支付方式

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public BigDecimal getChaMoney() {
        return chaMoney;
    }

    public void setChaMoney(BigDecimal chaMoney) {
        this.chaMoney = chaMoney;
    }

    public int getChaLen() {
        return chaLen;
    }

    public void setChaLen(int chaLen) {
        this.chaLen = chaLen;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusDesc() {
        if (UseToTypeEnum.CAR.getValue() == pileToType) {
            return ChargePayStatusEnum.getText(payStatus);
        } else {
            return BikePayStatusEnum.getText(payStatus);
        }

    }

    public String getPayWayDesc() {
        String desc = PayWayEnum.getText(payWay);
        return desc == null ? "未知" : desc;
    }

    public int getPileToType() {
        return pileToType;
    }

    public void setPileToType(int pileToType) {
        this.pileToType = pileToType;
    }

    public String getChaStartTime() {
        return DateUtil.DateToStr(start, DateUtil.TIME_LONG);
    }

    public String getChaEndTime() {
        return DateUtil.DateToStr(end, DateUtil.TIME_LONG);
    }

    public int getBikeChaLen() {
        return bikeChaLen;
    }

    public void setBikeChaLen(int bikeChaLen) {
        this.bikeChaLen = bikeChaLen;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

}
