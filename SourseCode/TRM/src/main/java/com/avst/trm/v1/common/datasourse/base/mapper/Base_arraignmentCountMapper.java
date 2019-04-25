package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_arraignmentCount;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.web.vo.AdminManage_session;
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
public interface Base_arraignmentCountMapper extends BaseMapper<Base_admininfo> {

    @Select("select count(distinct a.id) from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
            "where 1=1 ${ew.sqlSegment} " )
    public int getArraignmentCountCount(@Param("ew") EntityWrapper ew);

    /**
     * 笔录统计
     * @return
     */
    @Select("select * from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
            "where 1=1 ${ew.sqlSegment} GROUP BY a.id " )
    public List<Base_arraignmentCount> getArraignmentCountList(Page page, @Param("ew") EntityWrapper ew);

    /**
     * 不带分页的全部人员搜索
     * @param ew
     * @return
     */
    @Select("select * from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
            "where 1=1 ${ew.sqlSegment} GROUP BY a.id " )
    public List<Base_arraignmentCount> getArraignmentCountList(@Param("ew") EntityWrapper ew);


    @Select("select count(re.id) recordCount,count(re.id) recordrealCount ,ifnull(sum(re.recordtime),0) recordtimeCount,ifnull(sum(r.time),0) timeCount, ifnull(sum(CHAR_LENGTH(r.translatext)),0) translatextCount " +
            "from base_admininfo a " +
            "left join police_recordreal r on a.ssid = r.userssid " +
            "left join police_record re on re.id = r.recordssid " +
            "where 1=1 ${ew.sqlSegment} " )
    public Base_arraignmentCount getArraignmentCount(@Param("ew") EntityWrapper ew);

}
