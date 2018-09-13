package com.holley.wxcharging.dao.bus;

import com.holley.wxcharging.model.bus.BusBikePayment;
import com.holley.wxcharging.model.bus.BusBikePaymentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BusBikePaymentMapper {
    int countByExample(BusBikePaymentExample example);

    int deleteByExample(BusBikePaymentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusBikePayment record);

    int insertSelective(BusBikePayment record);

    List<BusBikePayment> selectByExample(BusBikePaymentExample example);

    BusBikePayment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusBikePayment record, @Param("example") BusBikePaymentExample example);

    int updateByExample(@Param("record") BusBikePayment record, @Param("example") BusBikePaymentExample example);

    int updateByPrimaryKeySelective(BusBikePayment record);

    int updateByPrimaryKey(BusBikePayment record);
}