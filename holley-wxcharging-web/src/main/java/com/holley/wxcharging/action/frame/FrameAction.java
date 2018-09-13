package com.holley.wxcharging.action.frame;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.StringUtil;
import com.holley.web.common.util.Validator;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.cache.WxchargingGlobals;
import com.holley.wxcharging.common.constants.LoginStatusEnum;
import com.holley.wxcharging.common.failReason.AccountLoginFailReasonEnum;
import com.holley.wxcharging.common.failReason.ForgetPasswordFailReasonEnum;
import com.holley.wxcharging.common.failReason.IsLoginFailReasonEnum;
import com.holley.wxcharging.common.failReason.PhoneCodeFailReasonEnum;
import com.holley.wxcharging.common.failReason.PhoneCodeLoginFailReasonEnum;
import com.holley.wxcharging.common.failReason.RegisterFailReasonEnum;
import com.holley.wxcharging.common.sms.AliyunSmsUtil;
import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.bus.BusUserExample;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.def.rs.ForgetPasswordRs;
import com.holley.wxcharging.model.def.rs.IsLoginRs;
import com.holley.wxcharging.model.def.rs.LoginRs;
import com.holley.wxcharging.model.def.rs.PhoneCodeRs;
import com.holley.wxcharging.model.def.rs.RegisterRs;
import com.holley.wxcharging.service.AccountService;
import com.holley.wxcharging.util.WxchargingCacheUtil;
import com.holley.wxcharging.util.WxchargingLocalUtil;

/**
 * 用户注册登录相关
 * 
 * @author sc
 */
public class FrameAction extends BaseAction {

    private final static Logger logger = Logger.getLogger(FrameAction.class);
    @Resource
    private AccountService      accountService;

    /**
     * 注册页面
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String register() throws UnsupportedEncodingException {
        return SUCCESS;
    }

    /**
     * 登录页面
     * 
     * @return
     */
    public String login() {
        System.out.println(getParameter("a"));
        return SUCCESS;
    }

    /**
     * 验证码登陆页面
     * 
     * @return
     */
    public String telLogin() {
        return SUCCESS;
    }

    /**
     * 忘记密码页面
     * 
     * @return
     */
    public String forgetPwd() {
        return SUCCESS;
    }

