package com.holley.wxcharging.dao.bus;

import com.holley.wxcharging.model.bus.BusUserWechat;
import com.holley.wxcharging.model.bus.BusUserWechatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BusUserWechatMapper {
    int countByExample(BusUserWechatExample example);

    int deleteByExample(BusUserWechatExample example);

    int deleteByPrimaryKey(String openid);

    int insert(BusUserWechat record);

    int insertSelective(BusUserWechat record);

    List<BusUserWechat> selectByExample(BusUserWechatExample example);

    BusUserWechat selectByPrimaryKey(String openid);

    int updateByExampleSelective(@Param("record") BusUserWechat record, @Param("example") BusUserWechatExample example);

    int updateByExample(@Param("record") BusUserWechat record, @Param("example") BusUserWechatExample example);

    int updateByPrimaryKeySelective(BusUserWechat record);

    int updateByPrimaryKey(BusUserWechat record);
}