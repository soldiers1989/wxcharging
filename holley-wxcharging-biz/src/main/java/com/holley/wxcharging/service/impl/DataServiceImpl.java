package com.holley.wxcharging.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.wxcharging.common.constants.AccountLogTypeEnum;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.common.constants.ChargePayStatusEnum;
import com.holley.wxcharging.common.constants.PayWayEnum;
import com.holley.wxcharging.dao.bus.BusAccountMapper;
import com.holley.wxcharging.dao.bus.BusBikePaymentMapper;
import com.holley.wxcharging.dao.bus.BusPaymentMapper;
import com.holley.wxcharging.dao.bus.BusRechargeMapper;
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
import com.holley.wxcharging.service.DataService;
import com.holley.wxcharging.util.AccountLogUtil;

public class DataServiceImpl implements DataService {

    @Resource
    private BusPaymentMapper     busPaymentMapper;
    @Resource
    private BusRechargeMapper    busRechargeMapper;
    @Resource
    private BusAccountMapper     busAccountMapper;
    @Resource
    private BusBikePaymentMapper busBikePaymentMapper;

    @Override
    public List<BusPayment> selectPaymentByExample(BusPaymentExample example) {
        return busPaymentMapper.selectByExample(example);
    }

    @Override
    public List<BusRecharge> selectRechargeByExample(BusRechargeExample example) {
        return busRechargeMapper.selectByExample(example);
    }

    @Override
    public List<BusBikePayment> selectBikePaymentByExample(BusBikePaymentExample example) {
        return busBikePaymentMapper.selectByExample(example);
    }

    @Override
    public int insertRecharge(BusRecharge record) {
        return busRechargeMapper.insertSelective(record);
    }

    @Override
    public int insertBikePayment(BusBikePayment record) {
        return busBikePaymentMapper.insertSelective(record);
    }

    @Override
    public int updateRecharge(BusRecharge recharge, boolean success) {
        if (success) {
            recharge.setStatus(RechargeStatusEnum.SUCCESS.getShortValue());
        } else {
            recharge.setStatus(RechargeStatusEnum.FAILURE.getShortValue());
        }
        int resultRecharge = busRechargeMapper.updateByPrimaryKeySelective(recharge);
        // 更新账户余额
        BusAccount account = busAccountMapper.selectByPrimaryKey(recharge.getUserId());
        account.setUsableMoney(account.getUsableMoney().add(recharge.getMoney()));
        account.setTotalMoney(account.getTotalMoney().add(recharge.getMoney()));
        int resultAccount = busAccountMapper.updateByPrimaryKeySelective(account);

        // result 在此情况下只可能是1(成功)或0(失败)
        int resultAll = resultRecharge * resultAccount;
        if (resultAll > 0) {
            AccountLogUtil.insertAccountLog(account, null, recharge.getId(), AccountLogTypeEnum.RECHARGE, recharge.getMoney(), null, null);
        }
        return resultAll;
    }

    @Override
    public int updateBikePayment(BusBikePayment record) {
        return busBikePaymentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateBikePaymentWithAccount(BusBikePayment record, boolean pay) {
        int resultAccount = 0, resultUpdate = 0;
        BusAccount account = busAccountMapper.selectByPrimaryKey(record.getUserId());
        account.setUpdateTime(new Date());
        AccountLogTypeEnum accountLogType = null;
        if (pay) {
            accountLogType = AccountLogTypeEnum.ACC_BIKE_CHARGING;
            // 支付
            account.setTotalMoney(account.getTotalMoney().subtract(record.getMoney()));
            account.setUsableMoney(account.getUsableMoney().subtract(record.getMoney()));
            resultAccount = busAccountMapper.updateByPrimaryKeySelective(account);

            record.setStatus(BikePayStatusEnum.PAID_UNCHARGED.getShortValue());
            record.setUpdateTime(new Date());
            resultUpdate = busBikePaymentMapper.updateByPrimaryKeySelective(record);
        } else {
            accountLogType = AccountLogTypeEnum.ACC_BACK_BIKE_CHARGING;
            // 退款
            // 退回余额
            account.setTotalMoney(account.getTotalMoney().add(record.getMoney()));
            account.setUsableMoney(account.getUsableMoney().add(record.getMoney()));
            resultAccount = busAccountMapper.updateByPrimaryKeySelective(account);

            // 更新订单状态
            record.setStatus(BikePayStatusEnum.REFUND.getShortValue());
            record.setUpdateTime(new Date());
            resultUpdate = busBikePaymentMapper.updateByPrimaryKeySelective(record);
        }
        int resultAll = resultAccount * resultUpdate;
        if (resultAll > 0) {
            AccountLogUtil.insertAccountLog(account, null, record.getId(), accountLogType, record.getMoney(), null, null);
        }
        return resultAll;
    }

    @Override
    public BusAccount selectAccountByPrimaryKey(Integer userId) {
        return busAccountMapper.selectByPrimaryKey(userId);
    }

    @Override
    public ChargingRecordDetailRs selectPaymentDetailByPrimaryKey(Integer recordID) {
        return busPaymentMapper.selectPaymentDetailByPrimaryKey(recordID);
    }

    @Override
    public int updatePayment(BusPayment record) {
        return busPaymentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updatePaymentWithAccount(BusPayment record) {
        int resultAccount = 0, resultUpdate = 0;
        BusAccount account = busAccountMapper.selectByPrimaryKey(record.getUserId());
        // 扣款
        account.setTotalMoney(account.getTotalMoney().subtract(record.getShouldMoney()));
        account.setUsableMoney(account.getUsableMoney().subtract(record.getShouldMoney()));
        resultAccount = busAccountMapper.updateByPrimaryKeySelective(account);
        // 更新订单状态
        record.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
        record.setPayWay(PayWayEnum.ACCOUNT.getShortValue());
        record.setUpdateTime(new Date());
        resultUpdate = busPaymentMapper.updateByPrimaryKeySelective(record);
        int resultAll = resultAccount * resultUpdate;
        if (resultAll > 0) {
            AccountLogUtil.insertAccountLog(account, null, record.getId(), AccountLogTypeEnum.ACC_CHARGING, record.getShouldMoney(), null, null);
        }
        return resultAll;
    }

    @Override
    public List<ChargingRecordVo> selectPaymentRecordByPage(Map<String, Object> param) {
        return busPaymentMapper.selectPaymentRecordByPage(param);
    }

    @Override
    public List<RechargeRecordVo> selectRechargeRecordByPage(Map<String, Object> param) {
        return busRechargeMapper.selectRechargeRecordByPage(param);
    }

    @Override
    public StatChargingInfoRs selectChargingStatByUserId(Integer userid) {
        return busPaymentMapper.selectChargingStatByUserId(userid);
    }

    @Override
    public BigDecimal countRechargeRecord(Map<String, Object> param) {
        return busRechargeMapper.countRechargeRecord(param);
    }

}
