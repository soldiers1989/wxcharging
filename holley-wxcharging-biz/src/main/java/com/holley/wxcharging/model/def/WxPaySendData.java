package com.holley.wxcharging.model.def;

/**
 * 微信支付参数
 * 
 * @author LHP
 */
public class WxPaySendData {

    // 公众账号ID
    private String appid;
    // 随机字符串
    private String nonce_str;
    // 签名
    private String sign;
    // 签名类型：MD5
    private String signType;
    // 时间戳
    private String timeStamp;
    // 微信支付时用到的prepay_id
    private String packageStr;
    // 返回码
    private String result_code;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

}
