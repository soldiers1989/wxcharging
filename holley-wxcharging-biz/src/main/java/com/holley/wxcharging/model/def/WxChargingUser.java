package com.holley.wxcharging.model.def;

public class WxChargingUser {

    /**
     * 用户内码
     */
    private int    userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
