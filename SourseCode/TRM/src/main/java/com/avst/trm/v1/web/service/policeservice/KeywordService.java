package com.avst.trm.v1.web.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.req.basereq.KeywordParam;
import com.avst.trm.v1.web.req.policereq.AddOrUpdateKeywordParam;
import com.avst.trm.v1.web.vo.basevo.KeywordListVO;
import com.avst.trm.v1.web.vo.basevo.UserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class KeywordService extends BaseService {

    @Autowired
    private Base_keywordMapper keywordMapper;


    /**
     * 通过id查询关键字
     * @param result
     * @param id
     */
    public void getKeywordById(RResult<Base_keyword> result, int id){

        Base_keyword keyword = keywordMapper.selectById(id);

        if(null==result){
            result=new RResult<Base_keyword>();
        }

        if(keyword != null){
            if (keyword.getShieldbool() == -1) {
                keyword.setShieldbool(0);
            }

            result.setData(keyword);
            this.changeResultToSuccess(result);
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

    /**
     * 修改关键字
     * @param rResult
     * @param keyword
     */
    public void UpdateKeyword(RResult rResult, AddOrUpdateKeywordParam keyword) {
        if(!checkKeyword(rResult, keyword)){
            return;
        }
        Integer update = keywordMapper.updateById(keyword);
        if (update > 0) {
            rResult.setData(update);
            this.changeResultToSuccess(rResult);
        }
    }

    /**
     * 新增关键字
     * @param rResult
     * @param keyword
     */
    public void AddKeyword(RResult rResult, AddOrUpdateKeywordParam keyword) {
        if(!checkKeyword(rResult, keyword)){
            return;
        }

//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

        keyword.setCreatetime(new Date());
        Integer insert = keywordMapper.insert(keyword);
        if (insert > 0) {
            rResult.setData(insert);
            this.changeResultToSuccess(rResult, "新增关键字成功!");
        }
    }

    /**
     * 删除关键字
     * @param rResult
     * @param keyword
     */
    public void deleteKeyword(RResult rResult, AddOrUpdateKeywordParam keyword) {
        if (null == keyword.getId()) {
            return;
        }

        Integer integer = keywordMapper.deleteById(keyword.getId());
        if (integer > 0) {
            rResult.setData(integer);
            this.changeResultToSuccess(rResult, "关键字删除成功!");
        }
    }

    /**
     * 校验参数
     * @param rResult
     * @param keyword
     * @return
     */
    private Boolean checkKeyword(RResult rResult, AddOrUpdateKeywordParam keyword) {

        if(StringUtils.isEmpty(keyword.getText()) || StringUtils.isEmpty(keyword.getReplacetext())){
            rResult.setMessage("关键字名称 或 替换字符不能为空");
            return false;
        }
        if(StringUtils.isEmpty(keyword.getColor()) || StringUtils.isEmpty(keyword.getBackgroundcolor())){
            rResult.setMessage("字体颜色 或 背景颜色不能为空");
            return false;
        }
        return true;
    }


}
