package com.holley.wxcharging.model.def.rs;

import java.util.List;

import com.holley.wxcharging.model.def.StationNearby;

/**
 * @author LHP
 */
public class StationNearbyRs extends PageBase {

    public StationNearbyRs(int totalProperty, int pageIndex, int limit) {
        super(totalProperty, pageIndex, limit);
    }

    private List<StationNearby> datas;

    public List<StationNearby> getDatas() {
        return datas;
    }

    public void setDatas(List<StationNearby> datas) {
        this.datas = datas;
    }

}
