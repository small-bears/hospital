package com.dgut.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgut.mapper.OrderMapper;
import com.dgut.pojo.Orders;
import com.dgut.service.OrderService;
import com.dgut.utils.RandomUtil;
import com.dgut.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private JedisPool jedisPool;//redis连接池
    /**
     * 分页模糊查询所有挂号信息
     */
    @Override
    public HashMap<String, Object> findAllOrders(int pageNumber, int size, String query) {
        Page<Orders> page = new Page<>(pageNumber, size);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query);
        IPage<Orders> iPage = this.orderMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("orders", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 删除挂号信息
     */
    @Override
    public Boolean deleteOrder(int oId) {
        this.orderMapper.deleteById(oId);
        return true;
    }
    /**
     * 增加挂号信息
     */
    @Override
    public Boolean addOrder(Orders order, String arId){
        //redis开始
        Jedis jedis = jedisPool.getResource();
        String time = order.getOStart().substring(11, 22);
        synchronized (this) {
            if (time.equals("08:30-09:30")) {
                if (jedis.hget(arId, "eTOn").equals("0"))
                    return false;
                jedis.hincrBy(arId, "eTOn", -1);
            }

            if (time.equals("09:30-10:30")) {
                if (jedis.hget(arId, "nTOt").equals("0"))
                    return false;
                jedis.hincrBy(arId, "nTOt", -1);
            }
            if (time.equals("10:30-11:30")) {
                if (jedis.hget(arId, "tTOe").equals("0"))
                    return false;
                jedis.hincrBy(arId, "tTOe", -1);
            }
            if (time.equals("14:30-15:30")) {
                if (jedis.hget(arId, "fTOf").equals("0"))
                    return false;
                jedis.hincrBy(arId, "fTOf", -1);
            }
            if (time.equals("15:30-16:30")) {
                if (jedis.hget(arId, "fTOs").equals("0"))
                    return false;
                jedis.hincrBy(arId, "fTOs", -1);
            }
            if (time.equals("16:30-17:30")) {
                if (jedis.hget(arId, "sTOs").equals("0"))
                    return false;
                jedis.hincrBy(arId, "sTOs", -1);
            }
        }
        jedis.close();
        //redis结束
        order.setOId(RandomUtil.randomOid(order.getPId()));
        order.setOState(0);
        order.setOPriceState(0);
        order.setOStart(order.getOStart().substring(0,22));
        this.orderMapper.insert(order);
        return true;
    }
    /**
     * 根据pId查询挂号
     */
    public List<Orders> findOrderByPid(int pId){

        return this.orderMapper.findOrderByPid(pId);
    }
    /**
     * 查看当天挂号列表
     */
    @Override
    public List<Orders> findOrderByNull(int dId, String oStart){
        return this.orderMapper.findOrderByNull(dId, oStart);
    }
    /**
     * 根据id更新挂号信息
     */
    @Override
    public Boolean updateOrder(Orders orders) {
        orders.setOState(1);
        orders.setOEnd(TodayUtil.getToday());
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("o_id", orders.getOId());
        this.orderMapper.update(orders, wrapper);
        return true;
    }
    /**
     * 根据id设置缴费状态
     */
    @Override
    public Boolean updatePrice(int oId){
        /**
         * 用QueryWrapper如果不把外键的值也传进来，会报错
         * 用UpdateWrapper就正常
         */
        UpdateWrapper<Orders> wrapper = new UpdateWrapper<>();
        wrapper.eq("o_id", oId).set("o_price_state", 1).set("o_total_price", 0.00);
        int i = this.orderMapper.update(null, wrapper);
        System.out.println("影响行数"+i);
        return true;
    }
    /**
     * 查找医生已完成的挂号单
     */
    @Override
    public HashMap<String, Object> findOrderFinish(int pageNumber, int size, String query, int dId){
        Page<Orders> page = new Page<>(pageNumber, size);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query).eq("d_id", dId).orderByDesc("o_start").eq("o_state", 1);
        IPage<Orders> iPage = this.orderMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("orders", iPage.getRecords()); //查询到的记录

        return hashMap;
    }
    /**
     * 根据dId查询挂号
     */
    public HashMap<String, Object> findOrderByDid(int pageNumber, int size, String query, int dId){
        Page<Orders> page = new Page<>(pageNumber, size);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query).eq("d_id", dId).orderByDesc("o_start");
        IPage<Orders> iPage = this.orderMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("orders", iPage.getRecords()); //查询到的记录
        return hashMap;
    }
    /**
     * 统计今天挂号人数
     */
    @Override
    public int orderPeople(String oStart){
        return this.orderMapper.orderPeople(oStart);
    }
    /**
     * 统计今天某个医生挂号人数
     */
    @Override
    public int orderPeopleByDid(String oStart, int dId){
        return this.orderMapper.orderPeopleByDid(oStart, dId);
    }
    /**
     * 统计挂号男女人数
     */
    public List<String> orderGender(){
        return this.orderMapper.orderGender();
    }
    /**
     * 增加诊断及医生意见
     */
    public Boolean updateOrderByAdd(Orders order){

        if (this.orderMapper.updateOrderByAdd(order) == 0){
            return false;
        }

        return true;
    }
    /**
     * 判断诊断之后再次购买药物是否已缴费
     */
    public Boolean findTotalPrice(int oId){
        Orders order = this.orderMapper.selectById(oId);
        if (order.getOTotalPrice() != 0.00){
            order.setOPriceState(0);
            this.orderMapper.updateById(order);
            return true;
        }
        return false;
    }
    /**
     * 请求挂号时间段
     */
    @Override
    public HashMap<String, String> findOrderTime(String arId){
        Jedis jedis = jedisPool.getResource();
        return (HashMap<String, String>) jedis.hgetAll(arId);
    }
    /**
     * 统计过去20天挂号科室人数
     */
    @Override
    public List<String> orderSection(){
        String startTime = TodayUtil.getPastDate(20);
        String endTime = TodayUtil.getTodayYmd();
        return this.orderMapper.orderSection(startTime, endTime);
    }
}
