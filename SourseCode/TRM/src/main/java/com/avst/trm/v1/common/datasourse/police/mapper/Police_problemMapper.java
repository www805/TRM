package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
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
 * @since 2019-04-09
 */
public interface Police_problemMapper extends BaseMapper<Police_problem> {
    @Select("select DISTINCT(p.id),p.*,pp.problemtypessid from police_problem p LEFT JOIN police_problemtotype pp  ON pp.problemssid=p.ssid where 1=1 ${ew.sqlSegment}" )
    List<Problem>  getProblemList(Page page, @Param("ew") EntityWrapper ew);

    @Select("select count(DISTINCT(p.id)) from police_problem p  LEFT JOIN police_problemtotype pp  ON pp.problemssid=p.ssid where 1=1 ${ew.sqlSegment}")
    int countgetProblemList( @Param("ew") EntityWrapper ew);

    @Select("select * from police_problem where 1=1 ${ew.sqlSegment}")
    Problem getProblemByEw( @Param("ew") EntityWrapper ew);
}
