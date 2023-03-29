package com.dgut.controller;

import com.dgut.pojo.Orders;
import com.dgut.service.OrderService;
import com.dgut.utils.ResponseData;
import com.dgut.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 根据id更新挂号信息
     */
    @PostMapping("updateOrder")
    @ResponseBody
    public ResponseData updateOrder(@RequestBody Orders orders) {
        if (this.orderService.updateOrder(orders))
            return ResponseData.success("更新挂号信息成功");

        return ResponseData.fail("更新挂号信息失败！");
    }
    /**
     * 根据id设置缴费状态
     */
    @RequestMapping("updatePrice")
    public ResponseData updatePrice(int oId){
        if (this.orderService.updatePrice(oId))
        return ResponseData.success("根据id设置缴费状态成功");
        return ResponseData.success("根据id设置缴费状态失败");
    }
    /**
     * 查找医生已完成的挂号单
     */
    @RequestMapping("findOrderFinish")
    public ResponseData findOrderFinish(int pageNumber, int size, String query, int dId){
        return ResponseData.success("查找医生已完成的挂号单完成！", this.orderService.findOrderFinish(pageNumber, size, query, dId));
    }
    /**
     * 根据dId查询挂号
     */
    @RequestMapping("findOrderByDid")
    public ResponseData findOrderByDid(int pageNumber, int size, String query, int dId){
        return ResponseData.success("返回挂号信息成功", this.orderService.findOrderByDid(pageNumber, size, query, dId)) ;
    }
    /**
     * 统计今天挂号人数
     */
    @RequestMapping("orderPeople")
    public ResponseData oderPeople(){
        String oStart = TodayUtil.getTodayYmd();
        return ResponseData.success("统计今天挂号人数成功", this.orderService.orderPeople(oStart));
    }
    /**
     * 统计今天某个医生挂号人数
     */
    @RequestMapping("orderPeopleByDid")
    public ResponseData orderPeopleByDid(int dId){
        String oStart = TodayUtil.getTodayYmd();
        return ResponseData.success("统计今天挂号人数成功", this.orderService.orderPeopleByDid(oStart, dId));
    }
    /**
     * 获取过去七天的挂号人数
     */
    @RequestMapping("orderSeven")
    public ResponseData orderSeven(){
        ArrayList<Integer> list = new ArrayList<>();
        String oStart = null;
        for(int i = 20; i > 0;i--){
            oStart = TodayUtil.getPastDate(i);
            int people = this.orderService.orderPeople(oStart);
            list.add(people);
        }
        return ResponseData.success("获取过去20天的挂号人数成功", list);
    }
    /**
     * 统计挂号男女人数
     */
    @RequestMapping("orderGender")
    public ResponseData orderGender(){
        return ResponseData.success("统计挂号男女人数", this.orderService.orderGender());
    }
    /**
     * 增加诊断及医生意见
     */
    @PostMapping("updateOrderByAdd")
    @ResponseBody
    public ResponseData updateOrderByAdd(@RequestBody Orders order){
        if (this.orderService.updateOrderByAdd(order))
            return ResponseData.success("增加诊断及医生意见成功");
        return ResponseData.fail("增加诊断及医生意见失败");
    }
    /**
     * 判断诊断之后再次购买药物是否已缴费
     */
    @RequestMapping("findTotalPrice")
    public ResponseData findTotalPrice(int oId){
       if(this.orderService.findTotalPrice(oId))
           return ResponseData.success("未缴费");
       return ResponseData.fail("无需缴费");
    }
    /**
     * 请求挂号时间段
     */
    @RequestMapping("findOrderTime")
    public ResponseData findOrderTime(String arId){
        return ResponseData.success("请求挂号时间段成功", this.orderService.findOrderTime(arId));

    }
    /**
     * 统计过去20天挂号科室人数
     */
    @RequestMapping("orderSection")
    public ResponseData orderSection(){
        return ResponseData.success("统计过去20天挂号科室人数成功", this.orderService.orderSection());
    }

}
