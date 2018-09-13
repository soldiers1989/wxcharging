package com.holley.wxcharging.dao.bus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.wxcharging.model.bus.BusRecharge;
import com.holley.wxcharging.model.bus.BusRechargeExample;
import com.holley.wxcharging.model.def.vo.RechargeRecordVo;

/**
 * @author LHP
 */
public interface BusRechargeMapper {

    int countByExample(BusRechargeExample example);

    int deleteByExample(BusRechargeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusRecharge record);

    int insertSelective(BusRecharge record);

    List<BusRecharge> selectByExample(BusRechargeExample example);

    BusRecharge selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusRecharge record, @Param("example") BusRechargeExample example);

    int updateByExample(@Param("record") BusRecharge record, @Param("example") BusRechargeExample example);

    int updateByPrimaryKeySelective(BusRecharge record);

    int updateByPrimaryKey(BusRecharge record);

    List<RechargeRecordVo> selectRechargeRecordByPage(Map<String, Object> param);

    BigDecimal countRechargeRecord(Map<String, Object> param);
}
