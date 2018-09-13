package com.holley.wxcharging.service;

import java.util.List;

import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusAccountLog;
import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.bus.BusUserExample;
import com.holley.wxcharging.model.bus.BusUserInfo;

/**
 * 账户相关service
 * 
 * @author sc
 */
public interface AccountService {

    List<BusUser> selectUserByExample(BusUserExample example);

    BusUser selectUserByPrimaryKey(Integer id);

    BusUserInfo selectUserInfoByPrimaryKey(Integer id);

    BusAccount selectAccountByPrimaryKey(Integer userId);

    int insertBusUser(BusUser bususer);

    int updateUserByPrimaryKeySelective(BusUser bususer);

    int countUserByExample(BusUserExample example);

    int insertAccountLogSelective(BusAccountLog record);

    int updateAccountByPrimaryKeySelective(BusAccount record);
}
