package com.holley.wxcharging.dao.pob;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.wxcharging.model.def.StationNearby;
import com.holley.wxcharging.model.pob.PobChargingStation;
import com.holley.wxcharging.model.pob.PobChargingStationExample;

public interface PobChargingStationMapper {

    int countByExample(PobChargingStationExample example);

    int deleteByExample(PobChargingStationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PobChargingStation record);

    int insertSelective(PobChargingStation record);

    List<PobChargingStation> selectByExample(PobChargingStationExample example);

    PobChargingStation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PobChargingStation record, @Param("example") PobChargingStationExample example);

    int updateByExample(@Param("record") PobChargingStation record, @Param("example") PobChargingStationExample example);

    int updateByPrimaryKeySelective(PobChargingStation record);

    int updateByPrimaryKey(PobChargingStation record);

    List<StationNearby> selectNearbyStationByPage(Map<String, Object> param);
}
