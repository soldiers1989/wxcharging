package com.holley.wxcharging.dao.bus;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.holley.wxcharging.model.bus.BusUserInfo;
import com.holley.wxcharging.model.bus.BusUserInfoExample;

public interface BusUserInfoMapper {

    int countByExample(BusUserInfoExample example);

    int deleteByExample(BusUserInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusUserInfo record);

    int insertSelective(BusUserInfo record);

    List<BusUserInfo> selectByExample(BusUserInfoExample example);

    BusUserInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusUserInfo record, @Param("example") BusUserInfoExample example);

    int updateByExample(@Param("record") BusUserInfo record, @Param("example") BusUserInfoExample example);

    int updateByPrimaryKeySelective(BusUserInfo record);

    int updateByPrimaryKey(BusUserInfo record);

}
