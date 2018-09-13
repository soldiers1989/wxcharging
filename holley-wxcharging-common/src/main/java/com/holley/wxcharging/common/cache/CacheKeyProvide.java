package com.holley.wxcharging.common.cache;


/**
 * 缓存主键定义规则
 * 
 * @author sc
 */
public class CacheKeyProvide {

    public final static String KEY_PILESTATUS_BEAN          = "keyPileStatusBean_";       // 充电桩状态
    public final static String KEY_WXCHARGEING_IMG_VALIDATE = "keyWxchargingImgValidate_"; // 图片验证码

    public final static String KEY_WXCHARGING_CODE          = "keyWxchargingCode_";       // 登录session
    public final static String KEY_WXCHARGING_BIKE_INFO     = "keyWxChargingBikeInfo_";
    public final static String KEY_WXCHARGING_LOGIN         = "keyWxChargingLogin_";

    public static String getKey(String suffex, String key) {
        return suffex + key;
    }

}
