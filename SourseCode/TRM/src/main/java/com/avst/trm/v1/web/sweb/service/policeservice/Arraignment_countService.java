package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_arraignmentCount;
import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_admininfoMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.sweb.req.basereq.Arraignment_countParam;
import com.avst.trm.v1.web.sweb.vo.basevo.ArraignmentCountVO;
import com.avst.trm.v1.web.sweb.vo.basevo.KeywordListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class Arraignment_countService extends BaseService {

    @Autowired
    private Base_admininfoMapper arraignmentCountMapper;

    @Value("${file.basepath}")
    private String filePath;

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
//            System.out.println(page.getSize()+"-----"+page.getCurrent()+"-----"+
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
//            System.out.println("请求结束");
//        }
    }

    /**
     * 多表查询
     * @param result
     */
    public void getArraignment_countList(RResult<ArraignmentCountVO> result, Arraignment_countParam param){

        if(null==result){
            result=new RResult<ArraignmentCountVO>();
        }
        List<Base_arraignmentCount> list = new ArrayList<Base_arraignmentCount>();
        try {
            ArraignmentCountVO getlist3VO=new ArraignmentCountVO();
            EntityWrapper ew=new EntityWrapper();

            Base_arraignmentCount arraignmentCount = null;
            if(null!=param){

                if(StringUtils.isNotEmpty(param.getTimes())){
                    ew.ge("r.time",param.getTimes());
                }
                if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                    ew.between("r.createtime", param.getStarttime(), param.getEndtime());
                }

            }
            int count = arraignmentCountMapper.getArraignmentCountCount(ew);
            param.setRecordCount(count);
            getlist3VO.setPageparam(param);
//current 第多少页，size 每页多少条
            Page<Base_arraignmentCount> page=new Page<Base_arraignmentCount>(param.getCurrPage(),param.getPageSize());
//            page.setRecords(list);
            list = arraignmentCountMapper.getArraignmentCountList(page, ew);

            if(null!=list&&list.size() > 0){

                for (int i = 0; i < list.size(); i++) {

                    EntityWrapper ew2=new EntityWrapper();

                    ew2.eq("a.id", list.get(i).getId());

                    arraignmentCount = arraignmentCountMapper.getArraignmentCount(ew2);

                    list.get(i).setRecordCount(arraignmentCount.getRecordCount());
                    list.get(i).setRecordrealCount(arraignmentCount.getRecordrealCount());
                    list.get(i).setRecordtimeCount(arraignmentCount.getRecordtimeCount());
                    list.get(i).setTimeCount(arraignmentCount.getTimeCount());
                    list.get(i).setTranslatextCount(arraignmentCount.getTranslatextCount());
                }

            }
//            AdminAndWorkunit list1 = arraignmentCountMapper.getList(page, ew);

            System.out.println(page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());
            if(null!=list&&list.size() > 0){
                getlist3VO.setPagelist(list);
            }

            result.setData(getlist3VO);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }


    public RResult exportExcel(RResult result, Arraignment_countParam param) {

        EntityWrapper ew=new EntityWrapper();

        if(null!=param){

            if(StringUtils.isNotEmpty(param.getTimes())){
                ew.ge("r.time",param.getTimes());
            }
            if(StringUtils.isNotEmpty(param.getStarttime()) && StringUtils.isNotEmpty(param.getEndtime())){
                ew.between("r.createtime", param.getStarttime(), param.getEndtime());
            }

        }
        List<Base_arraignmentCount> list = arraignmentCountMapper.getArraignmentCountListNoPage(ew);

        Base_arraignmentCount arraignmentCount = null;

        if (null != list && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                EntityWrapper ew2 = new EntityWrapper();
                ew2.eq("a.id", list.get(i).getId());
                arraignmentCount = arraignmentCountMapper.getArraignmentCount(ew2);

                list.get(i).setRecordCount(arraignmentCount.getRecordCount());
                list.get(i).setRecordrealCount(arraignmentCount.getRecordrealCount());
                list.get(i).setRecordtimeCount(arraignmentCount.getRecordtimeCount());
                list.get(i).setTimeCount(arraignmentCount.getTimeCount());
                list.get(i).setTranslatextCount(arraignmentCount.getTranslatextCount());
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
        sheet.setColumnWidth(6, 5000);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("人员名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("笔录总量");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("语音笔录总量");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("笔录总时长");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("录音时长");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("笔录字数");
        cell.setCellStyle(style);

        for (int i = 0; i < list.size(); i++) {

            row = sheet.createRow((int) i + 1);
            row.createCell((short) 0).setCellValue((double) (i + 1)); // 序号
            row.createCell((short) 1).setCellValue(list.get(i).getUsername()); // 人员名称
            row.createCell((short) 2).setCellValue(list.get(i).getRecordCount());// 笔录总量
            row.createCell((short) 3).setCellValue(list.get(i).getRecordrealCount());// 语音笔录总量
            row.createCell((short) 4).setCellValue(list.get(i).getRecordtimeCount());// 笔录总时长
            row.createCell((short) 5).setCellValue(list.get(i).getTimeCount());// 录音时长
            row.createCell((short) 6).setCellValue(list.get(i).getTranslatextCount());// 笔录字数

        }


        try {
            //String zipspath = OutsideDataRead.getproperty(OutsideDataRead.sys_pro, "zipspath");
            // 创建目录
            String filePathNew = filePath + "/zips";
            File fileMkdir = new File(filePathNew);
            if (!fileMkdir.exists()) {
                //如果不存在，就创建该目录
                fileMkdir.mkdirs();
            }

            String path = filePathNew + "/提讯案件列表.xls";
            FileOutputStream fout = new FileOutputStream(path);
            wb.write(fout);
            fout.close();

            String uploadpath= OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),path);
            result.setData(uploadpath);

            this.changeResultToSuccess(result);
            result.setMessage("表格导出成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("获取下载案件失败");
        }

        return result;
    }

}
