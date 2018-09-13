package com.holley.wxcharging.action.charging;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargeModeEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.jms.MessageSendService;
import com.holley.common.rocketmq.charging.MsgChargeStart;
import com.holley.common.rocketmq.charging.MsgChargeStop;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.cache.WxchargingGlobals;
import com.holley.wxcharging.common.constants.BikeFeeTypeEnum;
import com.holley.wxcharging.common.constants.HasUnpaidFeeEnum;
import com.holley.wxcharging.common.constants.IsStartChargingEnum;
import com.holley.wxcharging.common.constants.IsStopChargingEnum;
import com.holley.wxcharging.common.constants.UseToTypeEnum;
import com.holley.wxcharging.common.failReason.PileAuthFailReasonEnum;
import com.holley.wxcharging.common.failReason.StartChargingFailReasonEnum;
import com.holley.wxcharging.common.failReason.StopChargingFailReasonEnum;
import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.def.ChargeMode;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.def.rs.ChargingInfoRs;
import com.holley.wxcharging.model.def.rs.HasUnpaidFeeRs;
import com.holley.wxcharging.model.def.rs.IsStartChargingRs;
import com.holley.wxcharging.model.def.rs.IsStopChargingRs;
import com.holley.wxcharging.model.def.rs.PileAuthRs;
import com.holley.wxcharging.model.def.rs.PileStatusRs;
import com.holley.wxcharging.model.def.rs.StartChargingRs;
import com.holley.wxcharging.model.def.rs.StopChargingRs;
import com.holley.wxcharging.model.def.vo.BusChargeRuleVo;
import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import com.holley.wxcharging.model.pob.PobChargingStation;
import com.holley.wxcharging.service.AccountService;
import com.holley.wxcharging.service.ChargingService;
import com.holley.wxcharging.util.WxchargingCacheUtil;
import com.holley.wxcharging.util.WxchargingLocalUtil;
import com.holley.wxcharging.util.WxchargingUtil;

/**
 * 充电相关ACTION
 * 
 * @author sc
 */
