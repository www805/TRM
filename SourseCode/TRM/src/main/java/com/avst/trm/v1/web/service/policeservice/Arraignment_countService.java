package com.avst.trm.v1.web.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_arraignmentCount;
import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndWorkunit;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_arraignmentCountMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Arraignment_countParam;
import com.avst.trm.v1.web.vo.basevo.ArraignmentCountVO;
import com.avst.trm.v1.web.vo.basevo.KeywordListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Arraignment_countService extends BaseService {

    @Autowired
    private Base_arraignmentCountMapper arraignmentCountMapper;


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
//            if(null!=param){
//                if(StringUtils.isNotEmpty(param.getText())){
//                    ew.like("text",param.getText());
//                }
//            }
//
//            int count=keywordMapper.selectCount(ew);
//            param.setRecordCount(count);
//            getlist3VO.setPageparam(param);
//
////current 第多少页，size 每页多少条
//            Page<Base_role> page=new Page<Base_role>(param.getCurrPage(),param.getPageSize());
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


}
