package com.avst.trm.v1.common.datasourse.base.mapper;

import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ServerconfigAndType;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * InnoDB free: 90112 kB Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-04-11
 */
public interface Base_serverconfigMapper extends BaseMapper<Base_serverconfig> {
    @Select("select s.*,t.type,t.typename from base_serverconfig s inner join base_type t on s.typeid=t.id ")
    public ServerconfigAndType getServerconfigAndType(EntityWrapper ew);
}
