package com.dgut.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgut.mapper.DrugMapper;
import com.dgut.pojo.Drug;
import com.dgut.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("DrugService")
public class DrugServiceImpl implements DrugService {
    @Autowired
    private DrugMapper drugMapper;
    /**
     * 分页模糊查询所有药物信息
     */
    @Override
    public HashMap<String, Object> findAllDrugs(int pageNumber, int size, String query){
        Page<Drug> page = new Page<>(pageNumber, size);
        QueryWrapper<Drug> wrapper = new QueryWrapper<>();
        wrapper.like("dr_name", query);
        IPage<Drug> iPage = this.drugMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("size", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("drugs", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 根据id查找药物
     */
    @Override
    public Drug findDrug(int drId){
        return this.drugMapper.selectById(drId);
    }
    /**
     * 根据id删除药物数量
     */
    @Override
    public Boolean reduceDrugNumber(int drId,int usedNumber){
        Drug drug = this.drugMapper.selectById(drId);
        if(drug.getDrNumber() < usedNumber)
            return false;
        drug.setDrNumber(drug.getDrNumber()-usedNumber);
        this.drugMapper.updateById(drug);
        return true;
    }
    /**
     * 增加药物信息
     */
    public Boolean addDrug(Drug drug){
        //如果账号已存在则返回false
        List<Drug> drugs = this.drugMapper.selectList(null);
        for (Drug drug1 : drugs) {
            if (drug.getDrId() == drug1.getDrId()) {
                return false;
            }
        }
        this.drugMapper.insert(drug);
        return true;
    }
    /**
     * 删除药物信息
     */
    @Override
    public Boolean deleteDrug(int drId) {
        this.drugMapper.deleteById(drId);
        return true;
    }
    /**
     * 修改药物信息
     */
    @Override
    public Boolean modifyDrug(Drug drug) {
        int i = this.drugMapper.updateById(drug);
        System.out.println("影响行数："+i);
        return true;
    }
}
