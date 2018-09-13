package com.holley.wxcharging.model.def.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.common.constants.ChargePayStatusEnum;
import com.holley.wxcharging.common.constants.UseToTypeEnum;

/**
 * 充电记录vo
 * 
 * @author sc
 */
public class ChargingRecordVo {

    private Integer    chargingRecordId;// 记录ID
    private String     tradeNo;         // 订单编号
    private String     stationName;     // 充电站名称
    private String     pileName;        // 充电桩名称
    private Date       date;            // 数据时间
    private BigDecimal chaMoney;        // 充电金额
    private int        payStatus;       // 支付状态
    private int chargeType;
    public Integer getChargingRecordId() {
        return chargingRecordId;
    }

    public void setChargingRecordId(Integer chargingRecordId) {
        this.chargingRecordId = chargingRecordId;
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

    public String getDateTime() {
        return DateUtil.DateToStr(date, DateUtil.TIME_LONG);
    }

    public BigDecimal getChaMoney() {
        return chaMoney;
    }

    public void setChaMoney(BigDecimal chaMoney) {
        this.chaMoney = chaMoney;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatusDesc() {
    	if(UseToTypeEnum.CAR.getValue() == chargeType) {
    		 return ChargePayStatusEnum.getText(payStatus);
    	}else {
    		 return BikePayStatusEnum.getText(payStatus);
    	}
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

	public int getChargeType() {
		return chargeType;
	}

	public void setChargeType(int chargeType) {
		this.chargeType = chargeType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
