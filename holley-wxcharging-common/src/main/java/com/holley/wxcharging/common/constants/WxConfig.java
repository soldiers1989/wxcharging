package com.holley.wxcharging.common.constants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.github.wxpay.sdk.WXPayConfig;
import com.holley.wxcharging.common.cache.WxchargingGlobals;

public class WxConfig implements WXPayConfig {

    private byte[] certData;

    public WxConfig() {
        String certPath = WxchargingGlobals.WX_CERT_PATH;
        File file = new File(certPath);
        InputStream certStream = null;
        try {
            certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (certStream != null) {
                try {
                    certStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getAppSecret() {
        // return "4fda95b051aba3d7bec3958b82bfc850";// 已为服务商（e能家园）最新，请勿修改(2018-05-04)
        // return "51ac434880593ddf97cd87480126b3bf";// 已为服务商（能管家）最新
        // return "b0b3263b003dec489e91111010fbf66e";// 已为（创睿新能源）最新，请勿修改(2018-05-10)
        return WxchargingGlobals.WX_APP_SECRET;
    }

    public String getAppID() {
        // return "wx9881a033828453e0";// e能家园
        // return "wx2934f8c5c33a9ee3";// 能管家
        // return "wx30c09c08b1313c7b";// 创睿新能源
        return WxchargingGlobals.WX_APP_ID;
    }

    public String getMchID() {
        // return "1502968771";// 服务商（e能家园）
        // return "1487258712";// 服务商（能管家）
        // return "1503735471";// 商户（创睿）
        return WxchargingGlobals.WX_MCH_ID;
    }

    public String getKey() {
        // return "jDf6PGpv6vqyK3fAyTwNrO2ctK9WJoqq"; // 已为服务商（e能家园）最新，请勿修改(2018-05-04)
        // return "BaQSHTuL7p41LGqifVW2OxohK2Freyf9"; // 已为服务商（能管家）最新
        // return "lbVQ2NEjD9iC5Bc9EsuAf0IkfXt5dB4N"; // 已为商户（创睿）最新，请勿修改(2018-05-10)
        return WxchargingGlobals.WX_KEY;
    }

    public String getNotifyUrl() {
        return WxchargingGlobals.DOMAIN_URL_PREFIX + "/wxcharging/common/common_wechatNotify.action";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
