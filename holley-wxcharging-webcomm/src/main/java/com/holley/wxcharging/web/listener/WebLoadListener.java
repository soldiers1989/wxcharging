package com.holley.wxcharging.web.listener;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.holley.wxcharging.common.cache.WxchargingGlobals;

public class WebLoadListener implements ServletContextListener {

    private final static Logger logger = Logger.getLogger(WebLoadListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        Properties p = new Properties();
        try {
            p.load(WebLoadListener.class.getClassLoader().getResourceAsStream("wxcharging.properties"));
            WxchargingGlobals.WX_VERSION = p.getProperty("wxVersion");
            logger.info("当前版本号：" + WxchargingGlobals.WX_VERSION);
            context.setAttribute("version", WxchargingGlobals.WX_VERSION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
