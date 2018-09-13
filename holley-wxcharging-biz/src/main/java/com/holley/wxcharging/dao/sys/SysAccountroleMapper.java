package com.holley.wxcharging.dao.sys;

import com.holley.wxcharging.model.sys.SysAccountroleExample;
import com.holley.wxcharging.model.sys.SysAccountroleKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysAccountroleMapper {
    int countByExample(SysAccountroleExample example);

    int deleteByExample(SysAccountroleExample example);

    int deleteByPrimaryKey(SysAccountroleKey key);

    int insert(SysAccountroleKey record);

    int insertSelective(SysAccountroleKey record);

    List<SysAccountroleKey> selectByExample(SysAccountroleExample example);

    int updateByExampleSelective(@Param("record") SysAccountroleKey record, @Param("example") SysAccountroleExample example);

    int updateByExample(@Param("record") SysAccountroleKey record, @Param("example") SysAccountroleExample example);
}