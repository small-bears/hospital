package com.dgut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgut.pojo.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends BaseMapper<Orders> {
    /**
     * 统计今天挂号人数
     */
    int orderPeople(String oStart);
    /**
     * 统计今天某个医生挂号人数
     */
    int orderPeopleByDid(@Param("o_start") String oStart, @Param("d_id") int dId);
    /**
     * 统计挂号男女人数
     */
    List<String> orderGender();
    /**
     * 根据挂号单号查询挂号
     */
    Orders findOrderByOid(int oId);
    /**
     * 增加诊断及医生意见
     */
    Integer updateOrderByAdd(Orders order);
    /**
     * 统计过去20天挂号科室人数
     */
    List<String> orderSection(@Param("startTime") String startTime, @Param("endTime") String endTime);
    /**
     * 查看当天挂号列表
     */
    List<Orders> findOrderByNull(@Param("dId") int dId, @Param("oStart") String oStart);
    /**
     * 根据pId查询挂号
     */
    List<Orders> findOrderByPid(int pId);

}
