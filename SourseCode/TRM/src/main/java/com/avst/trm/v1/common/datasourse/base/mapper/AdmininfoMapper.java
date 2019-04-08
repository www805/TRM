package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdmintorole;
import com.avst.trm.v1.common.datasourse.base.mapper.param.GetAdminAndAdmintorolelistParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <说明.txt>
 * InnoDB free: 39936 kB Mapper 接口
 * </说明.txt>
 *
 * @author Mht
 * @since 2019-04-03
 */
public interface AdmininfoMapper extends BaseMapper<Admininfo> {

    @Select("select a.* ,ar.id admintoroleid,ar.adminid,ar.roleid from admininfo a left join admintorole ar on a.id=ar.adminid where a.id != ${adminid}")
    public List<AdminAndAdmintorole> getAdminAndAdmintorolelist(Page page, GetAdminAndAdmintorolelistParam param);

    @Select("select count(a.id) from admininfo a " +
            "left join admintorole ar " +
            "on a.id=ar.adminid " +
            "where a.id != ${adminid}")
    public int getAdminAndAdmintorolecount(GetAdminAndAdmintorolelistParam param);

}
