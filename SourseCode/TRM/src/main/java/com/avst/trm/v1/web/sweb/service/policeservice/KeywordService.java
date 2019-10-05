package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.sweb.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.sweb.req.basereq.KeywordParam;
import com.avst.trm.v1.web.sweb.req.policereq.AddOrUpdateKeywordParam;
import com.avst.trm.v1.web.sweb.vo.basevo.KeywordListVO;
import com.avst.trm.v1.web.sweb.vo.basevo.UserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class KeywordService extends BaseService {

    @Autowired
    private  Base_keywordMapper keywordMapper;


    /**
     * 通过id查询关键字
     * @param result
     * @param ssid
     */
    public void getKeywordById(RResult<Base_keyword> result, String ssid){

        Base_keyword base_keyword = new Base_keyword();
        base_keyword.setSsid(ssid);
        Base_keyword keyword = keywordMapper.selectOne(base_keyword);

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

            LogUtil.intoLog(this.getClass(),page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());

            if(null!=list&&list.size() > 0){
                getlist3VO.setPagelist(list);
            }

            result.setData(getlist3VO);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.fillInStackTrace();
        }finally {
            LogUtil.intoLog(this.getClass(),"请求结束");
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

            LogUtil.intoLog(this.getClass(),page.getSize()+"-----"+page.getCurrent()+"-----"+
                    page.getTotal()+"-----"+page.getPages());
            if(null!=list&&list.size() > 0){
                getlist3VO.setPagelist(list);
            }

            result.setData(getlist3VO);
            this.changeResultToSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            LogUtil.intoLog(this.getClass(),"请求结束");
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
        EntityWrapper<Base_keyword> ew = new EntityWrapper<>();
        ew.eq("text", keyword.getText());
        ew.ne("ssid", keyword.getSsid());
        List<Base_keyword> base_keywords = keywordMapper.selectList(ew);
        if (null != base_keywords && base_keywords.size() > 0) {
            rResult.setMessage("关键字已存在，请重新输入");
            return;
        }

        EntityWrapper<Base_keyword> ew2 = new EntityWrapper<>();
        ew2.eq("ssid", keyword.getSsid());

        Integer update = keywordMapper.update(keyword, ew2);
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

        EntityWrapper<Base_keyword> ew = new EntityWrapper<>();
        ew.eq("text", keyword.getText());
        List<Base_keyword> base_keywords = keywordMapper.selectList(ew);
        if (null != base_keywords && base_keywords.size() > 0) {
            rResult.setMessage("关键字已存在，请重新输入");
            return;
        }

        keyword.setCreatetime(new Date());
        keyword.setSsid(OpenUtil.getUUID_32());
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
        if (null == keyword.getSsid()) {
            return;
        }

        EntityWrapper<Base_keyword> ew = new EntityWrapper<>();
        ew.eq("ssid", keyword.getSsid());
        Integer integer = keywordMapper.delete(ew);
        if (integer > 0) {
            rResult.setData(integer);
            this.changeResultToSuccess(rResult, "关键字删除成功!");
        }
    }

    /**
     * 修改关键字状态
     * @param rResult
     * @param keyword
     */
    public void updateShieldbool(RResult rResult, AddOrUpdateKeywordParam keyword) {
        if(null != keyword.getShieldbool()){

            EntityWrapper<Base_keyword> ew = new EntityWrapper<>();
            ew.eq("ssid", keyword.getSsid());

            Integer integer = keywordMapper.update(keyword, ew);
            if (integer > 0) {
                rResult.setData(integer);
                this.changeResultToSuccess(rResult, "关键字屏蔽修改成功!");
                rResult.setMessage("关键字屏蔽修改成功");
            }
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
      /* 可以为空 if(StringUtils.isEmpty(keyword.getColor()) || StringUtils.isEmpty(keyword.getBackgroundcolor())){
            rResult.setMessage("字体颜色 或 背景颜色不能为空");
            return false;
        }*/
        return true;
    }



    public static void main(String[] args) {
        //用于默认关键字添加
        /*List<String> prolist= ReadWriteFile.readTxtFileToList("C:\\Users\\admin\\Desktop\\mgz.txt","GBK");
        for (String s : prolist) {
            s=s.replace(" ", "");
            System.out.println(s);
            if (StringUtils.isNotBlank(s)){
                AddOrUpdateKeywordParam keyword=new AddOrUpdateKeywordParam();
                keyword.setText(s);
                Integer len=s.length();
                String a="";
                if (null!=len){
                    for (int i = 0; i < len; i++) {
                        a+="*";

                    }
                }
                keyword.setReplacetext(a);
                keyword.setShieldbool(1);
                keyword.setCreatetime(new Date());
                keyword.setSsid(OpenUtil.getUUID_32());
                Integer insert = keywordMapper.insert(keyword);
                if (insert > 0) {
                    System.out.println("添加成功");
                }
            }
        }*/
    }



}
