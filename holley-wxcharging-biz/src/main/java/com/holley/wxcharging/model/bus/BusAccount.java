package com.holley.wxcharging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.JsonUtil;

/**
 * @author LHP
 */
public class BusAccount {

    private Integer    userId;

    private Short      status;

    private BigDecimal totalMoney;

    private BigDecimal usableMoney;

    private BigDecimal freezeMoney;

    private Date       updateTime;

    @Override
    public String toString() {
        return JsonUtil.bean2json(this);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getUsableMoney() {
        return usableMoney;
    }

    public void setUsableMoney(BigDecimal usableMoney) {
        this.usableMoney = usableMoney;
    }

    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
