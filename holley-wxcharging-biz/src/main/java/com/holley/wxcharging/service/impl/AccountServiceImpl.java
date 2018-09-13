package com.holley.wxcharging.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import com.holley.common.constants.RoleEnum;
import com.holley.wxcharging.dao.bus.BusAccountLogMapper;
import com.holley.wxcharging.dao.bus.BusAccountMapper;
import com.holley.wxcharging.dao.bus.BusUserInfoMapper;
import com.holley.wxcharging.dao.bus.BusUserMapper;
import com.holley.wxcharging.dao.sys.SysAccountroleMapper;
import com.holley.wxcharging.model.bus.BusAccount;
import com.holley.wxcharging.model.bus.BusAccountLog;
import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.bus.BusUserExample;
import com.holley.wxcharging.model.bus.BusUserInfo;
import com.holley.wxcharging.model.sys.SysAccountroleKey;
import com.holley.wxcharging.service.AccountService;

public class AccountServiceImpl implements AccountService {

    @Resource
    private BusUserMapper        busUserMapper;
    @Resource
    private BusUserInfoMapper    busUserInfoMapper;
    @Resource
    private BusAccountMapper     busAccountMapper;
    @Resource
    private SysAccountroleMapper sysAccountroleMapper;
    @Resource
    private BusAccountLogMapper  busAccountLogMapper;

    @Override
    public List<BusUser> selectUserByExample(BusUserExample example) {
        return busUserMapper.selectByExample(example);
    }

    @Override
    public BusUser selectUserByPrimaryKey(Integer id) {
        return busUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public BusUserInfo selectUserInfoByPrimaryKey(Integer id) {
        return busUserInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public BusAccount selectAccountByPrimaryKey(Integer userId) {
        return busAccountMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int insertBusUser(BusUser bususer) {
        int count = 0;
        // 初始化用户信息
        BusUserInfo bususerinfo = new BusUserInfo();
        bususerinfo.setRealName("微信注册");
        count += busUserInfoMapper.insert(bususerinfo);
        // 初始化用户
        bususer.setInfoId(bususerinfo.getId());
        count += busUserMapper.insertSelective(bususer);
        // 初始化用户账户
        BusAccount busaccount = new BusAccount();
        busaccount.setUserId(bususer.getId());
        busaccount.setUpdateTime(bususer.getRegistTime());
        busaccount.setFreezeMoney(BigDecimal.ZERO);// 冻结金额初始化
        busaccount.setTotalMoney(BigDecimal.ZERO);// 总金额初始化
        busaccount.setUsableMoney(BigDecimal.ZERO);// 可用金额初始化
        count += busAccountMapper.insertSelective(busaccount);

        // 插入权限
        SysAccountroleKey accountRole = new SysAccountroleKey();
        accountRole.setUserid(bususer.getId());
        accountRole.setRoleid(RoleEnum.PERSON.getValue());
        count += sysAccountroleMapper.insert(accountRole);
        return count;
    }

    @Override
    public int updateUserByPrimaryKeySelective(BusUser bususer) {
        return busUserMapper.updateByPrimaryKeySelective(bususer);

    }

    @Override
    public int countUserByExample(BusUserExample example) {
        return busUserMapper.countByExample(example);
    }

    @Override
    public int insertAccountLogSelective(BusAccountLog record) {
        return busAccountLogMapper.insertSelective(record);
    }

    @Override
    public int updateAccountByPrimaryKeySelective(BusAccount record) {
        return busAccountMapper.updateByPrimaryKeySelective(record);
    }

}
