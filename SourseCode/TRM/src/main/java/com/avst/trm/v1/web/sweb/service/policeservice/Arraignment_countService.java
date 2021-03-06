package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_admininfo;
import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.web.sweb.req.basereq.AdmininfoOrWorkunitParam;
import com.avst.trm.v1.web.sweb.req.basereq.Arraignment_countParam;
import com.avst.trm.v1.web.sweb.req.policereq.GetArraignmentCountParam;
import com.avst.trm.v1.web.sweb.vo.basevo.KeywordListVO;
import com.avst.trm.v1.web.sweb.vo.policevo.GetArraignmentCountVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class Arraignment_countService extends BaseService {

    @Autowired
    private Base_admininfoMapper arraignmentCountMapper;



    /**
     * 通过id查询关键字
     * @param result
     * @param id
     */
    public void getKeywordById(RResult<Base_keyword> result, int id){
//
//        Base_keyword keyword = keywordMapper.selectById(id);
//
//        if(null==result){
//            result=new RResult<Base_keyword>();
//        }
//
//        if(keyword != null){
//            if (keyword.getShieldbool() == -1) {
//                keyword.setShieldbool(0);
//            }
//
//            result.setData(keyword);
//            this.changeResultToSuccess(result);
//        }
    }

    /**
     * 分页查询
     * @param result
     * @param param
     */
    public void findKeywordlist(RResult<KeywordListVO> result, Arraignment_countParam param){
//
//        if(null==result){
//            result=new RResult<KeywordListVO>();
//        }
//        List<Base_keyword> list=new ArrayList<Base_keyword>();
//        try {
////分页的条件，基本上都有
//            KeywordListVO getlist3VO=new KeywordListVO();
//            EntityWrapper ew=new EntityWrapper();
////            ew.setEntity(new Admininfo());
////            ew.between("id",2,5);
//            if(null!=cmparam){
//                if(StringUtils.isNotEmpty(cmparam.getText())){
//                    ew.like("text",cmparam.getText());
//                }
//            }
//
//            int count=keywordMapper.selectCount(ew);
//            cmparam.setRecordCount(count);
//            getlist3VO.setPageparam(cmparam);
//
////current 第多少页，size 每页多少条
//            Page<Base_role> page=new Page<Base_role>(cmparam.getCurrPage(),cmparam.getPageSize());
//            page.setTotal(count);
//
//
////            page.setRecords(list);
//            list=keywordMapper.selectPage(page,ew );
//
//            LogUtil.intoLog(this.getClass(),page.getSize()+"-----"+page.getCurrent()+"-----"+
//                    page.getTotal()+"-----"+page.getPages());
//
//            if(null!=list&&list.size() > 0){
//                getlist3VO.setPagelist(list);
//            }
//
//            result.setData(getlist3VO);
//            this.changeResultToSuccess(result);
//        }catch (Exception e){
//            e.fillInStackTrace();
//        }finally {
//            LogUtil.intoLog(this.getClass(),"请求结束");
//        }
    }

    /**
     * 多表查询
     * @param result
     */
    public void getArraignment_countList(RResult result, Arraignment_countParam param){

        //查出所有的系统用户，通过系ssid查到三个总量
        GetArraignmentCountVO getArraignmentCountVO = new GetArraignmentCountVO();

        List<GetArraignmentCountParam> list = new ArrayList<GetArraignmentCountParam>();

        EntityWrapper<Base_admininfo> ew = new EntityWrapper<>();

        if(StringUtils.isNotBlank(param.getUsername())){
            ew.like("username", param.getUsername());
        }

        Integer count = arraignmentCountMapper.selectCount(ew);
        param.setRecordCount(count);

        Page<AdmininfoOrWorkunitParam> page=new Page<AdmininfoOrWorkunitParam>(param.getCurrPage(),param.getPageSize());

        List<AdmininfoOrWorkunitParam> admininfoOrWorkunitParams = arraignmentCountMapper.selectPageWorkunit(page, ew);
        if (null != admininfoOrWorkunitParams && admininfoOrWorkunitParams.size() > 0) {

            for (AdmininfoOrWorkunitParam base_admininfo : admininfoOrWorkunitParams) {

                GetArraignmentCountParam getArraignmentCountParam = new GetArraignmentCountParam();
                getArraignmentCountParam.setUsername(base_admininfo.getUsername());

                EntityWrapper<Base_admininfo> wrapper = new EntityWrapper<>();

                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    wrapper.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
                }
                wrapper.eq("arr.adminssid",base_admininfo.getSsid());
                wrapper.or("arr.otheradminssid",base_admininfo.getSsid());
                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    wrapper.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
                }
                //查询询问人总量
                Integer arraignmentCount = arraignmentCountMapper.getArraignmentListCount(wrapper);
                getArraignmentCountParam.setArraignmentcount(arraignmentCount);

                //查询记录人总量
                EntityWrapper<Base_admininfo> wrapper2 = new EntityWrapper<>();
                wrapper2.ne("arr.adminssid",base_admininfo.getSsid());
                wrapper2.eq("arr.recordadminssid",base_admininfo.getSsid());
                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    wrapper2.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
//                    wrapper2.ge("DATE(arr.createtime)",param.getStarttime() ); //>=  时间类型不一样所以要转换date()
//                    wrapper2.le("DATE(arr.createtime)",param.getEndtime()); //<=
                }
                Integer recordadmincount = arraignmentCountMapper.getArraignmentListCount(wrapper2);
                getArraignmentCountParam.setRecordadmincount(recordadmincount);

                //两个加起来
                getArraignmentCountParam.setRecordcount(arraignmentCount + recordadmincount);
                getArraignmentCountParam.setWorkname(base_admininfo.getWorkname());

                list.add(getArraignmentCountParam);
            }
        }

        getArraignmentCountVO.setPageparam(param);
        getArraignmentCountVO.setPagelist(list);
        result.setData(getArraignmentCountVO);
        this.changeResultToSuccess(result);

