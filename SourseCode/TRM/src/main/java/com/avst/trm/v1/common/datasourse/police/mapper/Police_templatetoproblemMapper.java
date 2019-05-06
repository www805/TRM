package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetoproblem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.TemplateToProblem;
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
 * @since 2019-04-09
 */
public interface Police_templatetoproblemMapper extends BaseMapper<Police_templatetoproblem> {

    @Select("select * from police_template t " +
            " left join police_templatetoproblem p on t.id = p.templatessid " +
            " left join police_problem b on p.problemssid = b.id where 1=1 ${ew.sqlSegment} ")
    List<TemplateToProblem> getTemplateToProblems(@Param("ew") EntityWrapper ew);

}
