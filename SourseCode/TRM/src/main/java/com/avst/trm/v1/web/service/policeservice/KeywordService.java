package com.avst.trm.v1.web.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_roleMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.req.basereq.KeywordParam;
import com.avst.trm.v1.web.vo.basevo.KeywordListVO;
import com.avst.trm.v1.web.vo.basevo.RoleListVO;
import com.avst.trm.v1.web.vo.basevo.UserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordService extends BaseService {

    @Autowired
    private Base_keywordMapper keywordMapper;


    /**
     * 条件查询
     * @param result
     * @param username  13 4
     */
    public void getadminlist(RResult<Integer> result, String username){

        if(null==result){
            result=new RResult<Integer>();
        }
        try {
            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
              ew.eq("a.id",1);
            System.out.println(ew.getSqlSegment()+"---------");
            int count=keywordMapper.selectCount(ew);
            result.setData(count);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

    /**
     * 分页查询
     * @param result
     * @param param
     */
    public void findKeywordlist(RResult<KeywordListVO> result, KeywordParam param){

        if(null==result){
            result=new RResult<KeywordListVO>();
        }
        List<Base_keyword> list=new ArrayList<Base_keyword>();
        try {
//分页的条件，基本上都有
            KeywordListVO getlist3VO=new KeywordListVO();
            EntityWrapper ew=new EntityWrapper();
//            ew.setEntity(new Admininfo());
//            ew.between("id",2,5);
            if(null!=param){
                if(StringUtils.isNotEmpty(param.getText())){
                    ew.like("text",param.getText());
                }
            }

            int count=keywordMapper.selectCount(ew);
            param.setRecordCount(count);
            getlist3VO.setPageparam(param);

//current 第多少页，size 每页多少条
            Page<Base_role> page=new Page<Base_role>(param.getCurrPage(),param.getPageSize());
            page.setTotal(count);


//            page.setRecords(list);
            list=keywordMapper.selectPage(page,ew );

            System.out.println(page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());

            if(null!=list&&list.size() > 0){
                getlist3VO.setPagelist(list);
            }

            result.setData(getlist3VO);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            System.out.println("请求结束");
        }
    }

    /**
     * 多表查询
     * @param result
     */
    public void getadminlist3(RResult<UserListVO> result, Getlist3Param param){

        if(null==result){
            result=new RResult<UserListVO>();
        }
        List<AdminAndAdminRole> list=new ArrayList<AdminAndAdminRole>();
        try {
            UserListVO getlist3VO=new UserListVO();
            EntityWrapper ew=new EntityWrapper();
            if(null!=param){
                if(StringUtils.isNotEmpty(param.getName())){

                    ew.like("username",param.getName());
                }
            }
            int count = keywordMapper.selectCount(ew);
            param.setRecordCount(count);
            getlist3VO.setPageparam(param);
//current 第多少页，size 每页多少条
            Page<AdminAndAdminRole> page=new Page<AdminAndAdminRole>(param.getCurrPage(),param.getPageSize());
//            page.setRecords(list);
            list=keywordMapper.selectPage(page,ew );

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
