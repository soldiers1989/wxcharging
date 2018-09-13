package com.holley.wxcharging.model.def.rs;

import java.util.List;

import com.holley.wxcharging.model.def.vo.ChargingRecordVo;

/**
 * 订单管理（充电记录）
 * 
 * @author sc
 */
public class ChargingRecordRs extends PageBase {

    public ChargingRecordRs(int totalProperty, int pageIndex, int limit) {
        super(totalProperty, pageIndex, limit);
    }

    private List<ChargingRecordVo> datas;

    public List<ChargingRecordVo> getDatas() {
        return datas;
    }

    public void setDatas(List<ChargingRecordVo> datas) {
        this.datas = datas;
    }

}
