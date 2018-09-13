package com.holley.wxcharging.dao.bus;

import com.holley.wxcharging.model.bus.BusAccountLog;
import com.holley.wxcharging.model.bus.BusAccountLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BusAccountLogMapper {
    int countByExample(BusAccountLogExample example);

    int deleteByExample(BusAccountLogExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusAccountLog record);

    int insertSelective(BusAccountLog record);

    List<BusAccountLog> selectByExample(BusAccountLogExample example);

    BusAccountLog selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusAccountLog record, @Param("example") BusAccountLogExample example);

    int updateByExample(@Param("record") BusAccountLog record, @Param("example") BusAccountLogExample example);

    int updateByPrimaryKeySelective(BusAccountLog record);

    int updateByPrimaryKey(BusAccountLog record);
}