package com.dgut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgut.pojo.Arrange;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArrangeMapper extends BaseMapper<Arrange> {

    /**
     * 根据日期查询排班信息
     */
    List<Arrange> findByTime(@Param("ar_time") String arTime, @Param("d_section") String dSection);

}