//        List<Base_arraignmentCount> list = new ArrayList<Base_arraignmentCount>();
//        try {
//            ArraignmentCountVO getlist3VO=new ArraignmentCountVO();
//            EntityWrapper ew=new EntityWrapper();
//
//            Base_arraignmentCount arraignmentCount = null;
//            if(null!=param){
//
//                if(StringUtils.isNotEmpty(param.getUsername())){
//                    ew.ge("r.username",param.getUsername());
//                }
//                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
//                    ew.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
////                    ew.ge("DATE(arr.createtime)",param.getStarttime() ); //>=  时间类型不一样所以要转换date()
////                    ew.le("DATE(arr.createtime)",param.getEndtime()); //<=
//                }
//
//            }
//            ew.orderBy("a.id", false);
//            int count = arraignmentCountMapper.getArraignmentCountCount(ew);
//            param.setRecordCount(count);
//            getlist3VO.setPageparam(param);
////current 第多少页，size 每页多少条
//            Page<Base_arraignmentCount> page=new Page<Base_arraignmentCount>(param.getCurrPage(),param.getPageSize());
////            page.setRecords(list);
//            list = arraignmentCountMapper.getArraignmentCountList(page, ew);
//
//            if(null!=list&&list.size() > 0){
//
//                for (int i = 0; i < list.size(); i++) {
//
//                    EntityWrapper ew2=new EntityWrapper();
//
//                    ew2.eq("a.id", list.get(i).getId());
//
//                    if(null!=param){
//
//                        if(StringUtils.isNotEmpty(param.getUsername())){
//                            ew2.ge("r.username",param.getUsername());
//                        }
//                        if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
//                            ew2.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
////                            ew2.ge("DATE(arr.createtime)",param.getStarttime() ); //>=  时间类型不一样所以要转换date()
////                            ew2.le("DATE(arr.createtime)",param.getEndtime()); //<=
//                        }
//                    }
//
//                    arraignmentCount = arraignmentCountMapper.getArraignmentCount(ew2);
//
//                    list.get(i).setRecordCount(arraignmentCount.getRecordCount()); //笔录总量
//                    list.get(i).setRecordrealCount(arraignmentCount.getRecordrealCount());  //语音笔录总量
//                    list.get(i).setRecordtimeCount(arraignmentCount.getRecordtimeCount());  //笔录总时长
////                    list.get(i).setTimeCount(arraignmentCount.getTimeCount());
////                    list.get(i).setTranslatextCount(arraignmentCount.getTranslatextCount());
//                }
//
//            }
////            AdminAndWorkunit list1 = arraignmentCountMapper.getList(page, ew);
//
//            LogUtil.intoLog(this.getClass(),page.getSize()+"-----"+page.getCurrent()+"-----"+
//                    page.getTotal()+"-----"+page.getPages());
//            if(null!=list&&list.size() > 0){
//                getlist3VO.setPagelist(list);
//            }
//
//            result.setData(getlist3VO);
//            this.changeResultToSuccess(result);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            LogUtil.intoLog(this.getClass(),"请求结束");
//        }
    }


    public RResult exportExcel(RResult result, Arraignment_countParam param) {

        EntityWrapper ew=new EntityWrapper();

//        if(null!=param){
//
//            if(StringUtils.isNotEmpty(param.getTimes())){
//                ew.ge("r.time",param.getTimes());
//            }
//            if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
//                ew.between("r.createtime", param.getStarttime(), param.getEndtime());
//            }
//
//        }
//        ew.orderBy("a.id", false);
//        List<Base_arraignmentCount> list = arraignmentCountMapper.getArraignmentCountListNoPage(ew);
//
//        Base_arraignmentCount arraignmentCount = null;
//
//        if (null != list && list.size() > 0) {
//
//            for (int i = 0; i < list.size(); i++) {
//                EntityWrapper ew2 = new EntityWrapper();
//                ew2.eq("a.id", list.get(i).getId());
//                arraignmentCount = arraignmentCountMapper.getArraignmentCount(ew2);
//
//                list.get(i).setRecordCount(arraignmentCount.getRecordCount());
//                list.get(i).setRecordrealCount(arraignmentCount.getRecordrealCount());
//                list.get(i).setRecordtimeCount(arraignmentCount.getRecordtimeCount());
//                list.get(i).setTimeCount(arraignmentCount.getTimeCount());
//                list.get(i).setTranslatextCount(arraignmentCount.getTranslatextCount());
//            }
//        }
//

        GetArraignmentCountVO getArraignmentCountVO = new GetArraignmentCountVO();

        List<GetArraignmentCountParam> list = new ArrayList<GetArraignmentCountParam>();

        EntityWrapper<Base_admininfo> eww = new EntityWrapper<>();

        if(StringUtils.isNotBlank(param.getUsername())){
            eww.like("username", param.getUsername());
        }

        Integer count = arraignmentCountMapper.selectCount(eww);
        param.setRecordCount(count);

        Page<AdmininfoOrWorkunitParam> page=new Page<AdmininfoOrWorkunitParam>(param.getCurrPage(),param.getPageSize());

        List<AdmininfoOrWorkunitParam> admininfoOrWorkunitParams = arraignmentCountMapper.selectPageWorkunit(page, eww);
        if (null != admininfoOrWorkunitParams && admininfoOrWorkunitParams.size() > 0) {

            for (AdmininfoOrWorkunitParam base_admininfo : admininfoOrWorkunitParams) {

                GetArraignmentCountParam getArraignmentCountParam = new GetArraignmentCountParam();
                getArraignmentCountParam.setUsername(base_admininfo.getUsername());

                EntityWrapper<Base_admininfo> wrapper = new EntityWrapper<>();

                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    wrapper.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
                }
                wrapper.eq("arr.adminssid",base_admininfo.getSsid());
                wrapper.or("arr.otheradminssid",base_admininfo.getSsid());
                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    wrapper.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
                }
                //查询询问人总量
                Integer arraignmentCount = arraignmentCountMapper.getArraignmentListCount(wrapper);
                getArraignmentCountParam.setArraignmentcount(arraignmentCount);

                //查询记录人总量
                EntityWrapper<Base_admininfo> wrapper2 = new EntityWrapper<>();
                wrapper2.ne("arr.adminssid",base_admininfo.getSsid());
                wrapper2.eq("arr.recordadminssid",base_admininfo.getSsid());
                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    wrapper2.between("DATE(arr.createtime)", param.getStarttime(), param.getEndtime());//时间类型不一样所以要转换date()
//                    wrapper2.ge("DATE(arr.createtime)",param.getStarttime() ); //>=  时间类型不一样所以要转换date()
//                    wrapper2.le("DATE(arr.createtime)",param.getEndtime()); //<=
                }
                Integer recordadmincount = arraignmentCountMapper.getArraignmentListCount(wrapper2);
                getArraignmentCountParam.setRecordadmincount(recordadmincount);

                //两个加起来
                getArraignmentCountParam.setRecordcount(arraignmentCount + recordadmincount);
                getArraignmentCountParam.setWorkname(base_admininfo.getWorkname());

                list.add(getArraignmentCountParam);
            }
        }

        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("AVST");
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
//        sheet.setColumnWidth(6, 5000);
//        sheet.setColumnWidth(7, 5000);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short

        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 5);
        sheet.addMergedRegion(cra);
        HSSFRow row0 = sheet.createRow((int) 0);

        HSSFRow row = sheet.createRow((int) 1);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String format2 = df2.format(new Date());// new Date()为获取当前系统时间

        HSSFCell cell0 = row0.createCell((short) 0);
        if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
            if(StringUtils.isNotBlank(param.getUsername())) {
                cell0.setCellValue("查询：" + param.getUsername() + "  " + param.getStarttime() + "~" + param.getEndtime() + " 笔录统计");
            }else {
                cell0.setCellValue(param.getStarttime() + "~" + param.getEndtime() + " 笔录统计");
            }
        }else{
            cell0.setCellValue("笔录统计 " + format2);
        }
        cell0.setCellStyle(style);

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("人员名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("工作单位");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("询问总次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("记录总次数");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("笔录总数");
        cell.setCellStyle(style);

//        cell = row.createCell((short) 6);
//        cell.setCellValue("录音时长");
//        cell.setCellStyle(style);
//        cell = row.createCell((short) 7);
//        cell.setCellValue("笔录字数");
//        cell.setCellStyle(style);


        for (int i = 1; i <= list.size(); i++) {

            GetArraignmentCountParam getArraignmentCountParam = list.get(i - 1);

            row = sheet.createRow((int) i + 1);
            cell= row.createCell((short) 0); // 序号
            cell.setCellValue((double) (i));
            cell.setCellStyle(style);
            if (null != getArraignmentCountParam) {
                /*Integer timeCount = base_arraignmentCount.getTimeCount();
                Integer translatextCount = base_arraignmentCount.getTranslatextCount();*/
                cell=row.createCell((short) 1);
                cell.setCellValue(getArraignmentCountParam.getUsername()); // 人员名称
                cell.setCellStyle(style);
                cell=row.createCell((short) 2);
                cell.setCellValue(getArraignmentCountParam.getWorkname());// 工作单位
                cell.setCellStyle(style);
                cell=row.createCell((short) 3);
                cell.setCellValue(getArraignmentCountParam.getArraignmentcount());// 询问总次数
                cell.setCellStyle(style);
                cell=row.createCell((short) 4);
                cell.setCellValue(getArraignmentCountParam.getRecordadmincount());// 记录总次数
                cell.setCellStyle(style);
                cell= row.createCell((short) 5);
                cell.setCellValue(getArraignmentCountParam.getRecordcount());// 笔录总数
                cell.setCellStyle(style);
                /*row.createCell((short) 6).setCellValue(timeCount == null ? 0 : timeCount);// 录音时长
                row.createCell((short) 7).setCellValue(translatextCount == null ? 0 : translatextCount);// 笔录字数*/
            }

        }




        try {
            //String zipspath = OutsideDataRead.getproperty(OutsideDataRead.sys_pro, "zipspath");
            // 创建目录
            String filePath=PropertiesListenerConfig.getProperty("file.basepath");
            String filePathNew = filePath + "/zips";
            File fileMkdir = new File(filePathNew);
            if (!fileMkdir.exists()) {
                //如果不存在，就创建该目录
                fileMkdir.mkdirs();
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
            String format = df.format(new Date());// new Date()为获取当前系统时间

            String path = filePathNew + "/笔录统计表" + format + ".xls";
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();

            String uploadpath= OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),path);
            String basepath = PropertiesListenerConfig.getProperty("upload.basepath");
            String uploadbasepath = ServerIpCache.getServerIp();
            basepath = basepath.replace("localhost", uploadbasepath);
            result.setData(basepath + uploadpath);

            this.changeResultToSuccess(result);
            result.setMessage("表格导出成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("获取下载案件失败");
        }

        return result;
    }

}
