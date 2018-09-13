package com.holley.wxcharging.model.def.rs;

import com.holley.wxcharging.model.def.WxPaySendData;

/**
 * 充电支付rs
 * 
 * @author LHP
 */
public class PaymentRs extends FailReasonBase {

    private String        tradeNo;
    private String        form;
    private WxPaySendData wxData;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public WxPaySendData getWxData() {
        return wxData;
    }

    public void setWxData(WxPaySendData wxData) {
        this.wxData = wxData;
    }

}
