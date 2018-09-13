package com.holley.wxcharging.model.def.rs;

/**
 * 登录rs
 * 
 * @author sc
 */
public class IsLoginRs extends FailReasonBase {

    private int    loginStatus; // 1:登录2：未登录
    private String accountKey;

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

}
