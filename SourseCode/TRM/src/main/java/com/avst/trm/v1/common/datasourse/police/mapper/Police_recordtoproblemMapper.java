package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtoproblem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Problem;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * InnoDB free: 89088 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-05-17
 */
public interface Police_recordtoproblemMapper extends BaseMapper<Police_recordtoproblem> {

    @Select(" select p.* from police_record r " +
            " INNER JOIN police_recordtoproblem p on p.recordssid=r.ssid " +
            " where 1=1 ${ew.sqlSegment} ")
    List<RecordToProblem> getRecordToProblemByRecordSsid(@Param("ew") EntityWrapper ew);
}
