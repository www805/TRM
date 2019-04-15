package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.mapper.param.GetAdminAndAdmintorolelistParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
    @Select("select a.* ,ar.id admintoroleid,ar.adminid,ar.roleid from base_admininfo a left join base_admintorole ar on a.id=ar.adminid ")
    public List<AdminAndAdmintorole> getAdminAndAdmintorolelist(Page page, EntityWrapper ew);

    @Select("select count(a.id) from base_admininfo a " +
            "left join base_admintorole ar " +
            "on a.id=ar.adminid " )
    public int getAdminAndAdmintorolecount(EntityWrapper ew);
}
