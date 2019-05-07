package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problemtype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problemtype;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
public interface Police_problemtypeMapper extends BaseMapper<Police_problemtype> {

    @Select("select pp.* from police_problem p left join police_problemtotype pr on p.id = pr.problemtypessid left join police_problemtype pp on pr.problemssid = pp.id where 1=1 ${ew.sqlSegment}" )
    List<Problemtype> getProblemTypeById(Page page, @Param("ew") EntityWrapper ew);

    @Select("select count(pp.id) from police_problem p left join police_problemtotype pr on p.id = pr.problemtypessid left join police_problemtype pp on pr.problemssid = pp.id where 1=1 ${ew.sqlSegment}")
    int countgetProblemTypeById( @Param("ew") EntityWrapper ew);

}
