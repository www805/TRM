package com.avst.trm.v1.web.sweb.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.DownserverAndDatainfo;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datainfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasheet_downserverMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasynchroni_downserverMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseReqClass;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizeDataTypeParam;
import com.avst.trm.v1.web.sweb.req.basereq.CloseddownServerParam;
import com.avst.trm.v1.web.sweb.req.basereq.GetdownServersParam;
import com.avst.trm.v1.web.sweb.req.basereq.StartdownServerParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetdownServersVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("downServerService")
public class DownServerService extends BaseService {
    private Gson gson=new Gson();
    @Autowired
    private Base_datasynchroni_downserverMapper base_datasynchroni_downserverMapper;
    @Autowired
    private Base_datainfoMapper base_datainfoMapper;
    @Autowired
    private Base_datasheet_downserverMapper datasheet_downserverMapper;


    public void  getdownServers(RResult result, GetdownServersParam param){
        GetdownServersVO getdownServersVO=new GetdownServersVO();

        EntityWrapper ew=new EntityWrapper();
        if (null!=param.getUpserverip()){
            ew.like(true,"dsd.upserverip",param.getUpserverip().trim());
        }

        int count = datasheet_downserverMapper.countgetdownServers(ew);
        param.setRecordCount(count);

        ew.orderBy("dsd.lastuploadtime",false);
        Page<DownserverAndDatainfo> page=new Page<DownserverAndDatainfo>(param.getCurrPage(),param.getPageSize());
        List<DownserverAndDatainfo> list=datasheet_downserverMapper.getdownServers(page,ew);
        getdownServersVO.setPageparam(param);

        if (null!=list&&list.size()>0){
            getdownServersVO.setPagelist(list);
        }


        String lastIp=base_datasynchroni_downserverMapper.getLastIpByTime();
        if (StringUtils.isNotBlank(lastIp)){
            getdownServersVO.setLastIP(lastIp);
        }

        result.setData(getdownServersVO);
        changeResultToSuccess(result);
        return;
    }

    public void  startdownServer(RResult result, StartdownServerParam param){
        String upserverip=param.getUpserverip();//同步服务器的ssid
        String datainfossid=param.getDatainfossid();//表单ssid集合
        Integer type=param.getType();//0同步全部数据，1同步一个表的数据，2同步一条数据

        if (StringUtils.isBlank(upserverip)||null==type){
            result.setMessage("参数有误");
            return;
        }

        //初始化
        boolean init=ToUpServerBaseReqClass.initsynchronizeddata();
        if (!init)
        {
            result.setMessage("初始化错误");
            return;
        }


        SynchronizeDataTypeParam synchronizeDataTypeParam=new SynchronizeDataTypeParam();
        synchronizeDataTypeParam.setSdIP(upserverip);
        synchronizeDataTypeParam.setType(type);
        if (type.intValue()==0){
            //同步全部表单
            ToUpServerBaseReqClass.startSynchronizedata(synchronizeDataTypeParam);
        }else if (type.intValue()==1){
                if (StringUtils.isEmpty(datainfossid)){
                    result.setMessage("参数有误");
                    return;
                }
                //同步单个表单
                Base_datainfo base_datainfo=new Base_datainfo();
                base_datainfo.setSsid(datainfossid);
                base_datainfo=base_datainfoMapper.selectOne(base_datainfo);
                if (null!=base_datainfo){
                    synchronizeDataTypeParam.setDatatablename(base_datainfo.getDataname());
                    ToUpServerBaseReqClass.startSynchronizedata(synchronizeDataTypeParam);
                }
        }else if (type.intValue()==2){
            //单个数据
        }
        changeResultToSuccess(result);
        return;
    }

    public void  closeddownServer(RResult result, CloseddownServerParam param){
        try {
            //强制关闭
            ToUpServerBaseReqClass.overSynchronizedata();
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeResultToSuccess(result);
        return;
    }


}
