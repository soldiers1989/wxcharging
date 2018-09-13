package com.holley.wxcharging.common.cache;

public class WxchargingGlobals {

    public static final String ENCODE_KEY               = "ttPfFNeR2DXDT4QU";
    public static final long   MAX_SCAN_LOCK            = 60;                            // 秒
    public static final long   MAX_START_CHARGING_DELAY = 60;                            // 启动充电延迟60秒
    public static final long   MAX_STOP_CHARGING_DELAY  = 60;                            // 停止充电延迟60秒
    public static final int    DEFAULT_WXUSER_GROUPID   = -1;                            // 默认微信注册用户groupId
    public static String       DOMAIN_URL_PREFIX        = "http://charging.s1.natapp.cc"; // 域名前缀
    public static String       WX_CERT_PATH             = "";                            // 微信证书地址
    public static String       WX_APP_SECRET            = "";
    public static String       WX_APP_ID                = "";
    public static String       WX_MCH_ID                = "";
    public static String       WX_KEY                   = "";
    public static String       ALI_APP_ID               = "";
    public static String       ALI_RSA_PRIVATE_KEY      = "";
    public static String       ALI_ALIPAY_PUBLIC_KEY    = "";
    public static String       WX_VERSION    = "1.1.3";
}
