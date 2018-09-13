package com.holley.wxcharging.web.interceptor;

import org.apache.log4j.Logger;

import com.holley.common.util.StringUtil;
import com.holley.wxcharging.common.constants.RetTypeEnum;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.util.WxchargingLocalUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * JSON调用异常拦截
 * 
 * @author sc
 */
public class JsonInterceptor extends BaseInterceptor {

    private static final Logger logger = Logger.getLogger(JsonInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        // getResponse().setHeader("Access-Control-Allow-Origin", "*");//跨域请求设置
        ValueStack st = invocation.getStack();
        WxChargingResultBean bean = (WxChargingResultBean) st.findValue("wxChargingResultBean");
        String result = null;
        String accountKey = getStringData("accountKey");
        if (StringUtil.isEmpty(accountKey)) {
            bean.setRet(RetTypeEnum.USER_ERROR.getValue());
            return "errorjson";
        }
        accountKey = accountKey.replace(' ', '+');
        WxChargingUser user = getUser(accountKey);
        if (user == null) {
            bean.setRet(RetTypeEnum.USER_ERROR.getValue());
            return "errorjson";
        }
        WxchargingLocalUtil.setUserLocal(user);
        try {
            result = invocation.invoke();
        } catch (Exception e) {
            bean.setRet(RetTypeEnum.SYS_ERROR.getValue());
            result = "errorjson";
            e.printStackTrace();
        }
        return result;
    }

    private String getExceptionAllinformation(Exception e) {
        String sOut = "System Error:\n";
        sOut += e.toString() + "\n";
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += s + "\n";
        }
        return sOut;
    }

}
