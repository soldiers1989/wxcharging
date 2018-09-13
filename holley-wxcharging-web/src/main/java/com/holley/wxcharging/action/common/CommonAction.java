package com.holley.wxcharging.action.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.holley.common.constants.charge.ChargeModeEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.jms.MessageSendService;
import com.holley.common.rocketmq.charging.MsgChargeStart;
import com.holley.common.util.StringUtil;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.common.constants.ChargePayStatusEnum;
import com.holley.wxcharging.common.constants.PayWayEnum;
import com.holley.wxcharging.common.constants.RetTypeEnum;
import com.holley.wxcharging.common.constants.UseToTypeEnum;
import com.holley.wxcharging.common.constants.WxConfig;
import com.holley.wxcharging.common.failReason.StationDetailFailReasonEnum;
import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusRecharge;
import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.def.PileStatus;
import com.holley.wxcharging.model.def.StationNearby;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.rs.StationDetailRs;
import com.holley.wxcharging.model.def.rs.StationNearbyRs;
import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import com.holley.wxcharging.model.pob.PobChargingStation;
import com.holley.wxcharging.service.AccountService;
import com.holley.wxcharging.service.ChargingService;
import com.holley.wxcharging.service.DataService;
import com.holley.wxcharging.util.AlipayConfig;
import com.holley.wxcharging.util.WxchargingCacheUtil;
import com.holley.wxcharging.util.WxchargingLocalUtil;

/**
 * 公共ACTION
 * 
 * @author sc
 */
public class CommonAction extends BaseAction {

    private static final long   serialVersionUID          = 1L;

    private final static Logger logger                    = Logger.getLogger(CommonAction.class);

