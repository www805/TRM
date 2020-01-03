package com.avst.trm.v1.web.cweb.service.policeservice;

import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.Phreport;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.poiwork.HtmlToWord;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.cweb.req.policereq.DelPhreportParam;
import com.avst.trm.v1.web.cweb.req.policereq.GetPhreportsParam;
import com.avst.trm.v1.web.cweb.req.policereq.UploadPhreportParam;
import com.avst.trm.v1.web.cweb.vo.policevo.GetPhreportsVO;
import com.avst.trm.v1.web.cweb.vo.policevo.UploadPhreportVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("phreportService")
public class PhreportService extends BaseService {
    @Autowired
    private Base_filesaveMapper base_filesaveMapper;

    private Gson gson=new Gson();

    public void getPhreports(RResult result, GetPhreportsParam param){
        GetPhreportsVO vo=new GetPhreportsVO();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }
         String recordssid=param.getRecordssid();//关联的笔录ssid
         String filename=param.getPhreportname();
         String starttime_start=param.getStarttime_start();//报告时间开始
         String starttime_end=param.getStarttime_end();//报告结束时间

        EntityWrapper ew=new EntityWrapper();
        if (StringUtils.isNotBlank(filename)){
            ew.like("uploadfilename",filename);
        }
        if (StringUtils.isNotBlank(recordssid)){
            ew.like("datassid",recordssid);
        }
        if(StringUtils.isNotEmpty(starttime_start) && StringUtils.isNotEmpty(starttime_end)){
            ew.between("createtime", starttime_start, starttime_end);
        }
        ew.eq("filebool",1);//文件状态正常的插件
        ew.eq("filetype","phreport");//文件类型为情绪 报告
        ew.orderBy("createtime",false);
        int count = base_filesaveMapper.countgetfilesavePage(ew);
        param.setRecordCount(count);

        Page<Base_filesave> page=new Page<Base_filesave>(param.getCurrPage(),param.getPageSize());
        List<Base_filesave> list=base_filesaveMapper.getfilesavePage(page,ew);
        vo.setPageparam(param);

        List<Phreport> pagelist=new ArrayList<>();
        if (null!=list&&list.size()>0){
            pagelist=gson.fromJson(gson.toJson(list), new TypeToken<List<Phreport>>(){}.getType());

            String uploadbasepath= PropertiesListenerConfig.getProperty("upload.basepath");
            for (Phreport phreport : pagelist) {
                String downurl=phreport.getRecorddownurl();
                String realurl=phreport.getRecordrealurl();
                String downurl_html=null;
                if (StringUtils.isNotBlank(downurl)){
                    phreport.setRecorddownurl(uploadbasepath+downurl);
                }

                //接下来判断HTML是否生成:不生成：转换失败
               /* if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                    if(realurl.endsWith(".doc")){
                        String replace = realurl.replace(".doc", ".html");
                        File f = new File(replace);
                        if (f.exists()) {
                            LogUtil.intoLog(this.getClass(),"word模板doc转html文件存在:"+replace);
                            downurl_html=downurl.replace(".doc", ".html");
                        }
                    }else if(realurl.endsWith(".docx")){
                        String replace = realurl.replace(".docx", ".html");
                        File f = new File(replace);
                        if (f.exists()) {
                            LogUtil.intoLog(this.getClass(),"word模板docx转html文件存在:"+replace);
                            downurl_html=downurl.replace(".docx", ".html");
                        }
                    }
                }*/
                if (null!=downurl_html){
                    phreport.setDownurl_html(uploadbasepath+downurl_html);
                }

            }
        }

