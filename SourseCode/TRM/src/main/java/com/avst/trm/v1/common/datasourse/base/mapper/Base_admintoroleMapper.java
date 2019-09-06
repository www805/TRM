package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_admintorole;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
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
public interface Base_admintoroleMapper extends BaseMapper<Base_admintorole> {


    @Select("select * from base_role r where r.ssid in(select ar.rolessid from base_admintorole ar where 1=1 ${ew.sqlSegment})")
    List<Base_role> getRolesByAdminSsid(@Param("ew")EntityWrapper ew);

}
