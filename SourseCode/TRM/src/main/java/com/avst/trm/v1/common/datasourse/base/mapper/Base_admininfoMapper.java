package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_arraignmentCount;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.web.sweb.vo.AdminManage_session;
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
public interface Base_admininfoMapper extends BaseMapper<Base_admininfo> {
    @Select("select a.* ,ar.id admintoroleid,ar.adminssid,ar.rolessid " +
            "from base_admininfo a left join base_admintorole ar on a.ssid=ar.adminssid ")
    public List<AdminAndAdmintorole> getAdminAndAdmintorolelist(Page page, EntityWrapper ew);


    /**
     * 用户表，角色表，用户角色关联表
     * 用户角色关联查询
     * @param page
     * @param ew
     * @return
     */
    @Select("select a.*,ro.rolename " +
            "from base_admininfo a " +
            "left join base_admintorole ar on a.ssid=ar.adminssid " +
            "left join base_role ro on ro.ssid = ar.rolessid ")
    public List<AdminAndAdminRole> getAdminAndAdminRolelist(Page page, EntityWrapper ew);

    @Select("select count(a.id) from base_admininfo a " +
            "left join base_admintorole ar " +
            "on a.ssid=ar.adminssid where 1=1 ${ew.sqlSegment}" )
    public int getAdminAndAdmintorolecount(@Param("ew") EntityWrapper ew);

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
     @Select("select  DISTINCT(a.ssid),a.*,w.workname as 'workname'" +
             "from base_admininfo a " +
             "left join police_workunit w on a.workunitssid=w.ssid " +
             "left join base_admintorole ar on ar.adminssid=a.ssid " +
             "left join base_role r on r.ssid=ar.rolessid " +
             "where 1=1 ${ew.sqlSegment}")
     List<AdminAndWorkunit> getUserList(Page page,@Param("ew") EntityWrapper ew);

     @Select("select count(DISTINCT(a.ssid))" +
             "from base_admininfo a " +
             "left join police_workunit w on a.workunitssid=w.ssid " +
             "left join base_admintorole ar on ar.adminssid=a.ssid " +
             "left join base_role r on r.ssid=ar.rolessid " +
             "where 1=1 ${ew.sqlSegment}")
     int countgetUserList(@Param("ew") EntityWrapper ew);



     @Select("select  a.*,w.workname as 'workname'" +
             " from base_admininfo a " +
             " left join police_workunit w on a.workunitssid=w.ssid " +
             " where 1=1 ${ew.sqlSegment}")
    List<AdminAndWorkunit> getAdminListAndWorkunit(@Param("ew") EntityWrapper ew);

    /******************笔录统计******************/
//    @Select("select count(distinct a.id) from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
    @Select("select count(distinct a.id) from base_admininfo a left join police_arraignment arr on a.ssid = arr.adminssid left join police_record r on arr.recordssid = r.ssid " +
            "where 1=1 ${ew.sqlSegment} " )
    public int getArraignmentCountCount(@Param("ew") EntityWrapper ew);

    /**
     * 笔录统计
     * @return
     */
//    @Select("select * from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
    @Select("select * from base_admininfo a left join police_arraignment arr on a.ssid = arr.adminssid left join police_record r on arr.recordssid = r.ssid " +
            "where 1=1 ${ew.sqlSegment} GROUP BY a.id " )
    public List<Base_arraignmentCount> getArraignmentCountList(Page page, @Param("ew") EntityWrapper ew);

    /**
     * 不带分页的全部人员搜索
     * @param ew
     * @return
     */
    @Select("select * from base_admininfo a left join police_recordreal r on a.ssid = r.userssid left join police_record re on re.id = r.recordssid " +
            "where 1=1 ${ew.sqlSegment} GROUP BY a.id " )
    public List<Base_arraignmentCount> getArraignmentCountListNoPage(@Param("ew") EntityWrapper ew);


//    @Select("select count(re.id) recordCount,count(re.id) recordrealCount ,ifnull(sum(re.recordtime),0) recordtimeCount,ifnull(sum(r.time),0) timeCount, ifnull(sum(CHAR_LENGTH(r.translatext)),0) translatextCount " +
//            "from base_admininfo a " +
//            "left join police_recordreal r on a.ssid = r.userssid " +
//            "left join police_record re on re.id = r.recordssid " +
//            "where 1=1 ${ew.sqlSegment} " )
    @Select("select count(arr.id) recordCount, count(arr.id) recordrealCount,ifnull(sum(r.recordtime),0) recordtimeCount " +
            "from base_admininfo a " +
            "left join police_arraignment arr on a.ssid = arr.adminssid " +
            "left join police_record r on r.ssid = arr.recordssid " +
            "where 1=1 ${ew.sqlSegment} " )
    public Base_arraignmentCount getArraignmentCount(@Param("ew") EntityWrapper ew);

}
