package com.holley.wxcharging.dao.bus;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.holley.wxcharging.model.bus.BusUser;
import com.holley.wxcharging.model.bus.BusUserExample;

public interface BusUserMapper {

    int countByExample(BusUserExample example);

    int deleteByExample(BusUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusUser record);

    int insertSelective(BusUser record);

    List<BusUser> selectByExample(BusUserExample example);

    BusUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusUser record, @Param("example") BusUserExample example);

    int updateByExample(@Param("record") BusUser record, @Param("example") BusUserExample example);

    int updateByPrimaryKeySelective(BusUser record);

    int updateByPrimaryKey(BusUser record);

}
