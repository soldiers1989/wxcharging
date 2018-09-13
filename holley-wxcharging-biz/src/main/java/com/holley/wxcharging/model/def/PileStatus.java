package com.holley.wxcharging.model.def;

import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.PileStatusEnum;

/**
 * @author LHP
 */
public class PileStatus {

    private Integer id;
    private String  pileCode;
    private String  pileName;
    private Short   status;
    private Short   type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPileCode() {
        return pileCode;
    }

    public void setPileCode(String pileCode) {
        this.pileCode = pileCode;
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getStatusText() {
        return PileStatusEnum.getText(this.status);
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getTypeText() {
        return ChargePowerTypeEnum.getText(this.type);
    }

}
