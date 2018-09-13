package com.holley.wxcharging.util;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.holley.common.cache.RedisUtil;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.wxcharging.common.cache.CacheKeyProvide;

/**
 * redis缓存工具类
 * 
 * @author sc
 */
public class WxchargingCacheUtil extends RedisUtil {

    /**
     * 缓存存储图片验证码，3分钟内有效
     * 
     * @param jsessionId
     * @param code
     */
    public static void setImgValidateCode(String jsessionId, String code) {
        setString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGEING_IMG_VALIDATE, jsessionId), code, EXRP_3M);
    }

    public static String getImgValidateCode(String jsessionId) {
        return getString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGEING_IMG_VALIDATE, jsessionId));
    }

    public static String getPhoneCode(String phone) {
        return getString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_CODE, phone));
    }

    public static void setPhoneCode(String phone, String code) {
        setString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_CODE, phone), code, EXRP_3M);
    }

    public static long getPhoneCodeTtl(String phone) {
        return ttl(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_CODE, phone));
    }

    public static void removePhoneCode(String phone) {
        delKey(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_CODE, phone));
    }

    public static PileStatusBean returnPileStatusBean(Integer pileId) {
        byte[] value = RedisUtil.getByte(CacheKeyProvide.getKey(CacheKeyProvide.KEY_PILESTATUS_BEAN, pileId.toString()).getBytes());
        if (value != null) {
            return (PileStatusBean) SerializationUtils.deserialize(value);
        }
        return null;
    }

    public static void refreshPileStatusBean(PileStatusBean pileStatusBean) {
        RedisUtil.setByte(CacheKeyProvide.getKey(CacheKeyProvide.KEY_PILESTATUS_BEAN, pileStatusBean.getId().toString()).getBytes(), SerializationUtils.serialize(pileStatusBean));
    }

    public static PileStatusEnum returnPileStatus(Integer pileId) {
        if (pileId != null) {
            int status = NumberUtils.toInt(RedisUtil.getString(pileId.toString()));
            return PileStatusEnum.getEnmuByValue(status);
        }
        return null;
    }

    public static void setBikeChargingInfo(String tradeNo, String info) {
        setString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_BIKE_INFO, tradeNo), info, EXRP_DAY);
    }

    public static JSONObject getBikeChargingInfo(String tradeNo) {
        String jsonStr = getString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_BIKE_INFO, tradeNo));
        if (jsonStr != null) {
            return JSON.parseObject(jsonStr);
        }
        return null;
    }

    public static void removeBikeChargingInfo(String tradeNo) {
        delKey(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_BIKE_INFO, tradeNo));
    }

    public static void setLoginUser(String openId, String accountKey) {
        setString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_LOGIN, openId), accountKey);
    }

    public static String getLoginUser(String openId) {
        return getString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_LOGIN, openId));
    }

    public static boolean removeLoginUser(String openId) {
        return delKey(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_LOGIN, openId));
    }

    public static void setLoginUserId(String userName, String userId) {
        setString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_LOGIN, userName), userId);
    }

    public static String getLoginUserId(String userName) {
        return getString(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_LOGIN, userName));
    }

    public static boolean removeLoginUserId(String userName) {
        return delKey(CacheKeyProvide.getKey(CacheKeyProvide.KEY_WXCHARGING_LOGIN, userName));
    }
}
