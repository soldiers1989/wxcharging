package com.holley.wxcharging.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.util.DateUtil;
import com.holley.wxcharging.common.constants.BikePayStatusEnum;
import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.model.bus.BusBikePaymentExample;
import com.holley.wxcharging.service.DataService;
import com.holley.wxcharging.util.WxchargingCacheUtil;
import com.holley.wxcharging.util.WxchargingPayUtil;

/**
 * @author LHP
 */
public class RefundTask {

    private final int   TIME_OUT_MINUTE = 5;

    @Resource
    private DataService dataService;

    public void execute() {
        // 查找状态为“已支付-未充电”的订单
        BusBikePaymentExample exp = new BusBikePaymentExample();
        BusBikePaymentExample.Criteria cri = exp.createCriteria();
        cri.andStatusEqualTo(BikePayStatusEnum.PAID_UNCHARGED.getShortValue());
        List<BusBikePayment> bikePaymentList = dataService.selectBikePaymentByExample(exp);
        for (BusBikePayment bike : bikePaymentList) {
            // 根据订单中桩id查询对应桩状态
            PileStatusBean bean = WxchargingCacheUtil.returnPileStatusBean(bike.getPileId());
            if (bean == null) {
                continue;
            }
            // 检查桩状态
            // 检验bean对应情况
            if (PileStatusEnum.CHARGING == bean.getStatus() && bike.getUserId().equals(bean.getUserid()) && bike.getTradeNo().equals(bean.getTradeno())) {
                BusBikePayment updateBike = new BusBikePayment();
                updateBike.setId(bike.getId());
                updateBike.setStatus(BikePayStatusEnum.CHARGED.getShortValue());
                // 更新订单状态
                // bike.setStatus(BikePayStatusEnum.CHARGED.getShortValue());
                // 如果更新失败，可以等待下次循环检查
                dataService.updateBikePayment(updateBike);
                continue;
            }
            // 检查超时情况
            int interval = DateUtil.getIntervalMinutes(new Date(), bike.getAddTime());
            if (interval >= TIME_OUT_MINUTE) {
                // 退款流程
                WxchargingPayUtil.refund(bike.getTradeNo());
            }
        }
    }
}
