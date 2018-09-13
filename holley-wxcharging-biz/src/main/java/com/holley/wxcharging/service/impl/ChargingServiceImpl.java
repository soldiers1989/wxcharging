package com.holley.wxcharging.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.holley.wxcharging.dao.bus.BusChargeRuleMapper;
import com.holley.wxcharging.dao.bus.BusPaymentMapper;
import com.holley.wxcharging.dao.pob.PobChargingPileMapper;
import com.holley.wxcharging.dao.pob.PobChargingStationMapper;
import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.def.StationNearby;
import com.holley.wxcharging.model.def.vo.BusChargeRuleVo;
import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import com.holley.wxcharging.model.pob.PobChargingStation;
import com.holley.wxcharging.service.ChargingService;

public class ChargingServiceImpl implements ChargingService {

    @Resource
    private PobChargingPileMapper    pobChargingPileMapper;
    @Resource
    private BusPaymentMapper         busPaymentMapper;
    @Resource
    private PobChargingStationMapper pobChargingStationMapper;
    @Resource
    private BusChargeRuleMapper      busChargeRuleMapper;

    @Override
    public PobChargingStation selectChargingStationByPrimaryKey(Integer id) {
        return pobChargingStationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PobChargingPile> selectChargingPileByExample(PobChargingPileExample example) {
        return pobChargingPileMapper.selectByExample(example);
    }

    @Override
    public List<BusPayment> selectPaymentByExample(BusPaymentExample example) {
        return busPaymentMapper.selectByExample(example);
    }

    @Override
    public int countPaymentByExample(BusPaymentExample example) {
        return busPaymentMapper.countByExample(example);
    }

    @Override
    public List<StationNearby> selectNearbyStationByPage(Map<String, Object> param) {
        return pobChargingStationMapper.selectNearbyStationByPage(param);
    }

    @Override
    public BusChargeRuleVo selectChargeRuleModelByMap(Map<String, Object> param) {
        return busChargeRuleMapper.selectChargeRuleModelByMap(param);
    }

	@Override
	public int countUnpayment(Map<String, Object> param) {
		return busPaymentMapper.countUnpayment(param);
	}

}
