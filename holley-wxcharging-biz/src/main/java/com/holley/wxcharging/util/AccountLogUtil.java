package com.holley.wxcharging.util;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.constants.charge.FundDirectionEnum;
import com.holley.wxcharging.common.constants.AccountLogTypeEnum;
import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusAccountLog;
import com.holley.wxcharging.service.AccountService;

/**
 * 资金日志
 * 
 * @author zdd
 */
public class AccountLogUtil {

    private static AccountService accountService;
    private static int            result = 0;

    public static int insertAccountLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, AccountLogTypeEnum logType, BigDecimal operateMoney, String ip, Short status) {
        int result = 0;
        switch (logType.getValue()) {
            case 1:// 充值
                result = rechargeAccountLog(fromAccount, recordId, operateMoney, ip);
                break;
            case 2:// 平台账户充电
                result = AccountChargeLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 8:// 资金结算
                result = accountBillLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 12:// 自行车充电扣款
                result = accountBikeBillLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 13:// 自行车充电退款
                result = accountBackBikeBillLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 充值日志
     */
    private static int rechargeAccountLog(BusAccount fromAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.RECHARGE, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 平台账户充电日志
     */
    private static int AccountChargeLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_CHARGING, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        // 平台
        if (toAccount != null) {
            BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_CHARGING, FundDirectionEnum.IN);
            result += accountService.insertAccountLogSelective(toLog);
        }

        return result;
    }

    /**
     * 非平台账户充电日志
     */
    private static int UnAccountChargeLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.UNACC_CHARGING, FundDirectionEnum.NO);
        result += accountService.insertAccountLogSelective(fromLog);
        // 平台
        BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.UNACC_CHARGING, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(toLog);
        return result;
    }

    private static int accountBikeBillLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_BIKE_CHARGING, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    private static int accountBackBikeBillLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_BACK_BIKE_CHARGING, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 资金结算日志
     */
    private static int accountBillLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 平台
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.BILL, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        // 运营商
        BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.BILL, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(toLog);
        return result;
    }

    private static BusAccountLog createLogBean(BusAccount account, Integer recordId, BigDecimal operateMoney, String ip, AccountLogTypeEnum logType, FundDirectionEnum direct) {
        BusAccountLog logBean = new BusAccountLog();
        logBean.setOperateMoney(operateMoney);
        logBean.setTotalMoney(account.getTotalMoney());
        logBean.setUsableMoney(account.getUsableMoney());
        logBean.setFreezeMoney(account.getFreezeMoney());
        logBean.setUserId(account.getUserId());
        logBean.setRecordId(recordId);
        logBean.setType(logType.getShortValue());
        logBean.setRemark(logType.getText());
        logBean.setDirection(direct.getShortValue());
        logBean.setAddIp(ip);
        logBean.setAddTime(new Date());
        return logBean;
    }

    public void setAccountService(AccountService accountService) {
        AccountLogUtil.accountService = accountService;
    }

}
