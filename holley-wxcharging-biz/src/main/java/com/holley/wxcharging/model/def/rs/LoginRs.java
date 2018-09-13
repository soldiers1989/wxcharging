package com.holley.wxcharging.model.def.rs;

/**
 * 登录rs
 * 
 * @author sc
 */
public class LoginRs extends FailReasonBase {

    private String accountKey; // 加密账户

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

}
