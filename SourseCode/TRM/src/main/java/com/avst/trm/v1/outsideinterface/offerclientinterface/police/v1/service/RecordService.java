package com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.service;

import com.avst.trm.v1.common.datasourse.police.entity.Police_record;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.ForClientBaseService;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.req.GetRecordsParam;
import com.avst.trm.v1.outsideinterface.offerclientinterface.police.v1.vo.GetRecordsVO;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("recordService")
public class RecordService extends ForClientBaseService {
    private Gson gson = new Gson();

    public void getRecords(RResult result, ReqParam<GetRecordsParam> param){
        GetRecordsVO getRecordsVO=new GetRecordsVO();

        //请求参数转换
        GetRecordsParam getRecordsParam = param.getParam();
        if (null==getRecordsParam){
            result.setMessage("参数为空");
            return;
        }


        return;
    }

    public void addRecord(RResult result, ReqParam param){
        return;
    }

    public void getRecordById(RResult result, ReqParam param){
        return;
    }

    public void uploadRecord(RResult result, ReqParam param){
        return;
    }

    public void recordIndex(RResult result, ReqParam param){
        return;
    }

    public void getRecordtypes(RResult result, ReqParam param){
        return;
    }

    public void getRecordtypeById(RResult result, ReqParam param){
        return;
    }

    public void addRecordtype(RResult result, ReqParam param){
        return;
    }

    public void updateRecordtype(RResult result, ReqParam param){
        return;
    }

    public void addRecordTemplate(RResult result, ReqParam param){
        return;
    }

    public void addCaseToArraignment(RResult result, ReqParam param){
        return;
    }





}
