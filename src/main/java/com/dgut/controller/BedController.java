package com.dgut.controller;

import com.dgut.pojo.Bed;
import com.dgut.service.BedService;
import com.dgut.utils.ResponseData;
import com.dgut.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bed")
public class BedController {
    @Autowired
    private BedService bedService;

    /**
     * 查找所有空床位
     */
    @RequestMapping("findNullBed")
    public ResponseData findNullBed(){
        return ResponseData.success("查找所有空床位成功", this.bedService.findNullBed());
    }

    /**
     * 增加床位信息
     */
    @RequestMapping("updateBed")
    public ResponseData updateBed(Bed bed) {
        if (this.bedService.updateBed(bed))
        return ResponseData.success("增加床位成功！");
        return ResponseData.fail("增加床位失败！");
    }
    /**
     * 根据pId查询住院
     */
    @RequestMapping("findBedByPid")
    public ResponseData findBedByPid(@RequestParam(value = "pId") int pId){
        return ResponseData.success("根据pId查询住院成功", this.bedService.findBedByPid(pId)) ;
    }
    /**
     * 分页模糊查询所有床位信息
     */
    @RequestMapping("findAllBeds")
    public ResponseData findAllBeds(int pageNumber, int size, String query){
        return ResponseData.success("返回所有床位信息成功", this.bedService.findAllBeds(pageNumber, size, query));
    }
    /**
     * 根据id查找床位
     */
    @RequestMapping("findBed")
    public ResponseData findBed(int bId){
        return ResponseData.success("根据id查找床位成功", this.bedService.findBed(bId));
    }
    /**
      * 增加床位信息
     */
    @RequestMapping("addBed")
    @ResponseBody
    public ResponseData addBed(Bed bed) {
        Boolean bo = this.bedService.addBed(bed);
        if (bo) {
            return ResponseData.success("增加床位信息成功");
        }
        return ResponseData.fail("增加床位信息失败！床号或已被占用");
    }
    /**
     * 删除药物信息
     */
    @RequestMapping("deleteBed")
    public ResponseData deleteBed(@RequestParam(value = "bId") int bId) {
        Boolean bo = this.bedService.deleteBed(bId);
        if (bo){
            return ResponseData.success("删除床位信息成功");
        }
        return ResponseData.fail("删除床位信息失败");
    }
    /**
     * 清空床位信息
     */
    @RequestMapping("emptyBed")
    public ResponseData emptyBed(int bId){
        if(this.bedService.emptyBed(bId))
            return ResponseData.success("清空床位信息成功");
        return ResponseData.fail("清空床位信息失败");
    }
    /**
     * 统计今天挂号人数
     */
    @RequestMapping("bedPeople")
    public ResponseData bedPeople(){
        String bStart = TodayUtil.getTodayYmd();
        return ResponseData.success("统计今天住院人数成功", this.bedService.bedPeople(bStart));
    }
}
