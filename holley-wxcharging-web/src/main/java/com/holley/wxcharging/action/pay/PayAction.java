package com.holley.wxcharging.action.pay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargeModeEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.jms.MessageSendService;
import com.holley.common.rocketmq.charging.MsgChargeStart;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.common.constants.ChargePayStatusEnum;
import com.holley.wxcharging.common.constants.PayWayEnum;
import com.holley.wxcharging.common.constants.RetTypeEnum;
import com.holley.wxcharging.common.constants.TradeTypeEnum;
import com.holley.wxcharging.common.constants.UseToTypeEnum;
import com.holley.wxcharging.common.constants.WxConfig;
import com.holley.wxcharging.common.failReason.AccountRechargeFailReasonEnum;
import com.holley.wxcharging.common.failReason.BikePaymentFailReasonEnum;
import com.holley.wxcharging.common.failReason.PaymentFailReasonEnum;
import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.model.bus.BusBikePaymentExample;
import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.bus.BusRecharge;
import com.holley.wxcharging.model.bus.BusRechargeExample;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.def.WxPaySendData;
import com.holley.wxcharging.model.def.rs.AccountRechargeRs;
import com.holley.wxcharging.model.def.rs.BikePaymentRs;
import com.holley.wxcharging.model.def.rs.PayResultRs;
import com.holley.wxcharging.model.def.rs.PaymentRs;
import com.holley.wxcharging.model.def.rs.TradeNoRs;
import com.holley.wxcharging.model.def.vo.BusChargeRuleVo;
import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import com.holley.wxcharging.service.AccountService;
import com.holley.wxcharging.service.ChargingService;
import com.holley.wxcharging.service.DataService;
import com.holley.wxcharging.util.AlipayConfig;
import com.holley.wxcharging.util.WxchargingCacheUtil;
import com.holley.wxcharging.util.WxchargingLocalUtil;
import com.holley.wxcharging.util.WxchargingPayUtil;
import com.holley.wxcharging.util.WxchargingUtil;

/**
 * 支付相关ACTION
 * 
 * @author LHP
 */
public class PayAction extends BaseAction {

    private static final long   serialVersionUID     = 1L;
    private final static Logger logger               = Logger.getLogger(PayAction.class);
    /**
     * 支付回传参数。充值余额时。
     */
    private final String        PASS_BACK_RECHARGE   = "PASS_BACK_RECHARGE";
    /**
     * 支付回传参数。充电支付时。
     */
    private final String        PASS_BACK_PAYMENT    = "PASS_BACK_PAYMENT";
    /**
     * 支付回传参数。自行车充电。
     */
    private final String        PASS_BACK_BIKE       = "PASS_BACK_BIKE";
    /**
     * 交易成功
     */
    private final String        TENPAY_TRADE_SUCCESS = "SUCCESS";
    /**
     * 微信openid长度
     */
    private final int           WX_OPENID_LENGTH     = 28;
    @Resource
    private DataService         dataService;
    @Resource
    private AccountService      accountService;
    @Resource
    private MessageSendService  messageSendService;
    @Resource
    private ChargingService     chargingService;

    /**
     * 充值页面
     * 
     * @return
     */
    public String initRechargePage() {
        return SUCCESS;
    }

    /**
     * 充电支付页面
     * 
     * @return
     */
    public String initPaymentPage() {
        return SUCCESS;
    }

