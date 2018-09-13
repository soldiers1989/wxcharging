package com.holley.wxcharging.model.def.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;
import com.holley.wxcharging.model.bus.BusChargeRule;

public class BusChargeRuleVo extends BusChargeRule {

    private Integer    pileId;
    private Date       activeTime;
    private BigDecimal chargeFee;
    private BigDecimal parkFee;
    private BigDecimal serviceFee;

    public Integer getPileId() {
        return pileId;
    }

    public void setPileId(Integer pileId) {
        this.pileId = pileId;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public BigDecimal getChargeFee() {
        if (chargeFee != null) {
            chargeFee = chargeFee.setScale(4, BigDecimal.ROUND_DOWN);
        }
        return chargeFee;
    }

    public void setChargeFee(BigDecimal chargeFee) {
        this.chargeFee = chargeFee;
    }

    public BigDecimal getParkFee() {
        if (parkFee != null) {
            parkFee = parkFee.setScale(4, BigDecimal.ROUND_DOWN);
        }
        return parkFee;
    }

    public void setParkFee(BigDecimal parkFee) {
        this.parkFee = parkFee;
    }

    public BigDecimal getServiceFee() {
        if (serviceFee != null) {
            serviceFee = serviceFee.setScale(4, BigDecimal.ROUND_DOWN);
        }
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getActiveTimeDesc() {
        String result = "";
        if (activeTime != null) {
            result = DateUtil.DateToStr(activeTime, "yyyy/MM/dd");
        }
        return result;
    }
}
