package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetProblemsParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * <p>
 * InnoDB free: 90112 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-04-09
 */
public interface Police_problemMapper extends BaseMapper<Police_problem> {
    List<Problem>  getProblemList(Page page, GetProblemsParam getProblemsParam);

    int countgetProblemList(GetProblemsParam getProblemsParam);
}
