package com.holley.wxcharging.dao.pob;

import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PobChargingPileMapper {
    int countByExample(PobChargingPileExample example);

    int deleteByExample(PobChargingPileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PobChargingPile record);

    int insertSelective(PobChargingPile record);

    List<PobChargingPile> selectByExample(PobChargingPileExample example);

    PobChargingPile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PobChargingPile record, @Param("example") PobChargingPileExample example);

    int updateByExample(@Param("record") PobChargingPile record, @Param("example") PobChargingPileExample example);

    int updateByPrimaryKeySelective(PobChargingPile record);

    int updateByPrimaryKey(PobChargingPile record);
}