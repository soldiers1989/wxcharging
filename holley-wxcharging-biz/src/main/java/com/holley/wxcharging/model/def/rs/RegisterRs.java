package com.holley.wxcharging.model.def.rs;

/**
 * 注册rs
 * 
 * @author sc
 */
public class RegisterRs extends FailReasonBase {

    private String accountKey; // 加密账户

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

}
