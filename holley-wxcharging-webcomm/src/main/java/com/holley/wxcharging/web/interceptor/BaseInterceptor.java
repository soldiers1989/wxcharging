package com.holley.wxcharging.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.holley.common.security.SecurityUtil;
import com.holley.wxcharging.common.cache.WxchargingGlobals;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.util.WxchargingLocalUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public abstract class BaseInterceptor extends AbstractInterceptor {

    private static final long   serialVersionUID = 299528989481069713L;
    @SuppressWarnings("unused")
    private static final Logger logger           = Logger.getLogger(BaseInterceptor.class);

    @Override
    public abstract String intercept(ActionInvocation invocation) throws Exception;

    protected String getNamespce() {
        return ServletActionContext.getActionMapping().getNamespace();
    }

    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    protected String getServletPath() {
        HttpServletRequest request = ServletActionContext.getRequest();
        return request.getServletPath();
    }

    protected WxChargingUser getUser(String accountKey) {

        try {
            String userName = SecurityUtil.decrypt(accountKey, WxchargingGlobals.ENCODE_KEY);
            return WxchargingLocalUtil.returnUser(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String getSessionId() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String jsessionID = request.getSession().getId();
        if (jsessionID.length() > 32) {
            jsessionID = jsessionID.substring(0, 32);
        }
        return jsessionID;
    }

    protected String getStringData(String key) {
        HttpServletRequest request = getRequest();
        String data = request.getParameter(key);
        return data != null ? data : "";
    }

    protected void setStringData(String key, String value) {
        HttpServletRequest request = getRequest();
        request.setAttribute(key, value);
    }
}
