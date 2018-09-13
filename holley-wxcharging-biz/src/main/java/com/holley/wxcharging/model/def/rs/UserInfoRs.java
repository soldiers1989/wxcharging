package com.holley.wxcharging.model.def.rs;

/**
 * 个人中心（个人中心）rs
 * 
 * @author sc
 */
public class UserInfoRs {

    private String userName;
    private String phone;
    private String registerTime;

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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

}
