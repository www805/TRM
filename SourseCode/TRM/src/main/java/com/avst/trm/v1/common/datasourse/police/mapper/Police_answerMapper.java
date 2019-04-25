package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_answer;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * InnoDB free: 90112 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-04-22
 */
public interface Police_answerMapper extends BaseMapper<Police_answer> {

    @Select("select * from police_answer a where 1=1 ${ew.sqlSegment}")
    List<Police_answer> getAnswerByProblemSsidAndRecordSsid(@Param("ew") EntityWrapper ew);

}
