package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_national;
import com.avst.trm.v1.common.datasourse.base.entity.Base_nationality;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_nationalMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_nationalityMapper;
import com.avst.trm.v1.common.datasourse.police.entity.Police_recordtype;
import com.avst.trm.v1.common.datasourse.police.mapper.Police_recordtypeMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.policereq.*;
import com.avst.trm.v1.web.cweb.vo.policevo.GetCaseByIdVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRecordsVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetRecordtypesVO;
import com.avst.trm.v1.web.cweb.vo.policevo.GetUserByCardVO;
import com.avst.trm.v1.web.cweb.vo.policevo.param.GetRecordtypesVOParam;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("recordService")
public class RecordService extends BaseService {
    private Gson gson = new Gson();

    @Autowired
    private Police_recordtypeMapper police_recordtypeMapper;

    @Autowired
    private Base_nationalityMapper base_nationalityMapper;

    @Autowired
    private Base_nationalMapper base_nationalMapper;



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

    public void getRecordtypes(RResult result, ReqParam<GetRecordtypesParam> param){
        GetRecordtypesVO getRecordtypesVO=new GetRecordtypesVO();

        //请求参数转换
        GetRecordtypesParam getRecordtypesParam = param.getParam();
        if (null==getRecordtypesParam){
            result.setMessage("参数为空");
            return;
        }

        Integer pid=getRecordtypesParam.getPid();
        EntityWrapper ew=new EntityWrapper();
        if (null!=pid){
            ew.eq("pid",pid);
        }
        ew.orderBy("ordernum",true);
        ew.orderBy("createtime",false);
        List<Police_recordtype> list=police_recordtypeMapper.selectList(ew);
        List<GetRecordtypesVOParam> getRecordtypesVOParamList =new ArrayList<GetRecordtypesVOParam>();
        if (null!=list&&list.size()>0){
            List<GetRecordtypesVOParam> recordtypes=gson.fromJson(gson.toJson(list), new TypeToken<List<GetRecordtypesVOParam>>(){}.getType());
            for (int i = 0; i < recordtypes.size(); i++) {
                for (int j = 0; j < recordtypes.size(); j++) {
                    if(recordtypes.get(i).getId()==recordtypes.get(j).getPid()){
                        GetRecordtypesVOParam m= recordtypes.get(i);
                        m.getPolice_recordtypes().add(recordtypes.get(j));
                    }
                }
            }
            for (int i = 0; i < recordtypes.size(); i++) {
                if(recordtypes.get(i).getPid()==0){
                    getRecordtypesVOParamList.add(recordtypes.get(i));
                }
                if (null!=pid){
                    getRecordtypesVOParamList.add(recordtypes.get(i));
                }
            }
            getRecordtypesVO.setGetRecordtypesVOParamList(getRecordtypesVOParamList);
        }
        result.setData(getRecordtypesVO);
        changeResultToSuccess(result);
        return;
    }

    public void getPidRecordtypes(RResult result, ReqParam param){
        EntityWrapper pidparam=new EntityWrapper();
        pidparam.eq("pid",0);
        List<Police_recordtype> police_recordtypesByPid=police_recordtypeMapper.selectList(pidparam);
        if (null!=police_recordtypesByPid&&police_recordtypesByPid.size()>0){
           result.setData(police_recordtypesByPid);
           changeResultToSuccess(result);
        }
        return;
    }

    public void getRecordtypeById(RResult result, ReqParam<GetRecordtypeByIdParam> param){
        //请求参数转换
        GetRecordtypeByIdParam getRecordtypeByIdParam = param.getParam();
        if (null==getRecordtypeByIdParam){
            result.setMessage("参数为空");
            return;
        }

        Police_recordtype police_recordtype=new Police_recordtype();
        police_recordtype.setId(getRecordtypeByIdParam.getId());
        police_recordtype =  police_recordtypeMapper.selectOne(police_recordtype);
        if (null==police_recordtype){
            System.out.println("未找到该笔录类型--");
            result.setMessage("系统错误");
        }
        result.setData(police_recordtype);
        changeResultToSuccess(result);
        return;
    }

    public void addRecordtype(RResult result, ReqParam<Police_recordtype> param){
        Police_recordtype police_recordtype=param.getParam();
        if (null==police_recordtype){
            result.setMessage("参数为空");
            return;
        }
        police_recordtype.setSsid(OpenUtil.getUUID_32());
        police_recordtype.setCreatetime(new Date());
        int insert_bool=police_recordtypeMapper.insert(police_recordtype);
        System.out.println("insert_bool__"+insert_bool);
        if (insert_bool>0){
            result.setData(insert_bool);
            changeResultToSuccess(result);
        }
        return;
    }

    public void updateRecordtype(RResult result, ReqParam<Police_recordtype> param){
        Police_recordtype police_recordtype=param.getParam();
        if (null==police_recordtype){
            result.setMessage("参数为空");
            return;
        }
        String ssid=police_recordtype.getSsid();

        int update_bool=police_recordtypeMapper.updateById(police_recordtype);
        System.out.println("update_bool__"+update_bool);
        if (update_bool>0){
            result.setData(update_bool);
            changeResultToSuccess(result);
        }
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

    public void getNationalitys(RResult result, ReqParam param){
        List<Base_nationality> list=base_nationalityMapper.selectList(null);
        result.setData(list);
        changeResultToSuccess(result);
        return;
    }

    public void getNationals(RResult result, ReqParam param){
        List<Base_national> list=base_nationalMapper.selectList(null);
        result.setData(list);
        changeResultToSuccess(result);
        return;
    }



}
