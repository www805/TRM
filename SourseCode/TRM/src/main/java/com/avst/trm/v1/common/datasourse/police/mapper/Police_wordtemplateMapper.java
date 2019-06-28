package com.avst.trm.v1.common.datasourse.police.mapper;

import com.avst.trm.v1.common.datasourse.police.entity.Police_wordtemplate;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.WordTemplate;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Admin
 * @since 2019-06-27
 */
public interface Police_wordtemplateMapper extends BaseMapper<Police_wordtemplate> {

    @Select(" select count(w.ssid) from police_wordtemplate w  " +
            " left join police_recordtype r on r.ssid=w.recordtypessid " +
            " left join base_filesave f on f.ssid=w.wordtemplate_filesavessid  where 1=1 ${ew.sqlSegment}")
    int countgetWordTemplateList(@Param("ew") EntityWrapper ew);

    @Select(" select w.*,f.recorddownurl as 'wordtemplate_downurl',f.recordrealurl as 'wordtemplate_realurl',r.typename as 'recordtypename' from police_wordtemplate w  " +
            " left join police_recordtype r on r.ssid=w.recordtypessid " +
            " left join base_filesave f on f.ssid=w.wordtemplate_filesavessid  where 1=1 ${ew.sqlSegment}")
    List<WordTemplate> getWordTemplateList(Page page, @Param("ew") EntityWrapper ew);

    @Select(" select w.*,f.recorddownurl as 'wordtemplate_downurl',f.recordrealurl as 'wordtemplate_realurl',r.typename as 'recordtypename' from police_wordtemplate w  " +
            " left join police_recordtype r on r.ssid=w.recordtypessid " +
            " left join base_filesave f on f.ssid=w.wordtemplate_filesavessid  where 1=1 ${ew.sqlSegment}")
    List<WordTemplate> getWordTemplate(@Param("ew") EntityWrapper ew);

}
