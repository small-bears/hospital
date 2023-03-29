package com.dgut.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgut.mapper.BedMapper;
import com.dgut.pojo.Bed;
import com.dgut.service.BedService;
import com.dgut.utils.TodayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("BedService")
public class BedServiceImpl implements BedService {

    @Autowired
    private BedMapper bedMapper;

    /**
     * 查找所有空床位
     */
    @Override
    public List<Bed> findNullBed(){
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.select("b_id").eq("b_state", 0);
        return this.bedMapper.selectList(wrapper);
    }

    /**
     * 增加床位信息
     */
    @Override
    /**
     * 更新床位信息
     */
    public Boolean updateBed(Bed bed){
        Bed bed1 = this.bedMapper.selectById(bed.getBId());
        if (bed1.getBState() == 1)
            return false;
        bed.setBStart(TodayUtil.getTodayYmd());
        bed.setBState(1);
        bed.setVersion(bed1.getVersion());

        this.bedMapper.updateById(bed);
        return true;
    }
    /**
     * 根据pId查询挂号
     */
    public List<Bed> findBedByPid(int pId){
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.bedMapper.selectList(wrapper);
    }
    /**
     * 分页模糊查询所有检查信息
     */
    @Override
    public HashMap<String, Object> findAllBeds(int pageNumber, int size, String query) {
        Page<Bed> page = new Page<>(pageNumber, size);
        QueryWrapper<Bed> wrapper = new QueryWrapper<>();
        wrapper.like("p_id", query);
        IPage<Bed> iPage = this.bedMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("size", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("beds", iPage.getRecords()); //查询到的记录
        return hashMap;
    }
    /**
     * 根据id查找检查
     */
    @Override
    public Bed findBed(int bId){
        return this.bedMapper.selectById(bId);
    }
    /**
     * 增加床位信息
     */
    @Override
    public Boolean addBed(Bed bed){
        //如果账号已存在则返回false
        List<Bed> beds = this.bedMapper.selectList(null);
        for (Bed bed1 : beds) {
            if (bed1.getBId() == bed.getBId()) {
                return false;
            }
        }
        bed.setBState(0);
        this.bedMapper.insert(bed);
        return true;
    }
    /**
     * 删除床位信息
     */
    @Override
    public Boolean deleteBed(int bId) {
        this.bedMapper.deleteById(bId);
        return true;
    }
    /**
     * 清空床位信息
     */
    public Boolean emptyBed(int bId){
        UpdateWrapper<Bed> wrapper = new UpdateWrapper<>();
        wrapper.set("p_id", -1).set("d_id", -1).set("b_reason", null).set("b_start", null).set("b_state", 0).eq("b_id", bId);
        this.bedMapper.update(null, wrapper);
        return true;

    }
    /**
     * 统计今天挂号人数
     */
    @Override
    public int bedPeople(String bStart){
        return this.bedMapper.bedPeople(bStart);
    }

}
