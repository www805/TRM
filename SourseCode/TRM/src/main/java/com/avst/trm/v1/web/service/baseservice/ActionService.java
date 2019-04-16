package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_action;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_actionMapper;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 关于动作action的处理
 */
public class ActionService {

    @Autowired
    private Base_actionMapper base_actionMapper;

    public ActionAndinterfaceAndPage getAction(String interfaceurl,String pageid ){

        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("interfaceurl",interfaceurl);
        entityWrapper.eq("pageid",pageid);

        ActionAndinterfaceAndPage actionAndinterfaceAndPage=base_actionMapper.getActionAndinterfaceAndPage(entityWrapper);
        if(null!=actionAndinterfaceAndPage){


            return actionAndinterfaceAndPage;
        }
        return null;
    }
}
