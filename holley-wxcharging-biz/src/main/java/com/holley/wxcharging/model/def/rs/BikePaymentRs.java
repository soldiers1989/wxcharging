package com.holley.wxcharging.model.def.rs;

import com.holley.wxcharging.model.def.WxPaySendData;

/**
 * @author LHP
 */
public class BikePaymentRs extends FailReasonBase {

    private String        form;
    private WxPaySendData wxData;
    private String        tradeNo;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
