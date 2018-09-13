package com.holley.wxcharging.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.wxpay.sdk.WXPay;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.common.constants.PayWayEnum;
import com.holley.wxcharging.common.constants.WxConfig;
import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.service.DataService;

/**
 * 支付工具类
 * 
 * @author sc
 */
public class WxchargingPayUtil {

    private final static Logger logger               = Logger.getLogger(WxchargingPayUtil.class);
    private final static String Key                  = "2C3NFEWaaedTW1oFYrKL60pl2DCNb43V";
    private final static String characterEncoding    = "UTF-8";
    public final static String  KEY_ACCESS_TOKEN     = "KEY_ACCESS_TOKEN_WECHAT_WXCHARGING";
    public final static String  KEY_JSAPI_TICKET     = "KEY_JSAPI_TICKET_WECHAT_WXCHARGING";
    private static DataService  dataService;
    public static WxConfig      wxConfig             = new WxConfig();
    /**
     * 交易成功
     */
    private static final String TENPAY_TRADE_SUCCESS = "SUCCESS";

    /**
     * @param characterEncoding
     * @param parameters
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String createSign(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Key);
        System.out.println("字符串拼接后是：" + sb.toString());
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        System.out.println("sign：" + sign);
        return sign;
    }

    public static String createKeyValue(SortedMap<String, Object> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        if (sb.length() == 0) {
            return "";
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static SortedMap createParameters(Class clazz, Object obj) {
        Field[] fs = clazz.getDeclaredFields();
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        if (fs != null && fs.length > 0) {
            for (Field f : fs) {
                try {
                    String key = f.getName();
                    String value = BeanUtils.getProperty(obj, key);
                    params.put(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return params;
    }

    /**
     * 自行车充电退款
     * 
     * @param tradeNo 商户订单号
     * @return true 退款成功;false 退款失败，或请求订单不支持退款
     */
    public static void refund(String tradeNo) {
        BusBikePayment bike = WxchargingLocalUtil.returnBikePayment(tradeNo);
        // 只有当已支付未充电，并且超时的情况下，才可以退款
        // if (BikePayStatusEnum.PAID_UNCHARGED.getShortValue().equals(bike.getStatus())) {
        if (PayWayEnum.WECHAT.getShortValue().equals(bike.getPayWay())) {
            // 微信退款
            // --- 微信以分为单位
            String moneyCent = bike.getMoney().multiply(new BigDecimal(100)).setScale(0).toString();
            boolean result = refundWithTenpay(tradeNo, moneyCent);
            if (result) {
                // 退款成功
                // 更新订单状态
                bike.setStatus(BikePayStatusEnum.REFUND.getShortValue());
                bike.setAddTime(new Date());
                int resultUpdate = dataService.updateBikePayment(bike);
                if (resultUpdate <= 0) {
                    logger.error("更新退款状态失败: " + bike.getTradeNo());
                }
            }
        } else if (PayWayEnum.ALIPAY.getShortValue().equals(bike.getPayWay())) {
            // 支付宝退款
            boolean result = refundWithAlipay(tradeNo, bike.getMoney().toString());
            if (result) {
                // 退款成功
                // 更新订单状态
                bike.setStatus(BikePayStatusEnum.REFUND.getShortValue());
                bike.setAddTime(new Date());
                int resultUpdate = dataService.updateBikePayment(bike);
                if (resultUpdate <= 0) {
                    logger.error("更新退款状态失败: " + bike.getTradeNo());
                }
            }
        } else if (PayWayEnum.ACCOUNT.getShortValue().equals(bike.getPayWay())) {
            int resultUpdate = dataService.updateBikePaymentWithAccount(bike, false);
            if (resultUpdate <= 0) {
                logger.error("更新退款状态失败: " + bike.getTradeNo());
            }
        } else {
            // 异常
            logger.error("退款订单支付方式不支持退款: outTradeNo= " + tradeNo);
        }
        // } else {
        // // 异常
        // logger.error("退款订单当前状态不支持退款: outTradeNo= " + tradeNo);
        // }
    }

    /**
     * 支付宝退款
     * 
     * @param outTradeNo 交易订单
     * @param refundAmount 退款金额
     * @return true 请求成功, false 请求失败
     * @throws AlipayApiException
     */
    private static boolean refundWithAlipay(String outTradeNo, String refundAmount) {
        String reason = "自行车充电启动失败";
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                                                      AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeRefundRequest alipayRequest = new AlipayTradeRefundRequest();
        // 封装退款请求信息
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setRefundAmount(refundAmount);
        model.setRefundReason(reason);
        alipayRequest.setBizModel(model);
        AlipayTradeRefundResponse response = null;
        try {
            response = client.execute(alipayRequest);
        } catch (AlipayApiException e) {
            logger.info("支付宝退款请求错误: " + e.getErrMsg());
            return false;
        }
        if (response.isSuccess()) {
            return true;
        } else {
            logger.info("支付宝退款失败: " + response.getMsg());
            return false;
        }
    }

    private static boolean refundWithTenpay(String outTradeNo, String refundAmount) {
        /*
         * WxConfig config = null; try { config = new WxConfig(); } catch (Exception e1) {
         * logger.error("微信WxConfig初始化错误: " + e1.getMessage()); return false; }
         */
        // 第三个参数true是使用沙盒
        WXPay wxpay = new WXPay(wxConfig);
        Map<String, String> data = new HashMap<String, String>(4);
        data.put("out_trade_no", outTradeNo);
        data.put("out_refund_no", outTradeNo);
        data.put("total_fee", refundAmount);
        data.put("refund_fee", refundAmount);
        Map<String, String> resp = new HashMap<String, String>(24);
        try {
            resp = wxpay.refund(data);
        } catch (Exception e) {
            logger.error("微信退款请求错误: " + e.getMessage());
            return false;
        }

        if (TENPAY_TRADE_SUCCESS.equals(resp.get("return_code")) && TENPAY_TRADE_SUCCESS.equals(resp.get("result_code"))) {
            return true;
        } else {
            logger.error("微信退款失败: " + resp.get("return_msg"));
            return false;
        }
    }

    public static void setDataService(DataService dataService) {
        WxchargingPayUtil.dataService = dataService;
    }

}
