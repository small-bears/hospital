package com.dgut.service;

import com.dgut.pojo.Orders;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    /**
     * 分页模糊查询所有挂号信息
     */
    HashMap<String, Object> findAllOrders(int pageNumber, int size, String query);
    /**
     * 真正删除挂号信息
     */
    Boolean deleteOrder(int oId);
    /**
     * 增加挂号信息
     */
    Boolean addOrder(Orders order, String arId);
    /**
     * 根据pId查询挂号
     */
    List<Orders> findOrderByPid(int pId) ;
    /**
     * 查看当天挂号列表
     */
    List<Orders> findOrderByNull(int dId, String oStart) ;
    /**
     * 根据id更新挂号信息
     */
    Boolean updateOrder(Orders orders);
    /**
     * 根据id设置缴费状态
     */
    Boolean updatePrice(int oId);
    /**
     * 查找医生已完成的挂号单
     */
    HashMap<String, Object> findOrderFinish(int pageNumber, int size, String query, int dId) ;
    /**
     * 根据dId查询挂号
     */
    HashMap<String, Object> findOrderByDid(int pageNumber, int size, String query, int dId) ;
    /**
     * 统计今天挂号人数
     */
    int orderPeople(String oStart);
    /**
     * 统计今天某个医生挂号人数
     */
    int orderPeopleByDid(String oStart, int dId);
    /**
     * 统计挂号男女人数
     */
    List<String> orderGender();
    /**
     * 增加诊断及医生意见
     */
    Boolean updateOrderByAdd(Orders order);
    /**
     * 判断诊断之后再次购买药物是否已缴费
     */
    Boolean findTotalPrice(int oId);
    /**
     * 请求挂号时间段
     */
    HashMap<String, String> findOrderTime(String arId);
    /**
     * 统计过去20天挂号科室人数
     */
    List<String> orderSection();

}
