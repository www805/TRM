package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.mapper.param.GetAdminAndAdmintorolelistParam;
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
}
