package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetotype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Templatetype;
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
public interface Police_templatetotypeMapper extends BaseMapper<Police_templatetotype> {

    //getTemplateTypeById

    @Select("select p2.* from police_templatetotype p left join police_templatetype p2 on p.templatetypessid = p2.id where 1=1 ${ew.sqlSegment}")
    List<Templatetype> getTemplateTypeById(@Param("ew") EntityWrapper ew);

}
