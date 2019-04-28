package com.avst.trm.v1.web.service.baseservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_datainfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasynchroni_downserver;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datainfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasynchroni_downserverMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseReqClass;
import com.avst.trm.v1.report.toupserver.common.reqparam.GotoSynchronizedataParam;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizeDataTypeParam;
import com.avst.trm.v1.web.req.basereq.CloseddownServerParam;
import com.avst.trm.v1.web.req.basereq.GetdownServersParam;
import com.avst.trm.v1.web.req.basereq.StartdownServerParam;
import com.avst.trm.v1.web.vo.basevo.GetdownServersVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("downServerService")
public class DownServerService extends BaseService {
    private Gson gson=new Gson();
    @Autowired
    private Base_datasynchroni_downserverMapper base_datasynchroni_downserverMapper;
    @Autowired
    private Base_datainfoMapper base_datainfoMapper;


    public void  getdownServers(RResult result, GetdownServersParam param){
        GetdownServersVO getdownServersVO=new GetdownServersVO();

        EntityWrapper ew=new EntityWrapper();
        if (null!=param.getUpserverip()){
            ew.like(true,"upserverip",param.getUpserverip().trim());
        }


        int count = base_datasynchroni_downserverMapper.selectCount(ew);
        param.setRecordCount(count);

        ew.orderBy("lastuploadtime",false);
        Page<Base_datasynchroni_downserver> page=new Page<Base_datasynchroni_downserver>(param.getCurrPage(),param.getPageSize());
        List<Base_datasynchroni_downserver> list=base_datasynchroni_downserverMapper.selectPage(page,ew);
        getdownServersVO.setPageparam(param);

        if (null!=list&&list.size()>0){
            getdownServersVO.setPagelist(list);
        }

        List<Base_datainfo> datainfos =  base_datainfoMapper.selectList(null);
        if (null!=datainfos&&datainfos.size()>0){
            getdownServersVO.setDatainfos(datainfos);
        }

        result.setData(getdownServersVO);
        changeResultToSuccess(result);
        return;
    }

    public void  startdownServer(RResult result, StartdownServerParam param){
        String downserverssid=param.getDownserverssid();//同步服务器的ssid

        List<String> datainfossids=param.getDatainfossids();//表单ssid集合

        if (StringUtils.isBlank(downserverssid)||null==datainfossids||datainfossids.size()<1){
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


        Base_datasynchroni_downserver datasynchroni_downserver=new Base_datasynchroni_downserver();
        datasynchroni_downserver.setSsid(downserverssid);
        datasynchroni_downserver=base_datasynchroni_downserverMapper.selectOne(datasynchroni_downserver);
        if (null!=datasynchroni_downserver){
            //开始
            for (String datainfossid : datainfossids) {
                Base_datainfo base_datainfo=new Base_datainfo();
                base_datainfo.setSsid(datainfossid);
                base_datainfo=base_datainfoMapper.selectOne(base_datainfo);


                SynchronizeDataTypeParam synchronizeDataTypeParam=new SynchronizeDataTypeParam();
                synchronizeDataTypeParam.setSdIP(datasynchroni_downserver.getUpserverip());
                synchronizeDataTypeParam.setDatassid(datainfossid);
             //   synchronizeDataTypeParam.setType(base_datainfo.getTypessid());
                ToUpServerBaseReqClass.startSynchronizedata(synchronizeDataTypeParam);


                GotoSynchronizedataParam gotoSynchronizedataParam=new GotoSynchronizedataParam();
                gotoSynchronizedataParam.setSdIP(datasynchroni_downserver.getUpserverip());
                ToUpServerBaseReqClass.synchronizedata(gotoSynchronizedataParam);

            }
            
            


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
