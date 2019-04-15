package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_template;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetTemplatesParam;
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
public interface Police_templateMapper extends BaseMapper<Police_template> {

    List<Police_template> getTemplateList(Page page, GetTemplatesParam param);

    int countgetTemplateList( GetTemplatesParam param);

}
