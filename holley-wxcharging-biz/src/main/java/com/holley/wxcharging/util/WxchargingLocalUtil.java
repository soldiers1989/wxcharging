package com.holley.wxcharging.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.holley.wxcharging.common.cache.WxchargingGlobals;
import com.holley.wxcharging.common.constants.UserTypeEnum;
import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.model.bus.BusBikePaymentExample;
import com.holley.wxcharging.model.bus.BusPayment;
import com.holley.wxcharging.model.bus.BusPaymentExample;
import com.holley.wxcharging.model.bus.BusRecharge;
import com.holley.wxcharging.model.bus.BusRechargeExample;
import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.bus.BusUserExample;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.pob.PobChargingPile;
import com.holley.wxcharging.model.pob.PobChargingPileExample;
import com.holley.wxcharging.service.AccountService;
import com.holley.wxcharging.service.ChargingService;
import com.holley.wxcharging.service.DataService;

/**
 * 本地存储工具类
 * 
 * @author sc
 */
public class WxchargingLocalUtil {

    private static ThreadLocal<WxChargingUser>                wxChargingUserLocal = new ThreadLocal<WxChargingUser>();
    private static ConcurrentHashMap<String, WxChargingUser>  userMap             = new ConcurrentHashMap<String, WxChargingUser>();
    private static ConcurrentHashMap<String, PobChargingPile> pileMap             = new ConcurrentHashMap<String, PobChargingPile>();
    private static AccountService                             accountService;
    private static ChargingService                            chargingService;
    private static DataService                                dataService;
    public static boolean                                     isDemo              = false;

    public static void init() {
        initUserMap();
        initPileMap();
        initPayConfig();
    }

    /**
     * 初始化域名
     */
    private static void initPayConfig() {
        // 载入配置
        try {
            Properties p = new Properties();
            p.load(WxchargingLocalUtil.class.getClassLoader().getResourceAsStream("wxcharging.properties"));
            WxchargingGlobals.DOMAIN_URL_PREFIX = p.getProperty("url");
            WxchargingGlobals.WX_CERT_PATH = p.getProperty("wxCertPath");
            WxchargingGlobals.WX_APP_ID = p.getProperty("wxAppId");
            WxchargingGlobals.WX_APP_SECRET = p.getProperty("wxAppSecret");
            WxchargingGlobals.WX_KEY = p.getProperty("wxKey");
            WxchargingGlobals.WX_MCH_ID = p.getProperty("wxMchId");
            WxchargingGlobals.ALI_APP_ID = p.getProperty("aliAppId");
            WxchargingGlobals.ALI_ALIPAY_PUBLIC_KEY = p.getProperty("aliAlipayPublicKey");
            WxchargingGlobals.ALI_RSA_PRIVATE_KEY = p.getProperty("aliRsaPrivateKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化用户信息
     */
    private static void initUserMap() {
        List<BusUser> list = accountService.selectUserByExample(null);
        WxChargingUser wxChargingUser = null;
        for (BusUser user : list) {
            wxChargingUser = new WxChargingUser();
            wxChargingUser.setUserId(user.getId());
            wxChargingUser.setUserName(user.getUsername());
            wxChargingUser.setPhone(user.getPhone());
            userMap.put(wxChargingUser.getUserName(), wxChargingUser);
        }
    }

    /**
     * 初始化桩信息
     */
    private static void initPileMap() {
        List<PobChargingPile> list = chargingService.selectChargingPileByExample(null);
        WxChargingUser wxChargingUser = null;
        for (PobChargingPile pile : list) {
            pileMap.put(pile.getPileCode(), pile);
        }
    }

    public static void setUserLocal(WxChargingUser wxChargingUser) {
        wxChargingUserLocal.set(wxChargingUser);
    }

    public static WxChargingUser getUserLocal() {
        return wxChargingUserLocal.get();
    }

    public static void removeUserLocal() {
        wxChargingUserLocal.remove();
    }

    public static void addUser(WxChargingUser wxChargingUser) {
        userMap.put(wxChargingUser.getUserName(), wxChargingUser);
    }

    public static WxChargingUser returnUser(String userName) {
        if (userMap.containsKey(userName)) {
            return userMap.get(userName);
        } else {
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andUsernameEqualTo(userName);
            cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
            List<BusUser> list = accountService.selectUserByExample(emp);
            if (list != null && !list.isEmpty()) {
                BusUser user = list.get(0);
                WxChargingUser wxChargingUser = new WxChargingUser();
                wxChargingUser.setUserId(user.getId());
                wxChargingUser.setUserName(user.getUsername());
                wxChargingUser.setPhone(user.getPhone());
                userMap.put(userName, wxChargingUser);
                return wxChargingUser;
            }
        }
        return null;
    }

    public static PobChargingPile returnPile(String pileCode) {
        if (pileMap.containsKey(pileCode)) {
            return pileMap.get(pileCode);
        } else {
            PobChargingPileExample emp = new PobChargingPileExample();
            PobChargingPileExample.Criteria cr = emp.createCriteria();
            cr.andPileCodeEqualTo(pileCode);
            List<PobChargingPile> list = chargingService.selectChargingPileByExample(emp);
            if (list != null && !list.isEmpty()) {
                PobChargingPile pobChargingPile = list.get(0);
                pileMap.put(pileCode, pobChargingPile);
                return pobChargingPile;
            }
        }
        return null;
    }

    /**
     * 根据outTradeNo查询BusPayment
     * 
     * @param outTradeNo
     * @return
     */
    public static BusPayment returnPayment(String outTradeNo) {
        String tradeNo = outTradeNo != null ? outTradeNo : "";
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusPayment> list = dataService.selectPaymentByExample(emp);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据outTradeNo查询BusRecharge
     * 
     * @param outTradeNo
     * @return
     */
    public static BusRecharge returnRecharge(String outTradeNo) {
        String tradeNo = outTradeNo != null ? outTradeNo : "";
        BusRechargeExample emp = new BusRechargeExample();
        BusRechargeExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusRecharge> list = dataService.selectRechargeByExample(emp);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据outTradeNo查询BusBikePayment
     * 
     * @param outTradeNo
     * @return
     */
    public static BusBikePayment returnBikePayment(String outTradeNo) {
        String tradeNo = outTradeNo != null ? outTradeNo : "";
        BusBikePaymentExample emp = new BusBikePaymentExample();
        BusBikePaymentExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusBikePayment> list = dataService.selectBikePaymentByExample(emp);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public static void setAccountService(AccountService accountService) {
        WxchargingLocalUtil.accountService = accountService;
    }

    public static void setChargingService(ChargingService chargingService) {
        WxchargingLocalUtil.chargingService = chargingService;
    }

    public static void setDataService(DataService dataService) {
        WxchargingLocalUtil.dataService = dataService;
    }

    public static void setIsDemo(boolean isDemo) {
        WxchargingLocalUtil.isDemo = isDemo;
    }

}
