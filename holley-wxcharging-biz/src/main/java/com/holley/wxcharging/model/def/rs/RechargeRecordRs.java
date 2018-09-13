package com.holley.wxcharging.model.def.rs;

import java.math.BigDecimal;
import java.util.List;

import com.holley.wxcharging.model.def.vo.RechargeRecordVo;

/**
 * 充值记录
 * 
 * @author sc
 */
public class RechargeRecordRs extends PageBase {

    private BigDecimal totalMoney; // 充值总额

    public RechargeRecordRs(int totalProperty, int pageIndex, int limit) {
        super(totalProperty, pageIndex, limit);
    }

    private List<RechargeRecordVo> datas;

    public List<RechargeRecordVo> getDatas() {
        return datas;
    }

    public void setDatas(List<RechargeRecordVo> datas) {
        this.datas = datas;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

}
