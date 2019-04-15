package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service;

import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("recordService")
public class RecordService extends ForClientBaseService {
    private Gson gson = new Gson();

    public void getRecords(RResult result, ReqParam param){
        Police_record record =new Police_record();
        String parameter= (String) param.getParam();
        if (StringUtils.isNotBlank(parameter)){
            record =gson.fromJson(parameter, Police_record.class);
        }
    }
}
