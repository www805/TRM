package com.avst.trm.v1.report.toupserver.police.v1.ToUpServerDeal;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_datasheet_downserver;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datainfoMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasheet_downserverMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_datasynchroni_downserverMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RRParam;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.conf.SynDataTableConstant;
import com.avst.trm.v1.report.cache.ReportCahce;
import com.avst.trm.v1.report.conf.ReportConf;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseReqInterface;
import com.avst.trm.v1.report.toupserver.common.cache.SynchronizedataCache;
import com.avst.trm.v1.report.toupserver.common.reqparam.SynchronizedataParam;
import com.avst.trm.v1.report.toupserver.common.conf.AddDataToSynchronizeDataConf;
import com.avst.trm.v1.report.toupserver.common.reqparam.StartSynchronizedata_2_Param;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ToUpServerBaseDeal_police  implements  ToUpServerBaseReqInterface{

    @Autowired
    private Base_datainfoMapper base_datainfoMapper;
    @Autowired
    private Base_filesaveMapper base_filesaveMapper;
    @Autowired
    private Base_datasynchroni_downserverMapper base_datasynchroni_downserverMapper;

    @Autowired
    private Base_datasheet_downserverMapper base_datasheet_downserverMapper;

    /**
     * 同步数据给上级服务器的接口
     * @param url
     * @param startparam
     * @return
     */
    @Override
    public RRParam synchronizedata(String url,StartSynchronizedata_2_Param startparam ,String downserverssid) {
        RRParam rrParam=new RRParam();

        String datatablename=startparam.getDataname();
        int type=startparam.getType();
        boolean overwork=startparam.isOverwork();
        String datassid=startparam.getDatassid();//文件也有ssid
        String filename=startparam.getFilename();

        //同步的参数对象是
        SynchronizedataParam synchronizedataParam=new SynchronizedataParam();
        synchronizedataParam.setDataname(datatablename);
        synchronizedataParam.setType(type);
        synchronizedataParam.setSsid(datassid);
        synchronizedataParam=AddDataToSynchronizeDataConf.getSynchronizedataParam(synchronizedataParam,datassid);

        String filepath="";
        if(SynDataTableConstant.tablebase_filesave.equals(datatablename)){//如果上传的是文件数据就需要获取文件地址
            Base_filesave base_filesave=(Base_filesave)synchronizedataParam.getT();
            filepath=base_filesave.getRealfilename();
        }

        String rr="";
        if(type==2){//上传文件
            Map<String,String> map=new HashMap<String,String>();
            map.put("data",JacksonUtil.objebtToString(synchronizedataParam));
            map.put("token",ReportCahce.getToupserverTBToken());
            map.put("sqNum", CommonCache.getServerconfig().getAuthorizesortnum()+"");
            rr=HttpRequest.uploadFile(url,filepath,map);
        }else{//数据库上传
            String reqparam= ReportConf.getParam(JacksonUtil.objebtToString(synchronizedataParam),false);
            rr=HttpRequest.readContentFromGet(url,reqparam);
        }

        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            LogUtil.intoLog(this.getClass(),result.getActioncode()+":result.getActioncode()");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){

                //请求成功的处理
                Base_datasheet_downserver base_datasheet_downserver=new Base_datasheet_downserver();
                base_datasheet_downserver.setDatassid(datassid);
                base_datasheet_downserver.setDataname(datatablename);
                if(type==2){//同步文件
                    base_datasheet_downserver.setFiletype(1);
                    base_datasheet_downserver.setFilename("");
                }else{
                    base_datasheet_downserver.setFiletype(-1);
                }
                base_datasheet_downserver.setSsid(OpenUtil.getUUID_32());
                base_datasheet_downserver.setDownserverssid(downserverssid);
                int insertdatasheet=base_datasheet_downserverMapper.insert(base_datasheet_downserver);
                if(insertdatasheet>=0){

                    //修改缓存
                    startparam.setOverwork(true);
                    SynchronizedataCache.setSynchronizedataBySortnum(ReportCahce.getToupserverTBToken(),startparam);

                    rrParam.setCode(Code.SUCCESS.hashCode());
                    rrParam.setMessage("请求成功");
                }else{
                    rrParam.setMessage("新增同步数据表失败");

                    //不需要做什么操作，留到全部同步完成，有检测是否可以正常结束同步
                }
            }else{
                rrParam.setMessage(result.getMessage());
                LogUtil.intoLog(this.getClass(),result.getMessage()+"-----上级服务器返回的错误信息");

            }
        }
        return rrParam;
    }





}
