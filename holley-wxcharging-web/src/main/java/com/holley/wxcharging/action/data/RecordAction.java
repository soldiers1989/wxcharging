package com.holley.wxcharging.action.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.wxcharging.action.BaseAction;
import com.holley.wxcharging.common.constants.RetTypeEnum;
import com.holley.wxcharging.model.def.WxChargingResultBean;
import com.holley.wxcharging.model.def.WxChargingUser;
import com.holley.wxcharging.model.def.rs.ChargingRecordDetailRs;
import com.holley.wxcharging.model.def.rs.ChargingRecordRs;
import com.holley.wxcharging.model.def.rs.RechargeRecordRs;
import com.holley.wxcharging.model.def.vo.ChargingRecordVo;
import com.holley.wxcharging.model.def.vo.RechargeRecordVo;
import com.holley.wxcharging.service.DataService;
import com.holley.wxcharging.util.WxchargingUtil;

/**
 * 数据记录相关ACTION
 * 
 * @author sc
 */
public class RecordAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private final static Logger logger           = Logger.getLogger(RecordAction.class);
    @Resource
    private DataService         dataService;

    // /**
    // * 充电记录页面
    // *
    // * @return
    // */
    // public String initRechargeRecordPage() {
    // return SUCCESS;
    // }

    /**
     * 订单管理（充电记录）页面
     * 
     * @return
     */
    public String chargingRecord() {
        return SUCCESS;
    }

    /**
     * 充值记录页面
     * 
     * @return
     */

    public String rechargingRecord() {
        return SUCCESS;
    }

    /**
     * 充值记录
     * 
     * @return
     */
    public String queryRechargeRecord() {
        // return value

        // parameter
        int pageIndex = getParamInt("pageIndex");
        WxChargingUser user = getUser();

        // page
        Page page = this.returnPage(pageIndex, limit);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("page", page);

        // query
        map.put("userID", user.getUserId());
        List<RechargeRecordVo> rechargeList = dataService.selectRechargeRecordByPage(map);
        RechargeRecordRs rs = new RechargeRecordRs(page.getTotalProperty(), pageIndex, limit);
        rs.setDatas(rechargeList);
        rs.setTotalMoney(dataService.countRechargeRecord(map));
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 订单管理（充电记录）
     * 
     * @return
     */
    public String queryChargingRecord() {
        // parameter
        String keyWord = getParameter("keyWord");
        String dateTime = getParameter("dateTime");
        int pageIndex = getParamInt("pageIndex");
        int chargeType = getParamInt("chargeType");
        WxChargingUser user = getUser();

        Date date = DateUtil.StrToDate(dateTime, DateUtil.MONTHTIME);

        // page
        Page page = this.returnPage(pageIndex, limit);
        Map<String, Object> map = new HashMap<String, Object>(7);
        map.put("page", page);
        if (StringUtil.isNotEmpty(keyWord)) {
            map.put("keyword", keyWord);
        }

        // query
        map.put("userid", user.getUserId());
        map.put("starttime", date);
        map.put("endtime", WxchargingUtil.getLastSecondOfMonth(date));
        map.put("chargeType", chargeType);
        map.put("dealStatus", ChargeDealStatusEnum.SUCCESS.getValue());
        List<ChargingRecordVo> recordList = dataService.selectPaymentRecordByPage(map);
        ChargingRecordRs rs = new ChargingRecordRs(page.getTotalProperty(), pageIndex, limit);
        rs.setDatas(recordList);
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    /**
     * 订单详细信息
     * 
     * @return
     */
    public String queryChargingRecordDetail() {
        // parameter
        int recordID = getParamInt("chargingRecordId");
        if (recordID <= 0) {
            wxChargingResultBean.setRet(RetTypeEnum.PARAM_ERROR.getValue());
            return SUCCESS;
        }
        // query
        ChargingRecordDetailRs rs = dataService.selectPaymentDetailByPrimaryKey(recordID);
        wxChargingResultBean.setData(rs);
        return SUCCESS;
    }

    public WxChargingResultBean getWxChargingResultBean() {
        return wxChargingResultBean;
    }
}
