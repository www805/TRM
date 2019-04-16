package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetoproblem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.TemplateToProblem;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplateToProblemsParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;

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

    List<TemplateToProblem> getTemplateToProblems(GetTemplateToProblemsParam param);

}