public class ChargingAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private final static Logger logger           = Logger.getLogger(ChargingAction.class);
    @Resource
    private ChargingService     chargingService;
    @Resource
    private MessageSendService  messageSendService;
    @Resource
    private AccountService      accountService;

    /**
     * 充电页面
     * 
     * @return
     */
    public String charging() {
        return SUCCESS;
    }

    /**
     * 充电过程页面
     */

    public String chargingProcess() {
        return SUCCESS;
    }

    /**
     * 充电过程数据页面
     */

    public String chargingProcessData() {
        return SUCCESS;
    }

    /**
     * 扫码/输入编号 设备认证
     * 
     * @return
     */
    public String queryPileAuth() {
        WxChargingUser wxChargingUser = getUser();
        String pileCode = getParameter("pileCode");// 桩编码
        PileAuthRs rs = new PileAuthRs();
        rs.setPileCode(pileCode);
        wxChargingResultBean.setData(rs);
        BusAccount account = accountService.selectAccountByPrimaryKey(wxChargingUser.getUserId());
        rs.setUsableMoney(account.getUsableMoney());
        // PobChargingPile pile = getPileByPileCode(pileCode);
        PobChargingPile pile = null;
        PobChargingPileExample emp = new PobChargingPileExample();
        PobChargingPileExample.Criteria cr = emp.createCriteria();
        cr.andPileCodeEqualTo(pileCode);
        List<PobChargingPile> list = chargingService.selectChargingPileByExample(emp);
        if (list != null && !list.isEmpty()) {
            pile = list.get(0);
        }
        if (pile == null) {
            rs.setFailReason(PileAuthFailReasonEnum.UNKNOW_PILE.getValue());
            rs.setFailReasonDesc(PileAuthFailReasonEnum.UNKNOW_PILE.getText());
            return SUCCESS;
        }
        PobChargingStation station = chargingService.selectChargingStationByPrimaryKey(pile.getStationId());
        rs.setPileToType(pile.getPileToType());
        rs.setStationName(station.getStationName());
        rs.setPileName(pile.getPileName());
        rs.setAddress(station.getAddress());
        rs.setPileType(pile.getChaWay());
        PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
        if (pileStatusBean == null) {
            rs.setFailReason(PileAuthFailReasonEnum.OFFLINE_PILE.getValue());
            rs.setFailReasonDesc(PileAuthFailReasonEnum.OFFLINE_PILE.getText());
            return SUCCESS;
        }
        PileStatusEnum pileStatus = pileStatusBean.getStatus();
        Date scandate = pileStatusBean.getScantime();
        Integer userid = pileStatusBean.getScanuserid();
        if (userid != null && scandate != null && wxChargingUser.getUserId() != userid) {
            if (!WxchargingUtil.isTimeOut(scandate.getTime(), WxchargingGlobals.MAX_SCAN_LOCK)) {
                rs.setFailReason(PileAuthFailReasonEnum.HOLD_PILE.getValue());
                rs.setFailReasonDesc(PileAuthFailReasonEnum.HOLD_PILE.getText());
                return SUCCESS;
            }
        }
        if (PileStatusEnum.OFFLINE == pileStatus) {
            rs.setFailReason(PileAuthFailReasonEnum.OFFLINE_PILE.getValue());
            rs.setFailReasonDesc(PileAuthFailReasonEnum.OFFLINE_PILE.getText());
            return SUCCESS;
        } else if (PileStatusEnum.FAULT == pileStatus) {
            rs.setFailReason(PileAuthFailReasonEnum.FAIL_PILE.getValue());
            rs.setFailReasonDesc(PileAuthFailReasonEnum.FAIL_PILE.getText());
            return SUCCESS;
        } else if (PileStatusEnum.CHARGING == pileStatus) {
            rs.setFailReason(PileAuthFailReasonEnum.CHARGING.getValue());
            rs.setFailReasonDesc(PileAuthFailReasonEnum.CHARGING.getText());
            return SUCCESS;
        }
        rs.setChargeModes(getChargeModels(pile));
        if (rs.getChargeModes() == null || rs.getChargeModes().isEmpty()) {
            rs.setFailReason(PileAuthFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("设备收费规则未知");
            return SUCCESS;
        }
        logger.info("queryPileAuth：--->" + "桩：" + pileCode + "设备认证成功。。。");
        pileStatusBean.setScantime(new Date());
        pileStatusBean.setScanuserid(wxChargingUser.getUserId());
        WxchargingCacheUtil.refreshPileStatusBean(pileStatusBean);
        return SUCCESS;
    }

    /**
     * 启动充电（改方法只适用于后付费模式）
     * 
     * @return
     */
    public String queryStartCharging() {
        WxChargingUser wxChargingUser = getUser();
        String pileCode = getParameter("pileCode");// 桩编码
        double value = NumberUtils.toDouble(getParameter("value"));
        ChargeModeEnum chargeMode = ChargeModeEnum.getEnmuByValue(getParamInt("chargeMode"));

        StartChargingRs rs = new StartChargingRs();
        rs.setPileCode(pileCode);
        wxChargingResultBean.setData(rs);
        PobChargingPile pile = getPileByPileCode(pileCode);
        if (pile == null || !UseToTypeEnum.CAR.getShortValue().equals(pile.getPileToType())) {
            rs.setFailReason(PileAuthFailReasonEnum.UNKNOW_PILE.getValue());
            rs.setFailReasonDesc(PileAuthFailReasonEnum.UNKNOW_PILE.getText());
            return SUCCESS;
        }

        PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
        if (pileStatusBean == null) {
            rs.setFailReason(StartChargingFailReasonEnum.OFFLINE_PILE.getValue());
            rs.setFailReasonDesc(StartChargingFailReasonEnum.OFFLINE_PILE.getText());
            return SUCCESS;
        }
        PileStatusEnum pileStatus = pileStatusBean.getStatus();
        if (PileStatusEnum.OFFLINE == pileStatus) {
            rs.setFailReason(StartChargingFailReasonEnum.OFFLINE_PILE.getValue());
            rs.setFailReasonDesc(StartChargingFailReasonEnum.OFFLINE_PILE.getText());
            return SUCCESS;
        } else if (PileStatusEnum.FAULT == pileStatus) {
            rs.setFailReason(StartChargingFailReasonEnum.FAIL_PILE.getValue());
            rs.setFailReasonDesc(StartChargingFailReasonEnum.FAIL_PILE.getText());
            return SUCCESS;
        } else if (PileStatusEnum.CHARGING == pileStatus) {
            rs.setFailReason(StartChargingFailReasonEnum.CHARGING.getValue());
            rs.setFailReasonDesc(StartChargingFailReasonEnum.CHARGING.getText());
            return SUCCESS;
        }
        // else if (PileStatusEnum.BUSYING != pileStatus) {
        // rs.setFailReason(StartChargingFailReasonEnum.UN_READY.getValue());
        // rs.setFailReasonDesc(StartChargingFailReasonEnum.UN_READY.getText());
        // return SUCCESS;
        // }

        if (chargeMode == null) {
            rs.setFailReason(PileAuthFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("未知收费规则");
            return SUCCESS;
        }

        BusAccount account = accountService.selectAccountByPrimaryKey(wxChargingUser.getUserId());
        if (chargeMode == ChargeModeEnum.BY_AUTO) {
            value = account.getUsableMoney().doubleValue();
            if (value <= 0) {
                rs.setFailReason(PileAuthFailReasonEnum.OTHER.getValue());
                rs.setFailReasonDesc("账户余额不足");
                return SUCCESS;
            }
        } else if (chargeMode == ChargeModeEnum.BY_MONEY) {
            if (value <= 0 || value > account.getUsableMoney().doubleValue()) {
                rs.setFailReason(PileAuthFailReasonEnum.OTHER.getValue());
                rs.setFailReasonDesc("账户余额不足");
                return SUCCESS;
            }
        }

        // 生成订单号
        String tradeNo = StringUtil.AddjustLength(pile.getComAddr(), 12, "0") + DateUtil.DateToLong14Str(new Date()) + StringUtil.zeroPadString(pile.getId().toString(), 6);
        rs.setTradeNo(tradeNo);
        // pileStatusBean.setUserid(wxChargingUser.getUserId());
        pileStatusBean.setUpdatetime(new Date());
        rs.setDelayTime(WxchargingGlobals.MAX_START_CHARGING_DELAY);
        // 启动充电刷新pileStatusBean
        WxchargingCacheUtil.refreshPileStatusBean(pileStatusBean);
        MsgChargeStart startMsg = new MsgChargeStart(pile.getId(), wxChargingUser.getUserId(), wxChargingUser.getPhone(), chargeMode.getShortValue().byteValue(), value, tradeNo);
        messageSendService.sendTopic(startMsg);// 发送消息通知前置启动充电
        logger.info("queryStartCharging：--->" + "桩：" + pileCode + "订单号：" + tradeNo + "启动充电命令下发。。。");
        return SUCCESS;
    }

    /**
     * 停止充电
     * 
     * @return
     */
    public String queryStopCharging() {
        WxChargingUser wxChargingUser = getUser();
        String pileCode = getParameter("pileCode");// 桩编码
        String tradeNo = getParameter("tradeNo");// 订单号
        StopChargingRs rs = new StopChargingRs();
        rs.setPileCode(pileCode);
        rs.setTradeNo(tradeNo);
        wxChargingResultBean.setData(rs);
        PobChargingPile pile = getPileByPileCode(pileCode);
        if (pile == null) {
            rs.setFailReason(StopChargingFailReasonEnum.UNKNOW_PILE.getValue());
            rs.setFailReasonDesc(StopChargingFailReasonEnum.UNKNOW_PILE.getText());
            return SUCCESS;
        }
        PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
        if (pileStatusBean == null) {
            rs.setFailReason(StopChargingFailReasonEnum.OFFLINE_PILE.getValue());
            rs.setFailReasonDesc(StopChargingFailReasonEnum.OFFLINE_PILE.getText());
            return SUCCESS;
        }
        if (wxChargingUser.getUserId() != pileStatusBean.getUserid()) {
            rs.setFailReason(StopChargingFailReasonEnum.OTHER_USER.getValue());
            rs.setFailReasonDesc(StopChargingFailReasonEnum.OTHER_USER.getText());
            return SUCCESS;
        }
        if (!tradeNo.equals(pileStatusBean.getTradeno())) {
            rs.setFailReason(StopChargingFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("订单号有误");
            return SUCCESS;
        }
        PileStatusEnum pileStatus = pileStatusBean.getStatus();
        if (PileStatusEnum.OFFLINE == pileStatus) {
            rs.setFailReason(StopChargingFailReasonEnum.OFFLINE_PILE.getValue());
            rs.setFailReasonDesc(StopChargingFailReasonEnum.OFFLINE_PILE.getText());
            return SUCCESS;
        } else if (PileStatusEnum.FAULT == pileStatus) {
            rs.setFailReason(StopChargingFailReasonEnum.FAIL_PILE.getValue());
            rs.setFailReasonDesc(StopChargingFailReasonEnum.FAIL_PILE.getText());
            return SUCCESS;
        }
        // 状态为充电中才下发停止充电命令
        if (PileStatusEnum.CHARGING == pileStatus) {
            rs.setDelayTime(WxchargingGlobals.MAX_STOP_CHARGING_DELAY);
            // 下发命令
            MsgChargeStop stopMsg = new MsgChargeStop(pile.getId(), wxChargingUser.getUserId(), wxChargingUser.getPhone(), tradeNo);
            messageSendService.sendTopic(stopMsg);// 发送消息通知前置启动充电
            logger.info("queryStopCharging：--->" + "桩：" + pileCode + "订单号：" + tradeNo + "停止充电命令下发。。。");
        }

        return SUCCESS;
    }

    /**
     * 充电过程数据
     * 
     * @return
     */
    public String queryChargingInfo() {
        WxChargingUser wxChargingUser = getUser();
        String pileCode = getParameter("pileCode");// 桩编码
        String tradeNo = getParameter("tradeNo");// 订单编号
        ChargingInfoRs rs = new ChargingInfoRs();
        rs.setTradeNo(tradeNo);
        rs.setPileCode(pileCode);
        wxChargingResultBean.setData(rs);
        logger.info("queryChargingInfo：--->" + "桩：" + pileCode + "订单号：" + tradeNo + "获取充电过程数据。。。");
        if (StringUtil.isNotEmpty(tradeNo) && StringUtil.isNotEmpty(pileCode)) {
            PobChargingPile pile = getPileByPileCode(pileCode);
            if (pile != null) {
                PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
                if (pileStatusBean != null && tradeNo.equals(pileStatusBean.getTradeno())
                    && wxChargingUser.getUserId() == (pileStatusBean.getUserid() != null ? pileStatusBean.getUserid() : 0)) {
                    PileStatusEnum pileStatus = pileStatusBean.getStatus() != null ? pileStatusBean.getStatus() : PileStatusEnum.OFFLINE;

                    if (UseToTypeEnum.BIKE.getShortValue().equals(pile.getPileToType())) {
                        JSONObject info = WxchargingCacheUtil.getBikeChargingInfo(tradeNo);
                        if (info != null) {
                            rs.setChaMoney(info.getBigDecimal("money"));
                            rs.setBikeChaLen(info.getIntValue("chaLen"));
                        }
                    } else {
                        rs.setChaMoney(WxchargingUtil.getDefaultBigDecimal(pileStatusBean.getMoney()));
                    }
                    rs.setChaLen(WxchargingUtil.getDefaultInteger(pileStatusBean.getChalen()));
                    rs.setChaPower(WxchargingUtil.getDefaultDouble(pileStatusBean.getChapower()));
                    rs.setPileStatus(pileStatus.getValue());
                    rs.setPileStatusDesc(pileStatus.getText());
                    rs.setCurrentA(NumberUtils.toDouble(pileStatusBean.getOuti()));
                    rs.setCurrentV(NumberUtils.toDouble(pileStatusBean.getOutv()));
                    rs.setStartTime(DateUtil.DateToLongStr(pileStatusBean.getStarttime()));
                    rs.setElecMoney(WxchargingUtil.getDefaultBigDecimal(pileStatusBean.getChaMoney()));
                    rs.setServiceMoney(WxchargingUtil.getDefaultBigDecimal(pileStatusBean.getServiceMoney()));
                    Double soc = pileStatusBean.getSoc();
                   	soc = soc!=null?soc:0.0;
                    rs.setSoc(soc);
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 请求设备状态
     * 
     * @return
     */
    public String queryPileStatus() {
        String pileCode = getParameter("pileCode");// 桩编码
        PileStatusRs rs = new PileStatusRs();
        rs.setPileCode(pileCode);
        wxChargingResultBean.setData(rs);
        if (StringUtil.isEmpty(pileCode)) {
            rs.setPileStatus(PileStatusEnum.OFFLINE.getValue());
            rs.setPileStatusDesc(PileStatusEnum.OFFLINE.getText());
            return SUCCESS;
        }
        PobChargingPile pile = getPileByPileCode(pileCode);
        if (pile == null) {
            rs.setPileStatus(PileStatusEnum.OFFLINE.getValue());
            rs.setPileStatusDesc(PileStatusEnum.OFFLINE.getText());
            return SUCCESS;
        }
        PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
        if (pileStatusBean == null) {
            rs.setPileStatus(PileStatusEnum.OFFLINE.getValue());
            rs.setPileStatusDesc(PileStatusEnum.OFFLINE.getText());
            return SUCCESS;
        }
        PileStatusEnum pileStatus = pileStatusBean.getStatus() != null ? pileStatusBean.getStatus() : PileStatusEnum.OFFLINE;
        rs.setPileStatus(pileStatus.getValue());
        rs.setPileStatusDesc(pileStatus.getText());
        return SUCCESS;
    }

    /**
     * 判断当前用户是否启动（正在）充电
     * 
     * @return
     */
    public String queryIsStartCharging() {
        WxChargingUser wxChargingUser = getUser();
        String pileCode = getParameter("pileCode");// 桩编码
        String tradeNo = getParameter("tradeNo");// 订单编号
        IsStartChargingRs rs = new IsStartChargingRs();
        rs.setPileCode(pileCode);
        rs.setTradeNo(tradeNo);
        rs.setIsStartChargingStatus(IsStartChargingEnum.UN_CHARGING.getValue());
        rs.setIsStartChargingStatusDesc(IsStartChargingEnum.UN_CHARGING.getText());
        wxChargingResultBean.setData(rs);
        if (StringUtil.isEmpty(pileCode) || StringUtil.isEmpty(tradeNo)) {
            return SUCCESS;
        }
        PobChargingPile pile = getPileByPileCode(pileCode);
        if (pile == null) {
            return SUCCESS;
        }
        PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
        if (pileStatusBean == null) {
            return SUCCESS;
        }
        PileStatusEnum pileStatus = pileStatusBean.getStatus();
        logger.info("queryIsStartCharging：--->" + "桩：" + pileCode + "订单号：" + tradeNo + "桩状态：" + pileStatus);

        if (tradeNo.equals(pileStatusBean.getTradeno()) && (pileStatusBean.getUserid() != null ? pileStatusBean.getUserid() : 0) == wxChargingUser.getUserId()) {
            if (PileStatusEnum.CHARGING == pileStatus) {
                rs.setIsStartChargingStatus(IsStartChargingEnum.CHARGING.getValue());
                rs.setIsStartChargingStatusDesc(IsStartChargingEnum.CHARGING.getText());
            } else if (PileStatusEnum.BUSYING == pileStatus || PileStatusEnum.IDLE == pileStatus) {
                long delay = getDelayTime(pileStatusBean.getStarttime());
                if (delay > 0) {
                    rs.setIsStartChargingStatus(IsStartChargingEnum.RUN_CHARGING.getValue());
                    rs.setIsStartChargingStatusDesc(IsStartChargingEnum.RUN_CHARGING.getText());
                    rs.setDelayTime(delay);
                }

            }
        }
        return SUCCESS;
    }

    /**
     * 判断当前用户是否停止充电
     * 
     * @return
     */
    public String queryIsStopCharging() {
        WxChargingUser wxChargingUser = getUser();
        String pileCode = getParameter("pileCode");// 桩编码
        String tradeNo = getParameter("tradeNo");// 订单编号
        IsStopChargingRs rs = new IsStopChargingRs();
        rs.setPileCode(pileCode);
        rs.setTradeNo(tradeNo);
        rs.setIsStopChargingStatus(IsStopChargingEnum.UN_STOP_CHARGING.getValue());
        rs.setIsStopChargingStatusDesc(IsStopChargingEnum.UN_STOP_CHARGING.getText());
        wxChargingResultBean.setData(rs);
        if (StringUtil.isEmpty(pileCode) || StringUtil.isEmpty(tradeNo)) {
            return SUCCESS;
        }

        BusPayment payment = getPaymentByTradeNoAndUserId(tradeNo, wxChargingUser.getUserId());

        if (payment == null || !ChargeDealStatusEnum.SUCCESS.getShortValue().equals(payment.getDealStatus())) {
            return SUCCESS;
        }
        PobChargingPile pile = getPileByPileCode(pileCode);
        // 该笔订单已结束
        if (ChargeDealStatusEnum.SUCCESS.getShortValue().equals(payment.getDealStatus())) {
            if (UseToTypeEnum.BIKE.getShortValue().equals(pile.getPileToType())) {
                JSONObject info = WxchargingCacheUtil.getBikeChargingInfo(tradeNo);
                if (info != null) {
                    rs.setChaMoney(info.getBigDecimal("money"));
                    rs.setBikeChaLen(info.getIntValue("chaLen"));
                }
                WxchargingCacheUtil.removeBikeChargingInfo(tradeNo);
            } else {
                rs.setChaMoney(WxchargingUtil.getDefaultBigDecimal(payment.getShouldMoney()));
            }
            rs.setIsStopChargingStatus(IsStopChargingEnum.STOP_CHARGING.getValue());
            rs.setIsStopChargingStatusDesc(IsStopChargingEnum.STOP_CHARGING.getText());
            rs.setChaLen(WxchargingUtil.getDefaultInteger(payment.getChaLen()));
            rs.setChaPower(WxchargingUtil.getDefaultDouble(payment.getChaPower()));
            rs.setStartTime(DateUtil.DateToLongStr(payment.getStartTime()));
            rs.setEndTime(DateUtil.DateToLongStr(payment.getEndTime()));
            rs.setPaymentId(payment.getId());
            return SUCCESS;
        }
        // PobChargingPile pile = getPileByPileCode(pileCode);
        // if (pile == null) {
        // return SUCCESS;
        // }
        // PileStatusBean pileStatusBean = WxchargingCacheUtil.returnPileStatusBean(pile.getId());
        // if (pileStatusBean == null) {
        // return SUCCESS;
        // }
        // PileStatusEnum pileStatus = pileStatusBean.getStatus();
        // logger.info("queryIsStopCharging：--->" + "桩：" + pileCode + "订单号：" + tradeNo + "桩状态：" + pileStatus);
        // if (tradeNo.equals(pileStatusBean.getTradeno()) && pileStatusBean.getUserid() == wxChargingUser.getUserId()
        // &&
        // PileStatusEnum.CHARGING == pileStatus) {
        // rs.setIsStopChargingStatus(IsStopChargingEnum.UN_STOP_CHARGING.getValue());
        // rs.setIsStopChargingStatusDesc(IsStopChargingEnum.UN_STOP_CHARGING.getText());
        // return SUCCESS;
        // }
        return SUCCESS;
    }

    /**
     * 查询是否有未激费的充电记录 此查询放在设备认证之前
     * 
     * @return
     */
    public String queryUnpaidFee() {
        WxChargingUser wxChargingUser = getUser();
        HasUnpaidFeeRs rs = new HasUnpaidFeeRs();
        rs.setHasUnpaidFee(HasUnpaidFeeEnum.UN_HAS.getValue());
        rs.setHasUnpaidFeeDesc(HasUnpaidFeeEnum.UN_HAS.getText());
        wxChargingResultBean.setData(rs);

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("pileToType", UseToTypeEnum.CAR.getValue());
        param.put("userId", wxChargingUser.getUserId());
        param.put("dealStatus", ChargeDealStatusEnum.SUCCESS.getValue());
        param.put("payStatus", ChargePayStatusEnum.SUCCESS.getShortValue());
        if (chargingService.countUnpayment(param) > 0) {
            rs.setHasUnpaidFee(HasUnpaidFeeEnum.HAS.getValue());
            rs.setHasUnpaidFeeDesc(HasUnpaidFeeEnum.HAS.getText());
        }
        return SUCCESS;
    }

    private long getDelayTime(Date date) {
        if (date == null) {
            return 0;
        }
        long time = WxchargingGlobals.MAX_START_CHARGING_DELAY - (System.currentTimeMillis() - date.getTime()) / 1000;
        return time > 0 ? time : 0;
    }

    private PobChargingPile getPileByPileCode(String pileCode) {
        if (StringUtil.isEmpty(pileCode)) {
            return null;
        }
        return WxchargingLocalUtil.returnPile(pileCode);
    }

    private BusPayment getPaymentByTradeNoAndUserId(String tradeNo, Integer userId) {
        if (StringUtil.isEmpty(tradeNo)) {
            return null;
        }
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        cr.andUserIdEqualTo(userId);
        List<BusPayment> list = chargingService.selectPaymentByExample(emp);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 充电模式
     * 
     * @return
     */
    private List<ChargeMode> getChargeModels(PobChargingPile pile) {
        List<ChargeMode> list = new ArrayList<ChargeMode>();

        if (UseToTypeEnum.CAR.getShortValue().equals(pile.getPileToType())) {
            ChargeMode m1 = new ChargeMode();
            m1.setName(ChargeModeEnum.BY_AUTO.getText());
            m1.setValue(ChargeModeEnum.BY_AUTO.getValue());
            list.add(m1);
            ChargeMode m2 = new ChargeMode();
            m2.setName(ChargeModeEnum.BY_MONEY.getText());
            m2.setValue(ChargeModeEnum.BY_MONEY.getValue());
            list.add(m2);
        } else if (UseToTypeEnum.BIKE.getShortValue().equals(pile.getPileToType())) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("pileId", pile.getId());
            param.put("active", WhetherEnum.YES.getShortValue());// 时间已激活
            param.put("status", WhetherEnum.YES.getShortValue());// 已生效
            BusChargeRuleVo activeRule = chargingService.selectChargeRuleModelByMap(param);
            double tempFee = 0;
            if (activeRule != null && activeRule.getId() == 1) {
                tempFee = activeRule.getChargeFee().doubleValue();
            } else {
                tempFee = pile.getChargeFee().doubleValue();
            }
            for (BikeFeeTypeEnum type : BikeFeeTypeEnum.values()) {
                ChargeMode m = new ChargeMode();
                double hour = tempFee * type.getValue();
                m.setName(type.getValue() + "元" + hour + "小时");
                m.setValue(type.getValue());
                list.add(m);
            }
        }

        return list;
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }

}
