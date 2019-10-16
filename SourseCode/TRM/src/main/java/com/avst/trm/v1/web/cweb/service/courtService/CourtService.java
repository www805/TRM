package com.avst.trm.v1.web.cweb.service.courtService;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_type;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_typeMapper;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.UserInfo;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.cweb.req.courtreq.AddCaseToUserParam;
import com.avst.trm.v1.web.cweb.vo.courtvo.AddCaseToUserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service("courtService")
public class CourtService extends BaseService {
    @Autowired
    private Base_typeMapper base_typeMapper;

    public void addCaseToUser(RResult result, ReqParam<AddCaseToUserParam> paramReqParam, HttpSession session){
        AddCaseToUserVO vo=new AddCaseToUserVO();

        AddCaseToUserParam param=paramReqParam.getParam();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

        String mtmodelssid=param.getMtmodelssid();//会议模板ssid
        List<Map<String,UserInfo>> arraignmentexpand=param.getArraignmentexpand();

        //获取会议模板
        Integer multifunctionbool=param.getMultifunctionbool();
        if (multifunctionbool==1||multifunctionbool==2){
            mtmodelssid= PropertiesListenerConfig.getProperty("mcmodel_conversation");
        }else {
            if (StringUtils.isBlank(mtmodelssid)){
                //会议模板为空，直接取默认的
                Base_type base_type=new Base_type();
                base_type.setType(CommonCache.getCurrentServerType());
                base_type=base_typeMapper.selectOne(base_type);
                if (null!=base_type){
                    mtmodelssid=base_type.getMtmodelssid();
                }
            }
        }
        LogUtil.intoLog(this.getClass(),"添加庭审使用的会议模板ssid_"+mtmodelssid);


        changeResultToSuccess(result);
        return;
    }
}