    /**
     * 充电支付
     * 
     * @return
     */
    public String queryPayment() {
        // return value
        PaymentRs rs = new PaymentRs();
        // parameter
        WxChargingUser user = getUser();
        // parameter
        int way = getParamInt("payWay");
        // parameter check
        if (way == 0) {
            // wrong pay way
            logger.error("支付方式参数错误: " + way);
            rs.setFailReason(PaymentFailReasonEnum.OTHER.getShortValue());
            rs.setFailReasonDesc("支付方式参数错误");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        // parameter
        String tradeNo = getParameter("tradeNo");
        // parameter check
        if (StringUtil.isEmpty(tradeNo)) {
            // null trade number
            logger.error("订单号为空: " + tradeNo);
            rs.setFailReason(PaymentFailReasonEnum.ERROR_TRADER.getShortValue());
            rs.setFailReasonDesc("订单号为空");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }

        // 检查订单状态
        BusPayment payment = WxchargingLocalUtil.returnPayment(tradeNo);
        if (payment == null) {
            // error trade number
            logger.error("订单号不存在: " + tradeNo);
            rs.setFailReason(PaymentFailReasonEnum.ERROR_TRADER.getShortValue());
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        if (!ChargeDealStatusEnum.SUCCESS.getShortValue().equals(payment.getDealStatus())) {
            // 充电状态不是success
            logger.error("充电状态未成功: " + tradeNo);
            rs.setFailReason(PaymentFailReasonEnum.OTHER.getShortValue());
            rs.setFailReasonDesc("充电状态未成功");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        if (ChargePayStatusEnum.SUCCESS.getShortValue().equals(payment.getPayStatus())) {
            // 订单已经支付过
            logger.error("订单已经支付过: " + tradeNo);
            rs.setFailReason(PaymentFailReasonEnum.ALREADY_PAID.getShortValue());
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        // 组回传参数
        JSONObject json = new JSONObject();
        json.put("origin", PASS_BACK_PAYMENT);
        json.put("tradeno", payment.getTradeNo());
        String passParam = null;
        try {
            String str = JSON.toJSONString(json, SerializerFeature.UseSingleQuotes);
            passParam = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // 不支持的编码方式
            logger.error("不支持的编码方式: " + json.toJSONString());
            wxChargingResultBean.setRet(RetTypeEnum.SYS_ERROR.getValue());
            return SUCCESS;
        }
        // 用userid+getTimeStamp()组outTradeNo传给支付宝和微信
        String outTradeNo = user.getUserId() + getTimeStamp();

        // 发起支付
        if (way == PayWayEnum.ALIPAY.getValue()) {
            // alipay
            // 支付宝以元为单位，amount也是以元为单位，无需换算
            String money = payment.getShouldMoney().setScale(2).toString();
            try {
                String form = payWithAlipay("余额充值" + money + "元", passParam, outTradeNo, money);
                rs.setForm(form);
            } catch (AlipayApiException e) {
                logger.error("AlipayApiException: " + e.getErrCode() + " " + e.getErrMsg() + ", tradeNo: " + payment.getTradeNo() + ", money: " + money);
                rs.setFailReason(PaymentFailReasonEnum.ERROR_PAY.getShortValue());
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
        } else if (way == PayWayEnum.WECHAT.getValue()) {
            // tenpay
            // 微信支付需要openID
            String openID = getParameter("openId");
            BigDecimal money = payment.getShouldMoney().setScale(2);
            if (openID.length() != WX_OPENID_LENGTH) {
                // failed
                logger.error("openID长度错误: " + openID);
                rs.setFailReason(PaymentFailReasonEnum.OTHER.getShortValue());
                rs.setFailReasonDesc("openID长度错误");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            // 微信支付以分为单位，amount是以元为单位，要换算
            int moneyCent = money.multiply(new BigDecimal(100)).intValueExact();
            WxPaySendData data = payWithTenpay("余额充值" + money.setScale(2) + "元", passParam, outTradeNo, moneyCent, openID);
            rs.setWxData(data);
        } else if (way == PayWayEnum.ACCOUNT.getValue()) {
            // 账户支付
            BusAccount account = dataService.selectAccountByPrimaryKey(user.getUserId());
            if (account.getUsableMoney().compareTo(payment.getShouldMoney()) == -1) {
                logger.error("账户余额不足: " + payment.getId());
                rs.setFailReason(PaymentFailReasonEnum.LESS_USABLE_MONEY.getShortValue());
                rs.setFailReasonDesc(PaymentFailReasonEnum.LESS_USABLE_MONEY.getText());
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            int result = dataService.updatePaymentWithAccount(payment);
            if (result <= 0) {
                logger.error("订单支付失败: " + payment.getId());
                rs.setFailReason(PaymentFailReasonEnum.ERROR_PAY.getShortValue());
                rs.setFailReasonDesc(PaymentFailReasonEnum.ERROR_PAY.getText());
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
        } else {
            // wrong pay way
            logger.error("支付方式有误: " + way);
            rs.setFailReason(PaymentFailReasonEnum.OTHER.getShortValue());
            rs.setFailReasonDesc("支付方式有误");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    public String testRefund() {
        String tradeNo = getParameter("tradeNo");
        WxchargingPayUtil.refund(tradeNo);
        return SUCCESS;
    }

    /**
     * 自行车充电
     * 
     * @return
     */
    public String queryBikePayment() {
        // return value
        BikePaymentRs rs = new BikePaymentRs();
        String tradeNo = getParameter("tradeNo");
        BusBikePayment bike = WxchargingLocalUtil.returnBikePayment(tradeNo);
        WxChargingUser user = getUser();
        if (bike == null) {
            logger.error("订单编号错误: " + tradeNo);
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getValue());
            return SUCCESS;
        }
        // 缓存自行车充电信息
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("chaLen", bike.getChaLen());
        info.put("money", bike.getMoney());
        WxchargingCacheUtil.setBikeChargingInfo(tradeNo, JSON.toJSONString(info));
        // 组回传参数
        JSONObject json = new JSONObject();
        json.put("origin", PASS_BACK_BIKE);
        String passParam = null;
        try {
            String str = JSON.toJSONString(json, SerializerFeature.UseSingleQuotes);
            passParam = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // 不支持的编码方式
            logger.error("不支持的编码方式: " + json.toJSONString());
            wxChargingResultBean.setRet(RetTypeEnum.SYS_ERROR.getValue());
            return SUCCESS;
        }
        if (bike.getPayWay() == PayWayEnum.ALIPAY.getValue()) {
            // alipay
            // 支付宝以元为单位，amount也是以元为单位，无需换算
            try {
                String form = payWithAlipay("电动自行车充电" + bike.getMoney().setScale(2).toString() + "元", passParam, bike.getTradeNo(), bike.getMoney().setScale(2).toString());
                rs.setForm(form);
            } catch (AlipayApiException e) {
                logger.error("AlipayApiException: " + e.getErrCode() + " " + e.getErrMsg() + ", tradeNo: " + bike.getTradeNo() + ", money: " + bike.getMoney());
                rs.setFailReason(BikePaymentFailReasonEnum.ERROR_PAY.getShortValue());
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
        } else if (bike.getPayWay() == PayWayEnum.WECHAT.getValue()) {
            // tenpay
            // 微信支付需要openID
            String openID = getParameter("openId");
            if (openID.length() != WX_OPENID_LENGTH) {
                // failed
                logger.error("openID长度错误: " + openID);
                rs.setFailReason(BikePaymentFailReasonEnum.ERROR_PARAM.getShortValue());
                rs.setFailReasonDesc("openID长度错误");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            // 微信支付以分为单位，money是以元为单位，要换算
            int moneyCent = bike.getMoney().multiply(new BigDecimal(100)).intValueExact();
            WxPaySendData data = payWithTenpay("余额充值" + bike.getMoney().setScale(2) + "元", passParam, bike.getTradeNo(), moneyCent, openID);
            rs.setWxData(data);
        } else if (bike.getPayWay() == PayWayEnum.ACCOUNT.getValue()) {
            // 账户支付
            BusAccount account = dataService.selectAccountByPrimaryKey(user.getUserId());
            if (account.getUsableMoney().compareTo(bike.getMoney()) == -1) {
                // failed
                logger.error("余额不足: " + bike.getTradeNo());
                rs.setFailReason(BikePaymentFailReasonEnum.ERROR_PAY.getShortValue());
                rs.setFailReasonDesc("余额不足");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            int resultAccount = dataService.updateBikePaymentWithAccount(bike, true);
            if (resultAccount <= 0) {
                // failed
                logger.error("账户扣款失败: " + bike.getTradeNo());
                rs.setFailReason(BikePaymentFailReasonEnum.ERROR_PAY.getShortValue());
                rs.setFailReasonDesc("账户扣款失败");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            // 启动充电
            MsgChargeStart startMsg = new MsgChargeStart(bike.getPileId(), user.getUserId(), user.getPhone(), ChargeModeEnum.BY_TIME.getShortValue().byteValue(), bike.getChaLen(),
                                                         bike.getTradeNo());
            // 发送消息通知前置启动充电
            messageSendService.sendTopic(startMsg);
        } else {
            // wrong pay way
            logger.error("支付方式有误: " + bike.getPayWay());
            rs.setFailReason(BikePaymentFailReasonEnum.OTHER.getShortValue());
            rs.setFailReasonDesc("支付方式有误");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        rs.setTradeNo(bike.getTradeNo());
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 账户充值
     * 
     * @return
     */
    public String queryAccountRecharge() {
        // return value
        AccountRechargeRs rs = new AccountRechargeRs();
        // parameters
        String tradeNo = getParameter("tradeNo");
        BusRecharge recharge = WxchargingLocalUtil.returnRecharge(tradeNo);
        if (recharge == null) {
            logger.error("订单编号错误: " + tradeNo);
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getValue());
            return SUCCESS;
        }

        // 组回传参数
        JSONObject json = new JSONObject();
        json.put("origin", PASS_BACK_RECHARGE);
        String passParam = null;
        try {
            String str = JSON.toJSONString(json, SerializerFeature.UseSingleQuotes);
            passParam = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // 不支持的编码方式
            logger.error("不支持的编码方式: " + json.toJSONString());
            wxChargingResultBean.setRet(RetTypeEnum.SYS_ERROR.getValue());
            return SUCCESS;
        }
        if (recharge.getPayWay() == PayWayEnum.ALIPAY.getValue()) {
            // alipay
            // 支付宝以元为单位，amount也是以元为单位，无需换算
            try {
                String form = payWithAlipay("余额充值" + recharge.getMoney().setScale(2).toString() + "元", passParam, recharge.getTradeNo(), recharge.getMoney().setScale(2).toString());
                rs.setForm(form);
            } catch (AlipayApiException e) {
                logger.error("AlipayApiException: " + e.getErrCode() + " " + e.getErrMsg() + ", tradeNo: " + recharge.getTradeNo() + ", money: " + recharge.getMoney());
                rs.setFailReason(AccountRechargeFailReasonEnum.ERROR_PAY.getShortValue());
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
        } else if (recharge.getPayWay() == PayWayEnum.WECHAT.getValue()) {
            // tenpay
            // 微信支付需要openID
            String openID = getParameter("openId");
            if (openID.length() != WX_OPENID_LENGTH) {
                // failed
                logger.error("openID长度错误: " + openID);
                rs.setFailReason(AccountRechargeFailReasonEnum.OTHER.getShortValue());
                rs.setFailReasonDesc("openID长度错误");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            // 微信支付以分为单位，amount是以元为单位，要换算
            int moneyCent = recharge.getMoney().multiply(new BigDecimal(100)).intValueExact();
            WxPaySendData data = payWithTenpay("余额充值" + recharge.getMoney().setScale(2) + "元", passParam, recharge.getTradeNo(), moneyCent, openID);
            rs.setWxData(data);
        } else {
            // wrong pay way
            logger.error("支付方式有误: " + recharge.getPayWay());
            rs.setFailReason(AccountRechargeFailReasonEnum.OTHER.getShortValue());
            rs.setFailReasonDesc("支付方式有误");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        rs.setTradeNo(recharge.getTradeNo());
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 获取订单编号
     * 
     * @return
     */
    public String queryTradeNo() {
        TradeNoRs rs = new TradeNoRs();
        // parameter
        int way = getParamInt("payWay");
        // check
        if (way == 0) {
            // failed
            logger.error("支付方式参数错误: " + way);
            rs.setFailReason(RetTypeEnum.PARAM_ERROR.getShortValue());
            rs.setFailReasonDesc("支付方式参数错误");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        // parameter
        String moneyStr = getParameter("money");
        // check
        BigDecimal money;
        try {
            money = new BigDecimal(moneyStr);
        } catch (NumberFormatException e) {
            // failed
            logger.error("充值金额格式错误: " + moneyStr);
            rs.setFailReason(RetTypeEnum.PARAM_ERROR.getShortValue());
            rs.setFailReasonDesc("充值金额格式错误");
            wxChargingResultBean.setData(rs);
            return SUCCESS;
        }
        // user
        WxChargingUser user = getUser();

        // 交易类型：1 自行车充电支付 ; 2 账户充值支付
        int tradeType = getParamInt("tradeType");
        if (TradeTypeEnum.BIKE_PAY.getValue() == tradeType) {
            // parameter
            String pileCode = getParameter("pileCode");
            // check
            if (StringUtil.isEmpty(pileCode)) {
                // failed
                logger.error("缺少桩编码参数: " + pileCode);
                rs.setFailReason(RetTypeEnum.PARAM_ERROR.getShortValue());
                rs.setFailReasonDesc("缺少桩编码参数");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            PobChargingPile pile = null;
            PobChargingPileExample emp = new PobChargingPileExample();
            PobChargingPileExample.Criteria cr = emp.createCriteria();
            cr.andPileCodeEqualTo(pileCode);
            cr.andPileToTypeEqualTo(UseToTypeEnum.BIKE.getShortValue());
            List<PobChargingPile> list = chargingService.selectChargingPileByExample(emp);
            if (list != null && !list.isEmpty()) {
                pile = list.get(0);
            }

            if (pile == null) {
                // failed
                logger.error("桩编码不存在: " + pileCode);
                rs.setFailReason(RetTypeEnum.PARAM_ERROR.getShortValue());
                rs.setFailReasonDesc("桩编码不存在");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }

            // 插入记录
            BusBikePayment bike = new BusBikePayment();
            String tradeNo = StringUtil.AddjustLength(pile.getComAddr(), 12, "0") + DateUtil.DateToLong14Str(new Date()) + StringUtil.zeroPadString(pile.getId().toString(), 6);
            bike.setTradeNo(tradeNo);
            bike.setPileId(pile.getId());
            bike.setChaLen(chargeLength(pile, money.intValue()));
            bike.setUserId(user.getUserId());
            bike.setPayWay((short) way);
            bike.setMoney(money);
            bike.setStatus(BikePayStatusEnum.UNPAID.getShortValue());
            bike.setAddTime(new Date());
            bike.setUpdateTime(new Date());

            int result = dataService.insertBikePayment(bike);
            if (result <= 0) {
                // failed
                logger.error("插入Recharge失败: " + bike.toString());
                rs.setFailReason(BikePaymentFailReasonEnum.OTHER.getShortValue());
                rs.setFailReasonDesc("插入Recharge失败");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            rs.setTradeNo(bike.getTradeNo());
        } else if (TradeTypeEnum.ACC_RECHARGE.getValue() == tradeType) {
            BusRecharge recharge = new BusRecharge();
            String tradeNo = user.getUserId() + getTimeStamp();
            recharge.setTradeNo(tradeNo);
            recharge.setUserId(user.getUserId());
            recharge.setStatus(RechargeStatusEnum.RECHARGING.getShortValue());
            recharge.setPayWay((short) way);
            recharge.setMoney(money);
            recharge.setAddTime(new Date());
            recharge.setAddIp(getRequest().getRemoteAddr());
            int result = dataService.insertRecharge(recharge);
            if (result <= 0) {
                // failed
                logger.error("插入Recharge失败: " + recharge.toString());
                rs.setFailReason(AccountRechargeFailReasonEnum.OTHER.getShortValue());
                rs.setFailReasonDesc("插入Recharge失败");
                wxChargingResultBean.setData(rs);
                return SUCCESS;
            }
            rs.setTradeNo(recharge.getTradeNo());
        } else {
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getShortValue());
        }
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 查询支付结果
     * 
     * @return
     */
    public String queryPayResult() {
        PayResultRs rs = new PayResultRs();
        // parameter
        String tradeNo = getParameter("tradeNo");
        // 交易类型：1 自行车充电支付 ; 2 账户充值支付; 3 汽车充电支付
        int tradeType = getParamInt("tradeType");
        if (TradeTypeEnum.BIKE_PAY.getValue() == tradeType) {
            BusBikePaymentExample exp = new BusBikePaymentExample();
            BusBikePaymentExample.Criteria cri = exp.createCriteria();
            cri.andTradeNoEqualTo(tradeNo);
            List<BusBikePayment> payments = dataService.selectBikePaymentByExample(exp);
            Short status = payments.get(0).getStatus();
            if (BikePayStatusEnum.PAID_UNCHARGED.getShortValue().equals(status) || BikePayStatusEnum.CHARGED.getShortValue().equals(status)) {
                rs.setStatus(ChargePayStatusEnum.SUCCESS.getValue());
            } else if (BikePayStatusEnum.UNPAID.getShortValue().equals(status)) {
                rs.setStatus(ChargePayStatusEnum.UNPAID.getValue());
            } else if (BikePayStatusEnum.REFUND.getShortValue().equals(status) || BikePayStatusEnum.CLOSED.getShortValue().equals(status)) {
                rs.setStatus(ChargePayStatusEnum.FAILURE.getValue());
            }
        } else if (TradeTypeEnum.ACC_RECHARGE.getValue() == tradeType) {
            BusRechargeExample exp = new BusRechargeExample();
            BusRechargeExample.Criteria cri = exp.createCriteria();
            cri.andTradeNoEqualTo(tradeNo);
            List<BusRecharge> recharge = dataService.selectRechargeByExample(exp);
            Short status = recharge.get(0).getStatus();
            rs.setStatus(status);
        } else if (TradeTypeEnum.CAR_PAY.getValue() == tradeType) {
            BusPaymentExample exp = new BusPaymentExample();
            BusPaymentExample.Criteria cri = exp.createCriteria();
            cri.andTradeNoEqualTo(tradeNo);
            List<BusPayment> payment = dataService.selectPaymentByExample(exp);
            Short status = payment.get(0).getPayStatus();
            rs.setStatus(status);
        } else {
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getShortValue());
        }
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 支付宝支付接口
     * 
     * @param subject 订单标题
     * @param passbackParams 回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。
     * @param outTradeNo 商户订单号
     * @param totalAmount 订单金额
     * @return
     * @throws AlipayApiException
     */
    private String payWithAlipay(String subject, String passbackParams, String outTradeNo, String totalAmount) throws AlipayApiException {
        // 销售产品码 必填
        String productCode = "QUICK_WAP_WAY";
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        // 调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                                                      AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setPassbackParams(passbackParams);
        model.setProductCode(productCode);
        alipayRequest.setBizModel(model);
        // 设置异步通知地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        // form表单生产
        String form = client.pageExecute(alipayRequest).getBody();
        return form;
    }

    /**
     * 微信预支付
     */
    private WxPaySendData payWithTenpay(String body, String attach, String outTradeNo, Integer totalFee, String openId) {
        WxPaySendData result = new WxPaySendData();
        WxConfig config = null;
        try {
            config = new WxConfig();
        } catch (Exception e1) {
            logger.error("微信WxConfig初始化错误: " + e1.getMessage());
        }
        // 第三个参数true是使用沙盒
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>(8);
        data.put("body", body);
        data.put("openid", openId);
        data.put("attach", attach);
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", totalFee.toString());
        data.put("spbill_create_ip", getRequest().getRemoteAddr());
        data.put("notify_url", config.getNotifyUrl());
        data.put("trade_type", "JSAPI");
        Map<String, String> resp = new HashMap<String, String>(24);
        try {
            resp = wxpay.unifiedOrder(data);
        } catch (Exception e) {
            logger.error("微信统一下单出错: " + e.getMessage());
        }

        // 两者都为SUCCESS才能获取prepay_id
        if (TENPAY_TRADE_SUCCESS.equals(resp.get("return_code")) && TENPAY_TRADE_SUCCESS.equals(resp.get("result_code"))) {
            // 时间戳
            String timeStamp = String.valueOf(WxchargingUtil.getCurrentTimestamp());
            // 随机字符串
            String nonceStr = WXPayUtil.generateNonceStr();
            result.setResult_code(resp.get("result_code"));
            result.setAppid(config.getAppID());
            result.setTimeStamp(timeStamp);
            result.setNonce_str(nonceStr);
            result.setPackageStr("prepay_id=" + resp.get("prepay_id"));
            result.setSignType("MD5");
            Map<String, String> signMap = new HashMap<String, String>(5);
            signMap.put("appId", config.getAppID());
            signMap.put("timeStamp", timeStamp);
            signMap.put("nonceStr", nonceStr);
            // 注：看清楚，值为：prepay_id=xxx,别直接放成了wxReturnData.getPrepay_id()
            signMap.put("package", "prepay_id=" + resp.get("prepay_id"));
            signMap.put("signType", "MD5");
            // 支付签名
            String paySign = null;
            try {
                paySign = WXPayUtil.generateSignature(signMap, config.getKey());
            } catch (Exception e) {
                logger.error("微信支付签名出错: " + e.getMessage());
                e.printStackTrace();
            }
            result.setSign(paySign);
        } else {
            logger.error("微信预支付失败: " + resp.get("return_msg"));
        }
        return result;
    }

    /**
     * 生成格式化时间戳，17位
     * 
     * @return
     */
    private String getTimeStamp() {
        return DateUtil.DateToStr(new Date(), "yyyyMMddHHmmssSSS");
    }

    private Integer chargeLength(PobChargingPile pile, int type) {
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
        return Double.valueOf(tempFee * type * 60).intValue();
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }

}
