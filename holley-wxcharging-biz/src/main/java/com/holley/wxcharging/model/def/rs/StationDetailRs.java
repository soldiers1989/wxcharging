package com.holley.wxcharging.model.def.rs;

import java.util.List;

import com.holley.wxcharging.model.def.PileStatus;

/**
 * @author LHP
 */
public class StationDetailRs extends FailReasonBase {

    private String           stationName;
    private String           address;
    private String           longtitude;
    private String           latitude;
    private String           phone;
    private List<PileStatus> datas;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<PileStatus> getDatas() {
        return datas;
    }

    public void setDatas(List<PileStatus> datas) {
        this.datas = datas;
    }

}