    private InputStream         imageStream;
    /**
     * 支付回传参数。充值余额时。
     */
    private final String        PASS_BACK_RECHARGE        = "PASS_BACK_RECHARGE";
    /**
     * 支付回传参数。充电支付时。
     */
    private final String        PASS_BACK_PAYMENT         = "PASS_BACK_PAYMENT";
    /**
     * 支付回传参数。自行车充电。
     */
    private final String        PASS_BACK_BIKE            = "PASS_BACK_BIKE";
    /**
     * 交易完成(支付宝)
     */
    private final String        ALIPAY_TRADE_FINISHED     = "TRADE_FINISHED";
    /**
     * 交易成功(支付宝)
     */
    private final String        ALIPAY_TRADE_SUCCESS      = "TRADE_SUCCESS";
    /**
     * 交易关闭(支付宝)
     */
    private final String        ALIPAY_TRADE_CLOSED       = "TRADE_CLOSED";
    /**
     * 交易成功
     */
    private final String        TENPAY_TRADE_SUCCESS      = "SUCCESS";
    /**
     * 微信返回串(成功)
     */
    private final String        WECHAT_RETURN_SUCCESS     = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    /**
     * 微信返回串(失败)
     */
    private final String        WECHAT_RETURN_FAIL        = "<xm><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[交易失败！]]></return_msg></xml>";
    /**
     * 微信返回串(参数错误)
     */
    private final String        WECHAT_RETURN_WRONG_PARAM = "<xm><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数错误！]]></return_msg></xml>";
    /**
     * 微信返回串(验签失败)
     */
    private final String        WECHAT_RETURN_WRONG_SIGN  = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[签名校验失败]]></return_msg></xml>";
    @Resource
    private DataService         dataService;
    @Resource
    private ChargingService     chargingService;
    @Resource
    private AccountService      accountService;
    @Resource
    private MessageSendService  messageSendService;

    public String blank() {
        return SUCCESS;
    }

    /**
     * 附近
     */

    public String nearby() {
        return SUCCESS;
    }

    /**
     * 附近详情
     */

    public String nearbyMore() {
        return SUCCESS;
    }

    /**
     * 生成验证码方法
     * 
     * @return
     */
    public String createCode() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String code = drawImg(output);

        WxchargingCacheUtil.setImgValidateCode(getSessionId(), code);

        try {
            imageStream = new ByteArrayInputStream(output.toByteArray());
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    private String drawImg(ByteArrayOutputStream output) {
        String code = "";
        for (int i = 0; i < 4; i++) {
            code += randomChar();
        }
        // int width = 70;
        // int height = 25;
        int width = 100;
        int height = 50;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Font font = new Font("Times New Roman", Font.PLAIN, 25);
        Graphics2D g = bi.createGraphics();
        g.setFont(font);
        // Color color = new Color(66, 2, 82);
        Color color = new Color(255, 255, 255);
        g.setColor(color);
        // g.setBackground(new Color(226, 226, 240));
        // g.setBackground(new Color(10, 80, 168));
        g.setBackground(new Color(79, 128, 174));
        g.clearRect(0, 0, width, height);
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(code, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = bounds.getY();
        double baseY = y - ascent;
        g.drawString(code, (int) x, (int) baseY);
        g.dispose();
        try {
            ImageIO.write(bi, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    private char randomChar() {
        Random r = new Random();
        String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
        return s.charAt(r.nextInt(s.length()));
    }

    /**
     * 查询附近充电站
     * 
     * @return
     */
    public String queryStationNearby() {
        // parameters
        String keyWord = getParameter("keyWord");
        String longtitude = getParameter("longtitude");
        String latitude = getParameter("latitude");
        int pageIndex = getParamInt("pageIndex");
        int stationType = getParamInt("stationType");
        // check
        try {
            Double.parseDouble(longtitude);
            Double.parseDouble(latitude);
        } catch (Exception e) {
            // 参数格式错误
            logger.error("坐标参数错误: " + longtitude + ' ' + latitude);
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getValue());
            return SUCCESS;
        }
        if (UseToTypeEnum.getText(stationType) == null) {
            // 参数格式错误
            logger.error("站类型参数错误: " + stationType);
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getValue());
            return SUCCESS;
        }
        // page
        Page page = this.returnPage(pageIndex, limit);
        Map<String, Object> param = new HashMap<String, Object>(5);
        param.put("page", page);
        if (StringUtil.isNotEmpty(keyWord)) {
            param.put("keyword", keyWord);
        }

        // query
        param.put("longtitude", longtitude);
        param.put("latitude", latitude);
        param.put("type", stationType);
        List<StationNearby> nearby = chargingService.selectNearbyStationByPage(param);
        // 取idList
        List<Integer> stationIdList = new ArrayList<Integer>();
        for (StationNearby station : nearby) {
            stationIdList.add(station.getId());
        }
        if (stationIdList.size() > 0) {
            // 查询pileList
            PobChargingPileExample exp = new PobChargingPileExample();
            PobChargingPileExample.Criteria cri = exp.createCriteria();
            cri.andStationIdIn(stationIdList);
            List<PobChargingPile> pileList = chargingService.selectChargingPileByExample(exp);
            // 计算闲充共
            for (StationNearby station : nearby) {
                List<PobChargingPile> pileOfStation = new ArrayList<PobChargingPile>();
                for (PobChargingPile pile : pileList) {
                    // 取属于当前station的pileList
                    if (pile.getStationId().equals(station.getId())) {
                        pileOfStation.add(pile);
                    }
                }
                // 查询统计
                int available = 0, busy = 0;
                for (PobChargingPile pile : pileOfStation) {
                    PileStatusEnum status = WxchargingCacheUtil.returnPileStatus(pile.getId());
                    if (status.getValue() == PileStatusEnum.IDLE.getValue()) {
                        // 闲
                        available++;
                    } else if (status.getValue() == PileStatusEnum.CHARGING.getValue()) {
                        // 充
                        busy++;
                    }
                }
                // 填入结果
                station.setAvailable(available);
                station.setBusy(busy);
                station.setTotal(pileOfStation.size());
            }
        }
        StationNearbyRs rs = new StationNearbyRs(page.getTotalProperty(), pageIndex, limit);
        rs.setDatas(nearby);
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 查询充电站详细信息
     * 
     * @return
     */
    public String queryStationDetail() {
        StationDetailRs rs = new StationDetailRs();
        // parameters
        int stationId = getParamInt("stationId");
        // check
        if (stationId == 0) {
            // 参数格式错误
            logger.error("站ID参数错误: " + stationId);
            rs.setFailReason(StationDetailFailReasonEnum.WRONG_PARAM.getValue());
            rs.setFailReasonDesc(StationDetailFailReasonEnum.WRONG_PARAM.getText());
            return SUCCESS;
        }
        // 查询station
        PobChargingStation station = chargingService.selectChargingStationByPrimaryKey(stationId);
        if (station == null) {
            // 参数错误
            logger.error("站ID不存在: " + stationId);
            rs.setFailReason(StationDetailFailReasonEnum.WRONG_PARAM.getValue());
            rs.setFailReasonDesc("请求的站id不存在");
            return SUCCESS;
        }
        // 查询站下的pile
        PobChargingPileExample exp = new PobChargingPileExample();
        PobChargingPileExample.Criteria cri = exp.createCriteria();
        cri.andStationIdEqualTo(station.getId());
        List<PobChargingPile> pileList = chargingService.selectChargingPileByExample(exp);
        // 组pileStatus
        List<PileStatus> pileStatusList = new ArrayList<PileStatus>();
        for (PobChargingPile pile : pileList) {
            PileStatus status = new PileStatus();
            status.setId(pile.getId());
            status.setPileCode(pile.getPileCode());
            status.setPileName(pile.getPileName());
            status.setType(pile.getPileType());
            // 查状态
            PileStatusEnum statusEnum = WxchargingCacheUtil.returnPileStatus(pile.getId());
            status.setStatus(statusEnum.getShortValue());
            pileStatusList.add(status);
        }
        // 填rs
        rs.setStationName(station.getStationName());
        rs.setAddress(station.getAddress());
        rs.setLongtitude(station.getLng());
        rs.setLatitude(station.getLat());
        rs.setPhone(station.getLinkPhone());
        rs.setDatas(pileStatusList);
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 支付宝异步返回
     * 
     * @return
     */
    public void alipayNotify() {
        PrintWriter writer = null;

        String outTradeNo = getParameter("out_trade_no");
        String passbackParams = getParameter("passback_params");
        String tradeStatus = getParameter("trade_status");
        String passbackParamsDecode = null;
        try {
            passbackParamsDecode = URLDecoder.decode(passbackParams, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // 不支持的编码方式
            logger.error("不支持的编码方式: " + passbackParams);
        }
        JSONObject passbackJson = JSON.parseObject(passbackParamsDecode);
        // 计算得出通知验证结果
        try {
            writer = getResponse().getWriter();
            if (alipayResult()) {
                // 支付宝状态为FINISHED和SUCCESS时，交易成功
                if (ALIPAY_TRADE_FINISHED.equals(tradeStatus) || ALIPAY_TRADE_SUCCESS.equals(tradeStatus)) {
                    // 余额充值通知
                    if (PASS_BACK_RECHARGE.equals(passbackJson.getString("origin"))) {
                        // 充值业务的outTradeNo就是系统内的订单号
                        BusRecharge recharge = WxchargingLocalUtil.returnRecharge(outTradeNo);
                        // 充值订单状态为未支付时才可接受操作
                        if (RechargeStatusEnum.RECHARGING.getShortValue().equals(recharge.getStatus())) {
                            int updateResult = dataService.updateRecharge(recharge, true);
                            if (updateResult <= 0) {
                                logger.error("更新充值订单失败: " + recharge.toString());
                                writer.println("fail");
                                writer.flush();
                                writer.close();
                                return;
                            }
                        } else {
                            logger.info("充值订单号(out_trade_no)" + outTradeNo + "已经处理过，略过");
                        }
                    } else if (PASS_BACK_PAYMENT.equals(passbackJson.getString("origin"))) {
                        // 充电支付业务的outTradeNo是生成的字符串，真实tradeNo在回传参数里
                        String payTradeNo = passbackJson.getString("tradeno");
                        // 充电支付通知
                        BusPayment payment = WxchargingLocalUtil.returnPayment(payTradeNo);
                        if (!ChargePayStatusEnum.SUCCESS.getShortValue().equals(payment.getPayStatus())) {
                            payment.setAccountInfo(outTradeNo);
                            payment.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
                            payment.setPayWay(PayWayEnum.ALIPAY.getShortValue());
                            int resultPayment = dataService.updatePayment(payment);
                            if (resultPayment <= 0) {
                                logger.error("更新充电订单失败: " + payment.toString());
                                writer.println("fail");
                                writer.flush();
                                writer.close();
                                return;
                            }
                        } else {
                            logger.info("充电订单号(out_trade_no)" + payTradeNo + "已经支付过，不可重复支付");
                        }
                    } else if (PASS_BACK_BIKE.equals(passbackJson.getString("origin"))) {
                        // 自行车充电通知
                        BusBikePayment bike = WxchargingLocalUtil.returnBikePayment(outTradeNo);
                        if (BikePayStatusEnum.UNPAID.getShortValue().equals(bike.getStatus())) {
                            bike.setStatus(BikePayStatusEnum.PAID_UNCHARGED.getShortValue());
                            bike.setUpdateTime(new Date());
                            int resultBike = dataService.updateBikePayment(bike);
                            if (resultBike <= 0) {
                                logger.error("更新自行车订单失败: " + bike.toString());
                                writer.println("fail");
                                writer.flush();
                                writer.close();
                                return;
                            }
                            BusUser user = accountService.selectUserByPrimaryKey(bike.getUserId());
                            MsgChargeStart startMsg = new MsgChargeStart(bike.getPileId(), user.getId(), user.getPhone(), ChargeModeEnum.BY_TIME.getShortValue().byteValue(),
                                                                         bike.getChaLen(), bike.getTradeNo());
                            // 发送消息通知前置启动充电
                            messageSendService.sendTopic(startMsg);
                        } else {
                            logger.info("自行车订单号(out_trade_no)" + outTradeNo + "已经支付过，不可重复支付");
                        }
                    } else {
                        // 回传参数错误
                        logger.error("支付宝回传参数错误: " + passbackJson.toJSONString());
                        writer.println("fail");
                        writer.flush();
                        writer.close();
                        return;
                    }
                } else if (ALIPAY_TRADE_CLOSED.equals(tradeStatus)) {
                    // TRADE_CLOSED:在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
                    if (PASS_BACK_RECHARGE.equals(passbackJson.getString("origin"))) {
                        // 充值业务的outTradeNo就是系统内的订单号
                        BusRecharge recharge = WxchargingLocalUtil.returnRecharge(outTradeNo);
                        // 判断订单支付状态
                        if (RechargeStatusEnum.RECHARGING.getShortValue().equals(recharge.getStatus())) {
                            // 余额充值通知
                            // 更新订单状态(支付失败)
                            int resultRecharge = dataService.updateRecharge(recharge, false);
                            if (resultRecharge <= 0) {
                                logger.error("更新充值订单失败: " + recharge.toString());
                                writer.println("fail");
                                writer.flush();
                                writer.close();
                                return;
                            }
                        } else {
                            logger.info("充值订单号(out_trade_no)" + outTradeNo + "已经处理过，略过");
                        }
                    } else if (PASS_BACK_PAYMENT.equals(passbackJson.getString("origin"))) {
                        // 充电支付业务的outTradeNo是生成的字符串，真实tradeNo在回传参数里
                        String payTradeNo = passbackJson.getString("tradeno");
                        // 充电支付通知
                        BusPayment payment = WxchargingLocalUtil.returnPayment(payTradeNo);
                        if (!ChargePayStatusEnum.SUCCESS.getShortValue().equals(payment.getPayStatus())) {
                            // 余额充值通知
                            payment.setPayStatus(ChargePayStatusEnum.FAILURE.getShortValue());
                            payment.setPayWay(PayWayEnum.ALIPAY.getShortValue());
                            int resultPayment = dataService.updatePayment(payment);
                            if (resultPayment <= 0) {
                                logger.error("更新充电订单失败: " + payment.toString());
                                writer.println("fail");
                                writer.flush();
                                writer.close();
                                return;
                            }
                        } else {
                            logger.info("充电订单号(out_trade_no)" + outTradeNo + "已经支付过，不可重复支付");
                        }
                    } else if (PASS_BACK_BIKE.equals(passbackJson.getString("origin"))) {
                        // 自行车充电通知
                        BusBikePayment bike = WxchargingLocalUtil.returnBikePayment(outTradeNo);
                        if (BikePayStatusEnum.UNPAID.getShortValue().equals(bike.getStatus())) {
                            bike.setStatus(BikePayStatusEnum.CLOSED.getShortValue());
                            bike.setUpdateTime(new Date());
                            int resultBike = dataService.updateBikePayment(bike);
                            if (resultBike <= 0) {
                                logger.error("更新自行车订单失败: " + bike.toString());
                                writer.println("fail");
                                writer.flush();
                                writer.close();
                                return;
                            }
                        } else {
                            logger.info("自行车订单号(out_trade_no)" + outTradeNo + "已经支付过，不可重复支付");
                        }
                    } else {
                        // 回传参数错误
                        logger.error("支付宝回传参数错误: " + passbackJson.toJSONString());
                        writer.println("fail");
                        writer.flush();
                        writer.close();
                        return;
                    }
                }
                writer.println("success");
            } else {
                // 验证失败
                logger.info("alipayNotify: 验证失败");
                writer.println("fail");
            }
        } catch (IOException e) {
            logger.error("getWriter failed");
        }
        if (writer != null) {
            writer.flush();
            writer.close();
        }

    }

    /**
     * 支付宝同步返回
     * 
     * @return
     * @throws IOException
     * @throws AlipayApiException
     */
    public String alipayReturn() throws IOException {
        // 商户订单号
        String totalAmount = getParameter("total_amount");
        if (alipayResult()) {
            // 验证成功
            logger.info("alipayReturn:" + "验证成功");
            // 请在这里加上商户的业务逻辑程序代码
            // 该页面可做页面美工编辑
            // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            getRequest().setAttribute("result", "success");
            getRequest().setAttribute("message", "支付成功");
            getRequest().setAttribute("subMessage", totalAmount + "元");
        } else {
            // 该页面可做页面美工编辑
            logger.info("alipayReturn:" + "验证失败");
            getRequest().setAttribute("result", "fail");
            getRequest().setAttribute("message", "等待");
            getRequest().setAttribute("subMessage", "已提交申请，请稍后查看");
        }

        return SUCCESS;
    }

    /**
     * 解析支付返回结果
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    private boolean alipayResult() {
        boolean rs = false;
        try {
            Map<String, String> params = new HashMap<String, String>(16);
            Map requestParams = getRequest().getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }
            if (params.containsKey("sign")) {
                String sign = params.get("sign");
                if (sign.contains(" ")) {
                    sign = sign.replace(" ", "+");
                    params.put("sign", sign);
                }
            }
            rs = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 微信支付回调接口
     * 
     * @return
     */
    public String wechatNotify() {
        try {
            InputStream is = getRequest().getInputStream();
            String notifyData = IOUtils.toString(is, "UTF-8");

            WxConfig config = new WxConfig();
            // 第三个参数的true是使用沙盒
            WXPay wxpay = new WXPay(config);

            // 转换成map
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);

            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String attach = notifyMap.get("attach");
                String attachDecode = null;
                try {
                    attachDecode = URLDecoder.decode(attach, "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    // 不支持的编码方式
                    logger.error("不支持的编码方式: " + attach);
                }
                JSONObject attachJson = JSON.parseObject(attachDecode);
                // 当returnCode和resultCode同时为success时，支付成功
                if (TENPAY_TRADE_SUCCESS.equals(notifyMap.get("return_code")) && TENPAY_TRADE_SUCCESS.equals(notifyMap.get("result_code"))) {
                    if (PASS_BACK_RECHARGE.equals(attachJson.getString("origin"))) {
                        // 充值业务的outTradeNo就是系统内的订单号
                        BusRecharge recharge = WxchargingLocalUtil.returnRecharge(notifyMap.get("out_trade_no"));
                        // 充值订单状态为未支付时才可接受操作
                        if (RechargeStatusEnum.RECHARGING.getShortValue().equals(recharge.getStatus())) {
                            int updateResult = dataService.updateRecharge(recharge, true);
                            if (updateResult <= 0) {
                                logger.error("更新充值订单失败: " + recharge.toString());
                                getResponse().getWriter().write(WECHAT_RETURN_FAIL);
                                getResponse().getWriter().flush();
                                getResponse().getWriter().close();
                                return SUCCESS;
                            }
                        } else {
                            // 已处理过
                            logger.info("充值订单号(out_trade_no)" + notifyMap.get("out_trade_no") + "已经处理过，略过");
                        }
                    } else if (PASS_BACK_PAYMENT.equals(attachJson.getString("origin"))) {
                        // 充电支付业务的outTradeNo是生成的字符串，真实tradeNo在回传参数里
                        String payTradeNo = attachJson.getString("tradeno");
                        // 充电支付通知
                        BusPayment payment = WxchargingLocalUtil.returnPayment(payTradeNo);
                        if (!ChargePayStatusEnum.SUCCESS.getShortValue().equals(payment.getPayStatus())) {
                            // 余额充值通知
                            payment.setAccountInfo(notifyMap.get("out_trade_no"));
                            payment.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
                            payment.setPayWay(PayWayEnum.WECHAT.getShortValue());
                            int resultPayment = dataService.updatePayment(payment);
                            if (resultPayment <= 0) {
                                logger.error("更新充电订单失败: " + payment.toString());
                                getResponse().getWriter().write(WECHAT_RETURN_FAIL);
                                getResponse().getWriter().flush();
                                getResponse().getWriter().close();
                                return SUCCESS;
                            }
                        } else {
                            logger.info("充电订单号(out_trade_no)" + payTradeNo + "已经支付过，不可重复支付");
                        }
                    } else if (PASS_BACK_BIKE.equals(attachJson.getString("origin"))) {
                        // 自行车充电通知
                        BusBikePayment bike = WxchargingLocalUtil.returnBikePayment(notifyMap.get("out_trade_no"));
                        if (BikePayStatusEnum.UNPAID.getShortValue().equals(bike.getStatus())) {
                            bike.setStatus(BikePayStatusEnum.PAID_UNCHARGED.getShortValue());
                            bike.setUpdateTime(new Date());
                            int resultBike = dataService.updateBikePayment(bike);
                            if (resultBike <= 0) {
                                logger.error("更新自行车订单失败: " + bike.toString());
                                getResponse().getWriter().write(WECHAT_RETURN_FAIL);
                                getResponse().getWriter().flush();
                                getResponse().getWriter().close();
                                return SUCCESS;
                            }
                            BusUser user = accountService.selectUserByPrimaryKey(bike.getUserId());
                            MsgChargeStart startMsg = new MsgChargeStart(bike.getPileId(), user.getId(), user.getPhone(), ChargeModeEnum.BY_TIME.getShortValue().byteValue(),
                                                                         bike.getChaLen(), bike.getTradeNo());
                            // 发送消息通知前置启动充电
                            messageSendService.sendTopic(startMsg);
                        } else {
                            logger.info("自行车订单号(out_trade_no)" + notifyMap.get("out_trade_no") + "已经支付过，不可重复支付");
                        }
                    } else {
                        // 回传参数错误
                        logger.error("微信回传参数错误: " + attachJson.toJSONString());
                        getResponse().getWriter().write(WECHAT_RETURN_WRONG_PARAM);
                        getResponse().getWriter().flush();
                        getResponse().getWriter().close();
                        return SUCCESS;
                    }
                } else {
                    // 交易失败
                    // TRADE_CLOSED:在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
                    if (PASS_BACK_RECHARGE.equals(attachJson.getString("origin"))) {
                        // 充值业务的outTradeNo就是系统内的订单号
                        BusRecharge recharge = WxchargingLocalUtil.returnRecharge(notifyMap.get("out_trade_no"));
                        // 判断订单支付状态
                        if (RechargeStatusEnum.RECHARGING.getShortValue().equals(recharge.getStatus())) {
                            // 余额充值通知
                            int resultRecharge = dataService.updateRecharge(recharge, false);
                            if (resultRecharge <= 0) {
                                logger.error("更新充值订单失败: " + recharge.toString());
                                getResponse().getWriter().write(WECHAT_RETURN_FAIL);
                                getResponse().getWriter().flush();
                                getResponse().getWriter().close();
                                return SUCCESS;
                            }
                        } else {
                            logger.info("充值订单号(out_trade_no)" + notifyMap.get("out_trade_no") + "已经处理过，略过");
                        }
                    } else if (PASS_BACK_PAYMENT.equals(attachJson.getString("origin"))) {
                        // 充电支付业务的outTradeNo是生成的字符串，真实tradeNo在回传参数里
                        String payTradeNo = attachJson.getString("tradeno");
                        // 充电支付通知
                        BusPayment payment = WxchargingLocalUtil.returnPayment(payTradeNo);
                        if (!ChargePayStatusEnum.SUCCESS.getShortValue().equals(payment.getPayStatus())) {
                            // 余额充值通知
                            payment.setPayStatus(ChargePayStatusEnum.FAILURE.getShortValue());
                            payment.setPayWay(PayWayEnum.WECHAT.getShortValue());
                            int resultPayment = dataService.updatePayment(payment);
                            if (resultPayment <= 0) {
                                logger.error("更新充电订单失败: " + payment.toString());
                                getResponse().getWriter().write(WECHAT_RETURN_FAIL);
                                getResponse().getWriter().flush();
                                getResponse().getWriter().close();
                                return SUCCESS;
                            }
                        } else {
                            logger.info("充电订单号(out_trade_no)" + notifyMap.get("out_trade_no") + "已经支付过，不可重复支付");
                        }
                    } else if (PASS_BACK_BIKE.equals(attachJson.getString("origin"))) {
                        // 自行车充电通知
                        BusBikePayment bike = WxchargingLocalUtil.returnBikePayment(notifyMap.get("out_trade_no"));
                        if (BikePayStatusEnum.UNPAID.getShortValue().equals(bike.getStatus())) {
                            bike.setStatus(BikePayStatusEnum.CLOSED.getShortValue());
                            bike.setUpdateTime(new Date());
                            int resultBike = dataService.updateBikePayment(bike);
                            if (resultBike <= 0) {
                                logger.error("更新自行车订单失败: " + bike.toString());
                                getResponse().getWriter().write(WECHAT_RETURN_FAIL);
                                getResponse().getWriter().flush();
                                getResponse().getWriter().close();
                                return SUCCESS;
                            }
                        } else {
                            logger.info("自行车订单号(out_trade_no)" + notifyMap.get("out_trade_no") + "已经支付过，不可重复支付");
                        }
                    } else {
                        // 回传参数错误
                        logger.error("微信回传参数错误: " + attachJson.toJSONString());
                        getResponse().getWriter().write(WECHAT_RETURN_WRONG_PARAM);
                        getResponse().getWriter().flush();
                        getResponse().getWriter().close();
                        return SUCCESS;
                    }
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                getResponse().getWriter().write(WECHAT_RETURN_WRONG_SIGN);
                getResponse().getWriter().flush();
                getResponse().getWriter().close();
                return SUCCESS;
            }

            // 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
            getResponse().getWriter().write(WECHAT_RETURN_SUCCESS);
            getResponse().getWriter().flush();
            getResponse().getWriter().close();
        } catch (Exception e) {
            logger.error("微信支付回调: " + e.getMessage());
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }
}
