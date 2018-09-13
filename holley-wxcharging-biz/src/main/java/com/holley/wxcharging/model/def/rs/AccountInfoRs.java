package com.holley.wxcharging.model.def.rs;

import java.math.BigDecimal;

/**
 * 账户余额信息
 * 
 * @author sc
 */
public class AccountInfoRs {

    private BigDecimal usableMoney; // 账户余额

    public BigDecimal getUsableMoney() {
        return usableMoney;
    }

    public void setUsableMoney(BigDecimal usableMoney) {
        this.usableMoney = usableMoney;
    }

}
