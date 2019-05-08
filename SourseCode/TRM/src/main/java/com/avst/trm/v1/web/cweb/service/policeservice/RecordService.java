package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetCaseByIdParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetRecordsParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetUserByCardParam;
import com.avst.trm.v1.web.cweb.vo.policevo.GetCaseByIdVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRecordsVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetUserByCardVO;
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

    public void getUserByCard(RResult result, ReqParam<GetUserByCardParam> param){
        GetUserByCardVO getUserByCardVO=new GetUserByCardVO();

        GetUserByCardParam getUserByCardParam=param.getParam();
        if (null==getUserByCardParam){
            result.setMessage("参数为空");
            return;
        }

        return;
    }

    public void getCaseById(RResult result, ReqParam<GetCaseByIdParam> param){
        GetCaseByIdVO getCaseByIdVO=new GetCaseByIdVO();

        GetCaseByIdParam getCaseByIdParam=param.getParam();
        if (null==getCaseByIdParam){
            result.setMessage("参数为空");
            return;
        }


        return;
    }



}
