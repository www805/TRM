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
    @Select("select a.* ,ar.id admintoroleid,ar.adminssid,ar.rolessid " +
            "from base_admininfo a left join base_admintorole ar on a.ssid=ar.adminssid ")
    public List<AdminAndAdmintorole> getAdminAndAdmintorolelist(Page page, EntityWrapper ew);


    /**
     * 笔录统计
     * @return
     */
    @Select("select a.* from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
            "where 1=1 ${ew.sqlSegment}" )
    public List<Base_arraignmentCount> getArraignmentCountList(Page page, @Param("ew") EntityWrapper ew);


    @Select("select count(re.id) recordCount,count(re.id) recordrealCount ,sum(re.recordtime) recordtime,sum(r.time) time, sum(CHAR_LENGTH(r.translatext)) translatext " +
            "from base_admininfo a left join police_recordreal r on a.ssid = r.userssid " +
            "left join police_record re on re.id = r.recordssid where a.id = 1" )
    public List<Base_arraignmentCount> getArraignmentCount(Page page, @Param("ew") EntityWrapper ew);

//    @Select("select a.* from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
//            "where 1=1 ${ew.sqlSegment}" )
//    public List<Base_arraignmentCount> getList(Page page,@Param("ew") EntityWrapper ew);

    /**
     * 登录校验，账号密码
     * @param ew
     * @return
     */
    @Select("select * from base_admininfo a " +
            "left join base_admintorole ar " +
            "on a.ssid=ar.adminssid where 1=1 ${ew.sqlSegment}" )
    public AdminManage_session getAdminAndAdmintorole(@Param("ew") EntityWrapper ew);



    /******************************************************/
     @Select("select  DISTINCT(a.id),a.*,w.workname as 'workname'" +
             "from base_admininfo a " +
             "left join police_workunit w on a.workunitssid=w.ssid " +
             "left join base_admintorole ar on ar.adminssid=a.ssid " +
             "left join base_role r on r.ssid=ar.rolessid " +
             "where 1=1 ${ew.sqlSegment}")
     List<AdminAndWorkunit> getUserList(Page page, @Param("ew") EntityWrapper ew);

     @Select("select count(DISTINCT(a.id))" +
             "from base_admininfo a " +
             "left join police_workunit w on a.workunitssid=w.ssid " +
             "left join base_admintorole ar on ar.adminssid=a.ssid " +
             "left join base_role r on r.ssid=ar.rolessid " +
             "where 1=1 ${ew.sqlSegment}")
     int countgetUserList(@Param("ew") EntityWrapper ew);



}
