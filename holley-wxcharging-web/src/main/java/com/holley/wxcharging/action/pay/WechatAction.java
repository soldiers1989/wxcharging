package com.holley.wxcharging.action.pay;

import java.security.DigestException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.github.wxpay.sdk.WXPayUtil;
import com.holley.common.cache.RedisUtil;
import com.holley.common.util.StringUtil;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.constants.RetTypeEnum;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.rs.WxAuthorizeRs;
import com.holley.wxcharging.model.def.rs.WxConfigRs;
import com.holley.wxcharging.model.def.rs.WxUserInfoRs;
import com.holley.wxcharging.util.HttpClientUtil;
import com.holley.wxcharging.util.SHA1Util;
import com.holley.wxcharging.util.WxchargingPayUtil;
import com.holley.wxcharging.util.WxchargingUtil;

public class WechatAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private final static Logger logger           = Logger.getLogger(PayAction.class);

    // private static WxConfig config;

    /**
     * 微信授权调用
     * 
     * @return
     */
    public String authorize() {
        // return value
        WxAuthorizeRs rs = new WxAuthorizeRs();
        // 获取code
        String code = getParameter("code");
        JSONObject json = getOpenId(code);
        if (!json.has("errmsg")) {
            String openid = json.getString("openid");
            rs.setOpenid(openid);
        } else {
            logger.error("微信openid获取失败: " + json.getString("errmsg"));
            rs.setFailReason(Integer.parseInt(json.getString("errcode")));
            rs.setFailReasonDesc(json.getString("errmsg"));
        }
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 拉取用户信息
     * 
     * @return
     */
    public String userInfo() {
        WxUserInfoRs rs = new WxUserInfoRs();
        String openid = getParameter("openid");
        JSONObject json = getWechatUserInfo(openid);
        if (!json.has("errmsg")) {
            rs.setCity(json.getString("city"));
            rs.setCountry(json.getString("country"));
            rs.setHeadimgurl(json.getString("headimgurl"));
            rs.setNickname(json.getString("nickname"));
            rs.setOpenid(json.getString("openid"));
            rs.setProvince(json.getString("province"));
            rs.setSex(json.getString("sex"));
        } else {
            logger.error("微信openid获取失败: " + json.getString("errmsg"));
            rs.setFailReason(Integer.parseInt(json.getString("errcode")));
            rs.setFailReasonDesc(json.getString("errmsg"));
        }
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 微信JSSDK调用所需的配置信息
     * 
     * @return
     */
    public String wxConfig() {
        String url = getParameter("url");
        if (url == null) {
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getValue());
            return SUCCESS;
        }
        String timestamp = String.valueOf(WxchargingUtil.getCurrentTimestamp());
        String noncestr = WXPayUtil.generateNonceStr();
        String jsapi_ticket = jsTicket();
        Map<String, Object> param = new HashMap<String, Object>(4);
        param.put("url", url);
        param.put("timestamp", timestamp);
        param.put("noncestr", noncestr);
        param.put("jsapi_ticket", jsapi_ticket);
        String sha1 = "";
        try {
            sha1 = SHA1Util.SHA1(param);
        } catch (DigestException e) {
            logger.error("SHA1加密错误: " + e.getMessage());
            wxChargingResultBean.setRet(RetTypeEnum.SYS_ERROR.getValue());
            return SUCCESS;
        }
        WxConfigRs rs = new WxConfigRs();
        rs.setAppId(WxchargingPayUtil.wxConfig.getAppID());
        rs.setNonceStr(noncestr);
        rs.setSignature(sha1);
        rs.setTimestamp(timestamp);
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    // 扫码验证是否关注
    public String qrcode() throws Exception {
        String code = getParameter("code");
        String token = accessToken();
        if (StringUtil.isEmpty(token)) {
            message("微信授权失败，请重新尝试.");
            return MSG;
        }
        JSONObject openIdjson = getOpenId(code);
        if (openIdjson == null || openIdjson.has("errmsg")) {
            message("微信授权失败，请重新尝试.");
            return MSG;
        }
        String openId = openIdjson.getString("openid");
        JSONObject userInfoJson = getWechatUserInfo(openId);
        if (userInfoJson == null || userInfoJson.has("errmsg")) {
            message("微信授权失败，请重新尝试.");
            return MSG;
        }
        int subscribe = userInfoJson.getInt("subscribe");// 1：关注0：未关注
        return SUCCESS;
    }

    /*
     * public static void main(String[] args) { RedisUtil.delKey(KEY_ACCESS_TOKEN); RedisUtil.delKey(KEY_JSAPI_TICKET);
     * }
     */

    /**
     * 微信全局调用accessToken
     * 
     * @return
     */
    private static String accessToken() {
        String token = RedisUtil.getString(WxchargingPayUtil.KEY_ACCESS_TOKEN);
        if (token == null) {
            HttpClientUtil httpClientUtil = new HttpClientUtil();
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WxchargingPayUtil.wxConfig.getAppID() + "&secret="
                         + WxchargingPayUtil.wxConfig.getAppSecret();
            String result = httpClientUtil.doGet(url, "UTF-8");
            JSONObject json = JSONObject.fromObject(result);
            if (!json.has("errmsg")) {
                token = json.getString("access_token");
                RedisUtil.setString(WxchargingPayUtil.KEY_ACCESS_TOKEN, token, 600);
            } else {
                System.out.println(json.getString("errmsg"));
            }
        }
        return token;
    }

    /**
     * 微信JSSDK调用所需签名
     * 
     * @return
     */
    private static String jsTicket() {
        String ticket = RedisUtil.getString(WxchargingPayUtil.KEY_JSAPI_TICKET);
        if (ticket == null) {
            HttpClientUtil httpClientUtil = new HttpClientUtil();
            String token = accessToken();
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + token + "&type=jsapi";
            String result = httpClientUtil.doGet(url, "UTF-8");
            JSONObject json = JSONObject.fromObject(result);
            if (json.getInt("errcode") == 0) {
                ticket = json.getString("ticket");
                RedisUtil.setString(WxchargingPayUtil.KEY_JSAPI_TICKET, ticket, 600);
            } else {
                System.out.println(json.getString("errmsg"));
            }
        }
        return ticket;
    }

    private static JSONObject getOpenId(String code) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        Map<String, String> param = new HashMap<String, String>(4);
        param.put("appid", WxchargingPayUtil.wxConfig.getAppID());
        param.put("secret", WxchargingPayUtil.wxConfig.getAppSecret());
        param.put("code", code);
        param.put("grant_type", "authorization_code");
        String result = httpClientUtil.doPost(url, param, "UTF-8");
        JSONObject json = JSONObject.fromObject(result);
        return json;
    }

    private static JSONObject getWechatUserInfo(String openid) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken() + "&openid=" + openid;
        String result = httpClientUtil.doGet(url, "UTF-8");
        JSONObject json = JSONObject.fromObject(result);
        return json;
    }

    /*
     * private static WxConfig getConfig() { if (config == null) { try { config = new WxConfig(); } catch (Exception e)
     * { logger.error("微信WxConfig初始化错误: " + e.getMessage()); e.printStackTrace(); } } return config; }
     */

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }
}
