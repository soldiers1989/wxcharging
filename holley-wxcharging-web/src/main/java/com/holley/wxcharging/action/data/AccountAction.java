package com.holley.wxcharging.action.data;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.holley.common.security.SecurityUtil;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.failReason.LogOutFailReasonEnum;
import com.holley.wxcharging.common.failReason.ModifyPasswordFailReasonEnum;
import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.def.rs.AccountInfoRs;
import com.holley.wxcharging.model.def.rs.LogoutRs;
import com.holley.wxcharging.model.def.rs.ModifyPasswordRs;
import com.holley.wxcharging.model.def.rs.UserInfoRs;
import com.holley.wxcharging.service.AccountService;
import com.holley.wxcharging.util.WxchargingCacheUtil;

/**
 * 账户数据相关ACTION
 * 
 * @author sc
 */
public class AccountAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private final static Logger logger           = Logger.getLogger(AccountAction.class);
    @Resource
    private AccountService      accountService;

    /**
     * 个人中心（个人信息）页面
     * 
     * @return
     */
    public String userInfo() {
        return SUCCESS;
    }

    /**
     * 账户信息页面
     * 
     * @return
     */
    public String accountInfo() {
        return SUCCESS;
    }

    /**
     * 支付宝支付页面
     * 
     * @return
     */
    public String payfor() {
        return SUCCESS;
    }

    /**
     * 账户余额页面
     * 
     * @return
     */
    public String accountRemain() {
        return SUCCESS;
    }

    /**
     * 修改密码页面
     * 
     * @return
     */
    public String modifyPwd() {
        return SUCCESS;
    }

    /**
     * 余额充值页面
     * 
     * @return
     */
    public String remainRecharge() {
        return SUCCESS;
    }

    /**
     * 查询用户信息
     * 
     * @return
     */
    public String queryUserInfo() {
        // return value
        UserInfoRs rs = new UserInfoRs();

        // parameter
        WxChargingUser user = getUser();

        // query
        BusUser userInfo = accountService.selectUserByPrimaryKey(user.getUserId());
        rs.setUserName(userInfo.getUsername());
        rs.setPhone(userInfo.getPhone());
        rs.setRegisterTime(DateUtil.DateToStr(userInfo.getRegistTime(), DateUtil.TIME_LONG));

        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 查询账户信息
     * 
     * @return
     */
    public String queryAccountInfo() {
        WxChargingUser user = getUser();
        BusAccount account = accountService.selectAccountByPrimaryKey(user.getUserId());
        AccountInfoRs rs = new AccountInfoRs();
        rs.setUsableMoney(account.getUsableMoney());
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 密码修改
     * 
     * @return
     */
    public String queryModifyPassword() {
        WxChargingUser wxChargingUser = getUser();
        String oldPassword = getParameter("oldPassword");
        String newPassword = getParameter("newPassword");
        String confirmNewPassword = getParameter("confirmNewPassword");
        ModifyPasswordRs rs = new ModifyPasswordRs();
        wxChargingResultBean.setData(rs);
        if (StringUtil.isNull(oldPassword, newPassword, confirmNewPassword)) {
            rs.setFailReason(ModifyPasswordFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            rs.setFailReason(ModifyPasswordFailReasonEnum.ERROR_CONFIRM_PASSWORD.getValue());
            rs.setFailReasonDesc(ModifyPasswordFailReasonEnum.ERROR_CONFIRM_PASSWORD.getText());
            return SUCCESS;
        }
        BusUser busUser = accountService.selectUserByPrimaryKey(wxChargingUser.getUserId());
        if (!SecurityUtil.passwordEncrypt(oldPassword).equals(busUser.getPassword())) {
            rs.setFailReason(ModifyPasswordFailReasonEnum.ERROR_PASSWORD.getValue());
            rs.setFailReasonDesc(ModifyPasswordFailReasonEnum.ERROR_PASSWORD.getText());
            return SUCCESS;
        }
        BusUser newBusUser = new BusUser();
        newBusUser.setId(wxChargingUser.getUserId());
        newBusUser.setPassword(SecurityUtil.passwordEncrypt(newPassword));
        if (accountService.updateUserByPrimaryKeySelective(newBusUser) <= 0) {
            rs.setFailReason(ModifyPasswordFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("修改密码失败");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 注销
     * 
     * @return
     */
    public String logout() {
        String openId = getParameter("openId");
        WxChargingUser wxChargingUser = getUser();
        LogoutRs rs = new LogoutRs();
        wxChargingResultBean.setData(rs);
        if (StringUtil.isEmpty(openId)) {
            rs.setFailReason(LogOutFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        WxchargingCacheUtil.removeLoginUser(openId);
        WxchargingCacheUtil.removeLoginUserId(wxChargingUser.getUserName());
        return SUCCESS;
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }
}
