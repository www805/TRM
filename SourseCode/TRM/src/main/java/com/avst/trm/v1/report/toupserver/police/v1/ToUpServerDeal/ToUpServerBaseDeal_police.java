package com.avst.trm.v1.report.toupserver.police.v1.ToUpServerDeal;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.util.HttpRequest;
import com.avst.trm.v1.common.util.JacksonUtil;
import com.avst.trm.v1.common.util.baseaction.Code;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.report.conf.ReportCahce;
import com.avst.trm.v1.report.conf.ReportConf;
import com.avst.trm.v1.report.reqparam.BaseReqParam;
import com.avst.trm.v1.report.toupserver.ToUpServerBaseReqInterface;
import org.apache.commons.lang.StringUtils;

public class ToUpServerBaseDeal_police implements ToUpServerBaseReqInterface {

    @Override
    public void initsynchronizeddata(String url) {

        String param= ReportConf.getParam(null,true);
        String rr=HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            System.out.println(result.getActioncode()+":result.getActioncode()");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){



                //请求成功的处理


            }
        }

    }

    @Override
    public void startSynchronizedata(String url) {

        String data="";//Jackson转对象为String
        String param= ReportConf.getParam(data,true);
        String rr=HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            System.out.println(result.getActioncode()+":result.getActioncode()");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){



                //请求成功的处理


            }
        }
    }

    @Override
    public void synchronizedata(String url) {

        String data="";//Jackson转对象为String
        String param= ReportConf.getParam(data,true);
        String rr=HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            System.out.println(result.getActioncode()+":result.getActioncode()");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){



                //请求成功的处理


            }
        }
    }

    @Override
    public void overSynchronizedata(String url) {

        String data="";//Jackson转对象为String
        String param= ReportConf.getParam(data,true);
        String rr=HttpRequest.readContentFromGet(url,param);
        if(StringUtils.isNotEmpty(rr)){

            RResult result=(RResult) JacksonUtil.stringToObjebt_1(rr, RResult.class);
            System.out.println(result.getActioncode()+":result.getActioncode()");
            if(null!=result && result.getActioncode().equals(Code.SUCCESS.toString())){



                //请求成功的处理


            }
        }
    }

}
