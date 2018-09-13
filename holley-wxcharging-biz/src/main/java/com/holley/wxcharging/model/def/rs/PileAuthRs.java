package com.holley.wxcharging.model.def.rs;

import java.math.BigDecimal;
import java.util.List;

import com.holley.wxcharging.model.def.ChargeMode;

/**
 * 设备认证rs
 * 
 * @author sc
 */
public class PileAuthRs extends FailReasonBase {

    private String           pileCode;   // 桩编码
    private String           pileName;   // 桩名称
    private String           stationName; // 站名称
    private int              pileToType; // 1.电动汽车2.电动自行车
    private String           address;    // 地址
    private List<ChargeMode> chargeModes;
    private BigDecimal       usableMoney; // 账户余额
    private int              pileType;   // 1.交流2.直流

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode;
    }

    public List<ChargeMode> getChargeModes() {
        return chargeModes;
    }

    public void setChargeModes(List<ChargeMode> chargeModes) {
        this.chargeModes = chargeModes;
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getPileToType() {
        return pileToType;
    }

    public void setPileToType(int pileToType) {
        this.pileToType = pileToType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getUsableMoney() {
        return usableMoney;
    }

    public void setUsableMoney(BigDecimal usableMoney) {
        this.usableMoney = usableMoney;
    }

    public int getPileType() {
        return pileType;
    }

    public void setPileType(int pileType) {
        this.pileType = pileType;
    }

}
