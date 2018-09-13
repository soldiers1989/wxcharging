package com.holley.wxcharging.model.def;

import java.math.BigDecimal;

import com.holley.wxcharging.model.pob.PobChargingStation;

/**
 * @author LHP
 */
public class StationNearby extends PobChargingStation {

    /**
     * 距离(千米)
     */
    private BigDecimal distance;
    private int        available;
    private int        busy;
    private int        total;

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getBusy() {
        return busy;
    }

    public void setBusy(int busy) {
        this.busy = busy;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

}
