package com.dgut.service;

import com.dgut.pojo.Bed;

import java.util.HashMap;
import java.util.List;

public interface BedService {
    /**
     * 查找所有空床位
     */
    List<Bed> findNullBed();
    /**
     * 更新床位信息
     */
    Boolean updateBed(Bed bed);
    /**
     * 根据pId查询住院信息
     */
    List<Bed> findBedByPid(int pId);
    /**
     * 分页模糊查询所有床位信息
     */
    HashMap<String, Object> findAllBeds(int pageNumber, int size, String query);
    /**
     * 根据id查找床位
     */
    Bed findBed(int bId);
    /**
     * 增加床位信息
     */
    Boolean addBed(Bed bed);
    /**
     * 删除床位信息
     */
    Boolean deleteBed(int bId);
    /**
     * 清空床位信息
     */
    Boolean emptyBed(int bId);
    /**
     * 统计今天住院人数
     */
    int bedPeople(String bStart);
}
