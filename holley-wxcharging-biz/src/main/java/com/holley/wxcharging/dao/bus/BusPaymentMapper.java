package com.holley.wxcharging.dao.bus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.def.rs.ChargingRecordDetailRs;
import com.holley.wxcharging.model.def.rs.StatChargingInfoRs;
import com.holley.wxcharging.model.def.vo.ChargingRecordVo;

public interface BusPaymentMapper {

    int countByExample(BusPaymentExample example);

    int deleteByExample(BusPaymentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusPayment record);

    int insertSelective(BusPayment record);

    List<BusPayment> selectByExample(BusPaymentExample example);

    BusPayment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusPayment record, @Param("example") BusPaymentExample example);

    int updateByExample(@Param("record") BusPayment record, @Param("example") BusPaymentExample example);

    int updateByPrimaryKeySelective(BusPayment record);

    int updateByPrimaryKey(BusPayment record);

    List<ChargingRecordVo> selectPaymentRecordByPage(Map<String, Object> param);

    ChargingRecordDetailRs selectPaymentDetailByPrimaryKey(Integer recordID);

    StatChargingInfoRs selectChargingStatByUserId(Integer userid);
    
    int countUnpayment(Map<String,Object> param);
}