    /**
     * 注册
     * 
     * @return
     */
    public String queryRegister() {
        RegisterRs rs = new RegisterRs();
        String userName = getParameter("userName");
        String phone = getParameter("phone");
        String password = getParameter("password");
        String confirmPassword = getParameter("confirmPassword");
        String phoneCode = getParameter("phoneCode");
        wxChargingResultBean.setData(rs);
        if (StringUtil.isNull(userName, phone, password, confirmPassword, phoneCode)) {
            rs.setFailReason(RegisterFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        if (!Validator.isMobile(phone)) {
            rs.setFailReason(RegisterFailReasonEnum.ERROR_PHONE.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.ERROR_PHONE.getText());
            return SUCCESS;
        }
        if (!Validator.isUsername(userName)) {
            rs.setFailReason(RegisterFailReasonEnum.USERNAME_ERROR.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.USERNAME_ERROR.getText());
            return SUCCESS;
        }
        if (!Validator.isPassword(password)) {
            rs.setFailReason(RegisterFailReasonEnum.PASSWORD_ERROR.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.PASSWORD_ERROR.getText());
            return SUCCESS;
        }
        if (!password.equals(confirmPassword)) {
            rs.setFailReason(RegisterFailReasonEnum.ERROR_CONFIRM_PASSWORD.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.ERROR_CONFIRM_PASSWORD.getText());
            return SUCCESS;
        }
        if (!phoneCode.equals(WxchargingCacheUtil.getPhoneCode(phone))) {
            rs.setFailReason(RegisterFailReasonEnum.ERROR_CODE.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.ERROR_CODE.getText());
            return SUCCESS;
        }

        BusUserExample example = new BusUserExample();
        example.createCriteria().andUsernameEqualTo(userName).andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        if (accountService.countUserByExample(example) > 0) {
            rs.setFailReason(RegisterFailReasonEnum.REPEAT_USERNAME.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.REPEAT_USERNAME.getText());
            return SUCCESS;
        }
        example.clear();
        example.createCriteria().andPhoneEqualTo(phone).andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        if (accountService.countUserByExample(example) > 0) {
            // WxchargingCacheUtil.removePhoneCode(phone);
            rs.setFailReason(RegisterFailReasonEnum.REPEAT_PHONE.getValue());
            rs.setFailReasonDesc(RegisterFailReasonEnum.REPEAT_PHONE.getText());
            return SUCCESS;
        }
        // WxchargingCacheUtil.removePhoneCode(phone);
        BusUser busUser = new BusUser();
        busUser.setUsername(userName);
        busUser.setPhone(phone);
        busUser.setPassword(SecurityUtil.passwordEncrypt(password));
        busUser.setPayPassword(busUser.getPassword());
        busUser.setRegistTime(new Date());
        busUser.setUserType(UserTypeEnum.PERSON.getShortValue());
        busUser.setGroupId(WxchargingGlobals.DEFAULT_WXUSER_GROUPID);// -1默认未微信注册用户
        try {
            if (accountService.insertBusUser(busUser) > 0) {
                WxChargingUser wxChargingUser = new WxChargingUser();
                wxChargingUser.setPhone(busUser.getPhone());
                wxChargingUser.setUserId(busUser.getId());
                wxChargingUser.setUserName(busUser.getUsername());
                WxchargingLocalUtil.addUser(wxChargingUser);
                rs.setAccountKey(encrypt(busUser.getUsername()));
            } else {
                rs.setFailReason(RegisterFailReasonEnum.OTHER.getValue());
                rs.setFailReasonDesc("注册失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            rs.setFailReason(RegisterFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("注册失败");
        }

        return SUCCESS;
    }

    /**
     * 短信验证码发送
     * 
     * @return
     */
    public String queryPhoneCode() {
        String phone = getParameter("phone");
        PhoneCodeRs rs = new PhoneCodeRs();
        wxChargingResultBean.setData(rs);
        if (!Validator.isMobile(phone)) {
            rs.setFailReason(PhoneCodeFailReasonEnum.ERROR_PHONE.getValue());
            rs.setFailReasonDesc(PhoneCodeFailReasonEnum.ERROR_PHONE.getText());
            return SUCCESS;
        }
        long len = WxchargingCacheUtil.getPhoneCodeTtl(phone);
        // 10秒内不能重复操作
        if (180 - len < 10) {
            rs.setFailReason(PhoneCodeFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("操作太频繁请稍后再试");
            return SUCCESS;
        }
        String code = StringUtil.randomNumber(4);
        logger.info("验证码：" + code);
        if (WxchargingLocalUtil.isDemo) {// 开发模式
            WxchargingCacheUtil.setPhoneCode(phone, code);
        } else {
            if (AliyunSmsUtil.sendVerificationCode(phone, code)) {
                WxchargingCacheUtil.setPhoneCode(phone, code);
            } else {
                rs.setFailReason(PhoneCodeFailReasonEnum.OTHER.getValue());
                rs.setFailReasonDesc("短信发送失败");
                return SUCCESS;
            }
        }

        return SUCCESS;
    }

    /**
     * 账号登陆
     * 
     * @return
     */
    public String queryAccountLogin() {
        String account = getParameter("account");
        String password = getParameter("password");
        String openId = getParameter("openId");
        LoginRs rs = new LoginRs();
        wxChargingResultBean.setData(rs);
        if (StringUtil.isNull(account, password, openId)) {
            rs.setFailReason(AccountLoginFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        BusUser busUser = null;
        List<BusUser> list = null;
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        cr.andGroupIdEqualTo(WxchargingGlobals.DEFAULT_WXUSER_GROUPID);
        if (Validator.isMobile(account)) {
            cr.andPhoneEqualTo(account);
            list = accountService.selectUserByExample(emp);
        } else {
            cr.andUsernameEqualTo(account);
            list = accountService.selectUserByExample(emp);
        }
        busUser = list.isEmpty() ? null : list.get(0);

        if (busUser == null) {
            rs.setFailReason(AccountLoginFailReasonEnum.ERROR_ACCOUNT.getValue());
            rs.setFailReasonDesc(AccountLoginFailReasonEnum.ERROR_ACCOUNT.getText());
            return SUCCESS;
        }
        String loginUser = WxchargingCacheUtil.getLoginUserId(busUser.getUsername());
        if (StringUtil.isNotEmpty(loginUser)) {
            rs.setFailReason(AccountLoginFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("该账户已经登录不能重复登录");
            return SUCCESS;
        }

        if (!busUser.getPassword().equals(SecurityUtil.passwordEncrypt(password))) {
            rs.setFailReason(AccountLoginFailReasonEnum.ERROR_ACCOUNT.getValue());
            rs.setFailReasonDesc(AccountLoginFailReasonEnum.ERROR_ACCOUNT.getText());
            return SUCCESS;
        }
        String accountKey = encrypt(busUser.getUsername());
        rs.setAccountKey(accountKey);
        WxchargingCacheUtil.setLoginUser(openId, accountKey);
        WxchargingCacheUtil.setLoginUserId(busUser.getUsername(), busUser.getId() + "");
        return SUCCESS;
    }

    /**
     * 短信验证码登陆
     * 
     * @return
     */
    public String queryPhoneCodeLogin() {
        String phone = getParameter("phone");
        String phoneCode = getParameter("phoneCode");
        String openId = getParameter("openId");
        LoginRs rs = new LoginRs();
        wxChargingResultBean.setData(rs);
        if (StringUtil.isNull(phone, phoneCode, openId)) {
            rs.setFailReason(PhoneCodeFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        if (!Validator.isMobile(phone)) {
            rs.setFailReason(PhoneCodeFailReasonEnum.ERROR_PHONE.getValue());
            rs.setFailReasonDesc(PhoneCodeFailReasonEnum.ERROR_PHONE.getText());
            return SUCCESS;
        }
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(phone);
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        cr.andGroupIdEqualTo(WxchargingGlobals.DEFAULT_WXUSER_GROUPID);
        List<BusUser> list = accountService.selectUserByExample(emp);
        BusUser user = list.isEmpty() ? null : list.get(0);
        if (user == null) {
            rs.setFailReason(PhoneCodeLoginFailReasonEnum.UNKNOW_PHONE.getValue());
            rs.setFailReasonDesc(PhoneCodeLoginFailReasonEnum.UNKNOW_PHONE.getText());
            return SUCCESS;
        }
        String loginUser = WxchargingCacheUtil.getLoginUserId(user.getUsername());
        if (StringUtil.isNotEmpty(loginUser)) {
            rs.setFailReason(PhoneCodeLoginFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("该账户已经登录不能重复登录");
            return SUCCESS;
        }
        if (!phoneCode.equals(WxchargingCacheUtil.getPhoneCode(phone))) {
            rs.setFailReason(PhoneCodeLoginFailReasonEnum.ERROR_CODE.getValue());
            rs.setFailReasonDesc(PhoneCodeLoginFailReasonEnum.ERROR_CODE.getText());
            return SUCCESS;
        }
        String accountKey = encrypt(user.getUsername());
        WxchargingCacheUtil.removePhoneCode(phone);
        rs.setAccountKey(accountKey);
        WxchargingCacheUtil.setLoginUser(openId, accountKey);
        WxchargingCacheUtil.setLoginUserId(user.getUsername(), user.getId() + "");
        return SUCCESS;

    }

    /**
     * 忘记密码
     * 
     * @return
     */
    public String queryForgetPassword() {
        String phone = getParameter("phone");
        String phoneCode = getParameter("phoneCode");
        String newPassword = getParameter("newPassword");
        String confirmNewPassword = getParameter("confirmNewPassword");
        ForgetPasswordRs rs = new ForgetPasswordRs();
        wxChargingResultBean.setData(rs);
        if (StringUtil.isNull(phone, phoneCode, newPassword, confirmNewPassword)) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        if (!Validator.isMobile(phone)) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.ERROR_PHONE.getValue());
            rs.setFailReasonDesc(ForgetPasswordFailReasonEnum.ERROR_PHONE.getText());
            return SUCCESS;
        }
        if (!Validator.isPassword(newPassword)) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.ERROR_PASSWORD.getValue());
            rs.setFailReasonDesc(ForgetPasswordFailReasonEnum.ERROR_PASSWORD.getText());
            return SUCCESS;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.ERROR_CONFIRM_PASSWORD.getValue());
            rs.setFailReasonDesc(ForgetPasswordFailReasonEnum.ERROR_CONFIRM_PASSWORD.getText());
            return SUCCESS;
        }
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(phone);
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        List<BusUser> list = accountService.selectUserByExample(emp);
        BusUser busUser = list.isEmpty() ? null : list.get(0);
        if (busUser == null) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.UNKNOW_PHONE.getValue());
            rs.setFailReasonDesc(ForgetPasswordFailReasonEnum.UNKNOW_PHONE.getText());
            return SUCCESS;
        }
        if (!phoneCode.equals(WxchargingCacheUtil.getPhoneCode(phone))) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.ERROR_CODE.getValue());
            rs.setFailReasonDesc(ForgetPasswordFailReasonEnum.ERROR_CODE.getText());
            return SUCCESS;
        }
        WxchargingCacheUtil.removePhoneCode(phone);
        BusUser newBusUser = new BusUser();
        newBusUser.setId(busUser.getId());
        newBusUser.setPassword(SecurityUtil.passwordEncrypt(newPassword));
        if (accountService.updateUserByPrimaryKeySelective(newBusUser) <= 0) {
            rs.setFailReason(ForgetPasswordFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("操作失败");
        }
        return SUCCESS;
    }

    public String queryIsLogin() {
        String openId = getParameter("openId");
        IsLoginRs rs = new IsLoginRs();
        wxChargingResultBean.setData(rs);
        if (StringUtil.isEmpty(openId)) {
            rs.setFailReason(IsLoginFailReasonEnum.OTHER.getValue());
            rs.setFailReasonDesc("缺少必要参数");
            return SUCCESS;
        }
        String accountKey = WxchargingCacheUtil.getLoginUser(openId);
        if (StringUtil.isNotEmpty(accountKey)) {
            rs.setAccountKey(accountKey);
            rs.setLoginStatus(LoginStatusEnum.LOGIN.getValue());
        } else {
            rs.setLoginStatus(LoginStatusEnum.LOGIN_OUT.getValue());
        }
        return SUCCESS;
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }
}
