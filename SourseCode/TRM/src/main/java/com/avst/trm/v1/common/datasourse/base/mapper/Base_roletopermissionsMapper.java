package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_roletopermissions;
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
public interface Base_roletopermissionsMapper extends BaseMapper<Base_roletopermissions> {

    @Select("select * from base_permissions p where p.ssid in(select rp.permissionsssid from base_roletopermissions rp where 1=1 ${ew.sqlSelect})")
    List<Base_roletopermissions> getPermissionsByRoleSsid(@Param("ew") EntityWrapper ew);

}
