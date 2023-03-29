package com.dgut.service;

import com.dgut.pojo.Checks;

import java.util.HashMap;

public interface CheckService {
    /**
     * 分页模糊查询所有检查信息
     */
    HashMap<String, Object> findAllChecks(int pageNumber, int size, String query);
    /**
     * 根据id查找药物
     */
    Checks findCheck(int chId);
    /**
     * 增加检查信息
     */
    Boolean addCheck(Checks checks);
    /**
     * 删除检查信息
     */
    Boolean deleteCheck(int chId);
    /**
     * 修改检查信息
     */
    Boolean modifyCheck(Checks checks);
}
