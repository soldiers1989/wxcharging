package com.holley.wxcharging.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.model.bus.BusBikePaymentExample;
import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.bus.BusRecharge;
import com.holley.wxcharging.model.bus.BusRechargeExample;
import com.holley.wxcharging.model.def.rs.ChargingRecordDetailRs;
import com.holley.wxcharging.model.def.rs.StatChargingInfoRs;
import com.holley.wxcharging.model.def.vo.ChargingRecordVo;
import com.holley.wxcharging.model.def.vo.RechargeRecordVo;

/**
 * 数据相关service stat record
 * 
 * @author sc
 */
public interface DataService {

    List<BusPayment> selectPaymentByExample(BusPaymentExample example);

    List<BusRecharge> selectRechargeByExample(BusRechargeExample example);

    List<BusBikePayment> selectBikePaymentByExample(BusBikePaymentExample example);

    int insertRecharge(BusRecharge record);

    int insertBikePayment(BusBikePayment record);

    int updateRecharge(BusRecharge record, boolean success);

    int updateBikePayment(BusBikePayment record);

    /**
     * 更新自行车充电订单及账户信息(账户余额付款)
     * 
     * @param record 自行车充电订单
     * @param pay true 订单支付, false 订单退款
     * @return
     */
    int updateBikePaymentWithAccount(BusBikePayment record, boolean pay);

    BusAccount selectAccountByPrimaryKey(Integer userId);

    ChargingRecordDetailRs selectPaymentDetailByPrimaryKey(Integer recordID);

    int updatePayment(BusPayment record);

    int updatePaymentWithAccount(BusPayment record);

    List<ChargingRecordVo> selectPaymentRecordByPage(Map<String, Object> param);

    List<RechargeRecordVo> selectRechargeRecordByPage(Map<String, Object> param);

    StatChargingInfoRs selectChargingStatByUserId(Integer userid);

    BigDecimal countRechargeRecord(Map<String, Object> param);
}
