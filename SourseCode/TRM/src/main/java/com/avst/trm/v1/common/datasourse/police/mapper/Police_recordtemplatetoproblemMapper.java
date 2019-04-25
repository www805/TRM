package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtemplatetoproblem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
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
public interface Police_recordtemplatetoproblemMapper extends BaseMapper<Police_recordtemplatetoproblem> {

    @Select(" select p.* from police_record r " +
            " left join police_recordtemplatetoproblem  rtp on rtp.recordtemplatessid=r.recordtemplatessid " +
            " left join police_problem p on p.ssid=rtp.problemssid " +
            " where 1=1 ${ew.sqlSegment} ")
   List<Problem> getProblemByRecordSsid(@Param("ew") EntityWrapper ew);

}
