package com.holley.wxcharging.service;

import java.util.List;
import java.util.Map;

import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.def.StationNearby;
import com.holley.wxcharging.model.def.vo.BusChargeRuleVo;
import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import com.holley.wxcharging.model.pob.PobChargingStation;

/**
 * 充电相关service
 * 
 * @author sc
 */
public interface ChargingService {

    PobChargingStation selectChargingStationByPrimaryKey(Integer id);

    List<PobChargingPile> selectChargingPileByExample(PobChargingPileExample example);

    List<BusPayment> selectPaymentByExample(BusPaymentExample example);

    int countPaymentByExample(BusPaymentExample example);

    List<StationNearby> selectNearbyStationByPage(Map<String, Object> param);

    /**
     * 根据条件查询收费规则
     * 
     * @param param
     * @return
     */
    BusChargeRuleVo selectChargeRuleModelByMap(Map<String, Object> param);
    
    int countUnpayment(Map<String,Object> param);

}