        vo.setPagelist(pagelist);
        result.setData(vo);
        changeResultToSuccess(result);
        return;


    }

    public void uploadPhreport(RResult result, UploadPhreportParam param){
        UploadPhreportVO vo =new UploadPhreportVO();
        if (null==param){
            result.setMessage("参数为空");
            return;
        }
        String phreportname=param.getPhreportname();//报告名称:唯一
        String recordssid=param.getRecordssid();//关联的笔录ssid
        List<String> phreportdataList=param.getPhreportdataList();
         LogUtil.intoLog(1,this.getClass(),"情绪报表参数__uploadPhreport__recordssid__"+recordssid+"__phreportname__"+phreportname);
        if (StringUtils.isEmpty(recordssid)||StringUtils.isEmpty(phreportname)){
            result.setMessage("参数为空");
            return;
        }



        //开始检测是否已经存在相同文件名
        EntityWrapper police_cases_param=new EntityWrapper();
        police_cases_param.eq("uploadfilename",phreportname.trim());
        police_cases_param.eq("filetype","phreport");//文件类型为情绪报告
        police_cases_param.ne("filebool",-1);
        List<Base_filesave> police_cases_=base_filesaveMapper.selectList(police_cases_param);
        if (null!=police_cases_&&police_cases_.size()>0){
            result.setMessage("情绪报告生成的名称已存在");
            return;
        }

        phreportname=phreportname+".doc";
        String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");
        String savePath=PropertiesListenerConfig.getProperty("file.phreport");
        String qg=PropertiesListenerConfig.getProperty("file.qg");

        String realurl = OpenUtil.createpath_fileByBasepath(savePath, phreportname);
        LogUtil.intoLog(1,this.getClass(),"情绪报表的真实地址__："+realurl);
        //开始保存文件
        String content="";
        if (null!=phreportdataList&&phreportdataList.size()>0){
            for (String  asrhtml : phreportdataList) {
                if (StringUtils.isNotEmpty(asrhtml)){
                    content+=asrhtml;
                }
            }
        }
        try {
            boolean  htmltowordbool = HtmlToWord.HtmlToWord(realurl,content);
            if (htmltowordbool){
                String downurl =OpenUtil.strMinusBasePath(qg, realurl) ;
                LogUtil.intoLog(1,this.getClass(),"情绪报表的下载地址__："+downurl);

                if (StringUtils.isNotBlank(realurl)&&StringUtils.isNotBlank(downurl)){
                    //添加数据库
                    Base_filesave base_filesave=new Base_filesave();
                    base_filesave.setDatassid(recordssid);
                    base_filesave.setUploadfilename(param.getPhreportname());
                    base_filesave.setRealfilename(param.getPhreportname());
                    base_filesave.setRecordrealurl(realurl);
                    base_filesave.setRecorddownurl(downurl);
                    base_filesave.setSsid(OpenUtil.getUUID_32());
                    base_filesave.setFilebool(1);
                    base_filesave.setFiletype("phreport");//固定类型
                    base_filesave.setCreatetime(DateUtil.getDateAndMinute());
                    int  filesaveinsert_bool= base_filesaveMapper.insert(base_filesave);
                    LogUtil.intoLog(1,this.getClass(),"uploadPhreport__filesaveinsert_bool__"+filesaveinsert_bool);
                    if (filesaveinsert_bool>0){
                        vo.setDownurl(uploadbasepath+downurl);
                        result.setData(vo);
                        changeResultToSuccess(result);
                    }
                }else {
                    LogUtil.intoLog(1,this.getClass(),"情绪报表地址有误__realurl__"+realurl+"__downurl__"+downurl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void delPhreport(RResult result, DelPhreportParam param){
        if (null==param){
            result.setMessage("参数为空");
            return;
        }

        String ssid=param.getSsid();
        Integer filebool=param.getFilebool();
        LogUtil.intoLog(this.getClass(),"情绪报告删除的ssid__delPhreport__"+ssid);
        if (StringUtils.isBlank(ssid)||null==filebool){
            result.setMessage("参数为空");
            return;
        }

        EntityWrapper filesaves_ew=new EntityWrapper();
        filesaves_ew.eq("ssid",ssid);
        List<Base_filesave> filesaves_=base_filesaveMapper.selectList(filesaves_ew);
        if (null!=filesaves_&&filesaves_.size()==1){
            Base_filesave base_filesave_=filesaves_.get(0);
            base_filesave_.setFilebool(filebool);
            int base_filesaveMapper_update_bool=base_filesaveMapper.update(base_filesave_,filesaves_ew);
            LogUtil.intoLog(this.getClass(),"base_filesaveMapper_update_bool__"+base_filesaveMapper_update_bool);
            if (base_filesaveMapper_update_bool>0){
                result.setData(base_filesaveMapper_update_bool);
                changeResultToSuccess(result);
            }
        }else {
            LogUtil.intoLog(this.getClass(),"情绪报告删除查询是否存在该文件：该记录不存在或者查询出多条");
        }
        return;
    }


    public static void main(String[] args) {
       String content = "<div><p style='color: #999'>【赵六】 2019-11-02 13:31:12</p><span style='color: #fff; background: #0181cc;'>但是。</span></div>" +
                " <div><p style='color: #999'>2019-11-02 13:31:12 【赵六】 </p> <span  style='color: #fff; background: #ef8201;float:right'>八嘎。</span>  </div>";
         content = "<html><meta charset='UTF-8'><head>情绪报告</head><body>"+content+"</body></html>";
        byte b[] = content.getBytes();
        try {
            InputStream is = new ByteArrayInputStream(b);
            OutputStream os = new FileOutputStream("f:"+File.separator+"1.doc");
            POIFSFileSystem fs = new POIFSFileSystem();
            fs.createDocument(is, "WordDocument");
            fs.writeFilesystem(os);
            os.close();
            is.close();
            System.out.println("生成完毕！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
