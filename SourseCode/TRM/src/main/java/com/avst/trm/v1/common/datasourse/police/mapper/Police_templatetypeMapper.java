package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_templatetype;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.Template;
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
public interface Police_templatetypeMapper extends BaseMapper<Police_templatetype> {

    //获取第一个模板类型
    @Select("select id from police_templatetype ORDER BY ordernum desc limit 0,1")
    String getTemplateTypeTopId();
}
