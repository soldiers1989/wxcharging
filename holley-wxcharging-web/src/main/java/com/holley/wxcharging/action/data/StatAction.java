package com.holley.wxcharging.action.data;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.def.rs.StatChargingInfoRs;
import com.holley.wxcharging.service.DataService;

/**
 * 数据统计相关ACTION
 * 
 * @author sc
 */
public class StatAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private final static Logger logger           = Logger.getLogger(StatAction.class);
    @Resource
    private DataService         dataService;

    /**
     * 历史充电数据统计页面
     * 
     * @return
     */
    public String chargingRecordMore() {
        return SUCCESS;
    }

    /**
     * 此接口用于展现费控用户电费详细情况
     * 
     * @return
     */
    public String queryStatChargingInfo() {
        WxChargingUser user = getUser();
        StatChargingInfoRs rs = dataService.selectChargingStatByUserId(user.getUserId());
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }
}
