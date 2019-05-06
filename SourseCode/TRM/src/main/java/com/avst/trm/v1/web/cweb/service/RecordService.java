package com.avst.trm.v1.web.cweb.service;

import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.GetRecordsParam;
import com.avst.trm.v1.web.cweb.vo.GetRecordsVO;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service("recordService")
public class RecordService extends BaseService {
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
