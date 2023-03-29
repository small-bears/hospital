package com.dgut.controller;

import com.dgut.pojo.Drug;
import com.dgut.service.DrugService;
import com.dgut.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("drug")
public class DrugController {
    @Autowired
    private DrugService drugService;
    /**
     * 分页模糊查询所有药物信息
     */
    @RequestMapping("findAllDrugs")
    public ResponseData findAllDrugs(int pageNumber, int size, String query){
        return ResponseData.success("返回所有药物信息成功", this.drugService.findAllDrugs(pageNumber, size, query));
    }
    /**
     * 根据id查找药物
     */
    @RequestMapping("findDrug")
    public ResponseData findDrug(int drId){
        return ResponseData.success("根据id查找药物成功", this.drugService.findDrug(drId));
    }
    /**
     * 根据id删除药物数量
     */
    @RequestMapping("reduceDrugNumber")
    public ResponseData reduceDrugNumber(int drId,int usedNumber){
        if (this.drugService.reduceDrugNumber(drId, usedNumber))
            return ResponseData.success("根据id删除药物数量成功");
        return ResponseData.fail("根据id删除药物数量失败");
    }
    /**
     * 增加药物信息
     */
    @RequestMapping("addDrug")
    @ResponseBody
    public ResponseData addDrug(Drug drug) {
        Boolean bo = this.drugService.addDrug(drug);
        if (bo) {
            return ResponseData.success("增加药物信息成功");
        }
        return ResponseData.fail("增加药物信息失败！账号或已被占用");
    }
    /**
     * 删除药物信息
     */
    @RequestMapping("deleteDrug")
    public ResponseData deleteDrug(@RequestParam(value = "drId") int drId) {
        Boolean bo = this.drugService.deleteDrug(drId);
        if (bo){
            return ResponseData.success("删除药物信息成功");
        }
        return ResponseData.fail("删除药物信息失败");
    }
    /**
     * 修改药物信息
     */
    @RequestMapping("modifyDrug")
    @ResponseBody
    public ResponseData modifyDrug(Drug drug) {
        this.drugService.modifyDrug(drug);
        return ResponseData.success("修改药物信息成功");
    }
}
