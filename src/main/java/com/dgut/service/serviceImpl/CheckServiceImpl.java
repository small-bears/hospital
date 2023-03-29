package com.dgut.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgut.mapper.CheckMapper;
import com.dgut.pojo.Checks;
import com.dgut.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("CheckService")
public class CheckServiceImpl implements CheckService {

    @Autowired
    private CheckMapper checkMapper;
    /**
     * 分页模糊查询所有检查信息
     */
    @Override
    public HashMap<String, Object> findAllChecks(int pageNumber, int size, String query) {
        Page<Checks> page = new Page<>(pageNumber, size);
        QueryWrapper<Checks> wrapper = new QueryWrapper<>();
        wrapper.like("ch_name", query);
        IPage<Checks> iPage = this.checkMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("size", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("checks", iPage.getRecords()); //查询到的记录
        return hashMap;
    }
    /**
     * 根据id查找检查
     */
    @Override
    public Checks findCheck(int chId){
        return this.checkMapper.selectById(chId);
    }
    /**
     * 增加检查信息
     */
    @Override
    public Boolean addCheck(Checks checks){
        //如果账号已存在则返回false
        List<Checks> checks1 = this.checkMapper.selectList(null);
        for (Checks checks2 : checks1) {
            if (checks.getChId() == checks2.getChId()) {
                return false;
            }
        }
        this.checkMapper.insert(checks);
        return true;
    }
    /**
     * 删除检查信息
     */
    @Override
    public Boolean deleteCheck(int chId) {
        this.checkMapper.deleteById(chId);
        return true;
    }
    /**
     * 修改检查信息
     */
    @Override
    public Boolean modifyCheck(Checks checks) {
        int i = this.checkMapper.updateById(checks);
        System.out.println("影响行数："+i);
        return true;
    }
}
