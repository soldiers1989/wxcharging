package com.holley.wxcharging.util;

import com.holley.wxcharging.common.cache.WxchargingGlobals;

public class AlipayConfig {

    // private static String domainUrl = "http://www.xinyhy.cn";
    private static String domainUrl         = WxchargingGlobals.DOMAIN_URL_PREFIX;
    // 沙箱appid
    // public static String APPID = "2016091200493323";
    // 创睿appid
    public static String  APPID             = WxchargingGlobals.ALI_APP_ID;
    // 沙箱私钥
    // public static String RSA_PRIVATE_KEY =
    // "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC3uMci1YOJz10Xw/EWB9Uie1990YPNkbKfBdC0rPYy2U0Dvi/SIALr0jHD5SOfH+t7kqkdOoQ3kVVpYK9eaG+TxRwl0yemtZOfCH9uRcY2eJ2V3APNMy2119l+fO4v2NRV7c/HX5Q0uZiUTytuifvlM7TG4RBhHkxv7eKAjKr9YZE0363Pc8vaJvEBSNh7p7+OUxmTj13t3njdZKMNqNzAoXidt1l1AIC0w9ur1/ompS4ZMeU1KA9eaSmSP5LDxowIdj0PAB8fQazqo1WHSJF9rWoP0dAC6Nwn0tqehT0iG8NwIQ7qjTxbZGgt1xMUrfi9SKoH94jtSopVWR4rDvlfAgMBAAECggEAaaKSzVKa6TgTvMBq9FHYmJT7WOqIsEAfxhOf5uK0RJRxFb/a2ySQyWr4Njyf+LRvv4OaPTYSI6kqxQZUjyHZVcu13YHFgLNSh9RpAMyZpWRAp5Pzk57jn3TWl0XZ5iT0Idw05dvs6Uv1kQDcZhhsf2L1hIyP0GeNZ6YN4AABqOHejiwv6+GTEvFucPa2WvHB2rpeQx6FJKNVq+f7PSnxNQm1ft9PBHXLLQhBlm3K22MB/YaVFLtSnxFohup3j9m0J+I1gzZt2jq9KiNIKXw6fvXT8QL9TgRRSndHK3fnBVHv7jtiSqN4lMwkSqlxJukwTkwoJKS5r13YapIcKPow4QKBgQD6kWmK771MaYNTkkasfqpdgyIJQ6md6zGv7ArZsIhHgLEK19nu0n3EC6icGpa95sZbcMYbM9F+ALC951SEoQvzVHbyYF5W+ma8KECg4O6jXTfIe+O1m8I6XkI/elT6le1kc80fNGA+hiFsUyTMXTMsCo5tWkLaBo4p2/HhGRj4qQKBgQC7tGLlVgyDWYKMJdUiXuZVVhH7jdZJKtKKzJllG7Wl0dkRhjBeSRKvKaWhtFd4G4Zp+h494hWcZXqE6Ss5p2axv1qTbtbtrnNhqiEJKH0YL247xoJ2MUeLm3/v0Bo7QaXWId/ejPQGYLDGrm40ZgVefox8PCLxUNtGq0B4AT7+xwKBgAs8mCoV5DxYAv6tcaPsrkExT0ZxFVsyn/DOuhHXG9VxDP96fJh8Mg2npDET9Cyh+8VAAnJX2Y44A7UQebbU7g9Ee20VK0XlRtQrTfKpA6qp1IWWILkVOhmZ/oiQON1f+dxmYQ2EMMi9BSSxedX8W/4enxKCWLskTh3MckVw9bC5AoGAITznCj87hgLSqCiG3kg5t0C/fHD4UgqAL83dF9dZShynuH+vjvKXmWbhionZt04AdkGxGzouo3wgvupfGh13hOAilNraukna6p9qrCl8RU0Y0wLq1S0Swzjs5bVO/9E3IXzsHIePKpXyl6wJncO3Da2RCOrJMTwlKBVpHBb0Zb8CgYEA53irrzZp7Zg8Xm4dSKbursNP7PoD8wMPHnOHbVbLi/8FC7Xvt1gOqeSDVv7b4XYXYXqfyDp2y8XuOYR1AAINSm2z2wDKaQ/AvzjqarVcpf2uP8AHQ7lRpvmEsTuxfHB3LZO1ewUs3c+33Pil3HRV4ymqUzChFyjw6bqjY5pbA4Y=";
    // 创睿私钥
    public static String  RSA_PRIVATE_KEY   = WxchargingGlobals.ALI_RSA_PRIVATE_KEY;
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String  notify_url        = domainUrl + "/wxcharging/common/common_alipayNotify.action";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String  return_url        = domainUrl + "/wxcharging/common/alipayReturn.action";
    // 请求网关地址
    public static String  URL               = "https://openapi.alipay.com/gateway.do";
    // 沙箱地址
    // public static String URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static String  CHARSET           = "UTF-8";
    // 返回格式
    public static String  FORMAT            = "json";
    // 创睿公钥
    public static String  ALIPAY_PUBLIC_KEY = WxchargingGlobals.ALI_ALIPAY_PUBLIC_KEY;
    // 沙箱环境
    // public static String ALIPAY_PUBLIC_KEY =
    // "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5aw5rbAihYzyMvgW66NPIQL20HTQN1RWnpH3SzRvV/zsN+d2zBXW2kC6JEQIEGtIv9zobsC0QLo3Xuu9NIEHQVFwVqJunYCw5m27qNmO0i/+G7qyBTAHqfR6SXELuwls0m0xGJc3rjTxgU33WGM94/LtMlZfNI+Q43Lc/1bLNNsJWHBs24ldSAQ9uw1oYoVTUSkR+Vt0pKN1k5WEwnqKEJyILO3vo0Gdd5qH74jZibjcb8+Lr5kC45MN1mznBDa0knSYmO1PoNSgX7cex2/bthuNIPqTVzdY+HSD+lt8ril9Jjk3gyq0eKaqH5WhUR1deeamYGdT762yj1Mr7ruW+wIDAQAB";
    // 日志记录目录
    public static String  log_path          = "/log";
    // RSA2
    public static String  SIGNTYPE          = "RSA2";

}
