package com.dgut.service;

import com.dgut.pojo.Arrange;

import java.util.List;

public interface ArrangeService {
    /**
     * 根据日期查询排班信息
     */
    List<Arrange> findByTime(String arTime, String dSection);
    /**
     * 增加排班信息
     */
    Boolean addArrange(Arrange arrange);
}
