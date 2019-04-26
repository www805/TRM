package com.avst.trm.v1.web.action.baseaction;

import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.param.CheckSQParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.ActionAndinterfaceAndPage;
import com.avst.trm.v1.common.util.SpringUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.report.toupserver.common.conf.AddDataToSynchronizeDataConf;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.req.basereq.GotolistParam;
import com.avst.trm.v1.web.service.baseservice.ActionService;
import com.avst.trm.v1.web.service.baseservice.CeshiService;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/web/ceshi")
public class CeshiAction extends BaseAction {

    @Autowired
    private CeshiService ceshiService;

    @Autowired
    private ActionService actionService;

    @RequestMapping(value = "/ceshi")
    @ResponseBody
    public RResult getlist(String username) {
        RResult rResult=createNewResultOfFail();
//        ceshiService.getadminlist(rResult,username);

//        ToUpServerBaseReqClass.initsynchronizeddata();

        BaseMapper mapper=(BaseMapper)SpringUtil.getBean("base_filesaveMapper");
        Base_filesave base_filesave=new Base_filesave();
        base_filesave.setDatassid("2we34rt6");
        base_filesave.setRealfilename("测试文件");
        base_filesave.setRecordrealurl("测试文件路径");
        base_filesave.setSsid("sjcyfnj572");
        base_filesave.setId(23);
        Object o=base_filesave;
        System.out.println("-------------"+mapper.insert(o));

        return rResult;
    }

    @RequestMapping(value = "/sq")
    @ResponseBody
    public String sq() {

        String msg="";
        String code="";
        CheckSQParam checkSQParam=CommonCache.checkSQ();

        return checkSQParam.getMsg();
    }

    @RequestMapping(value = "/ceshi2")
    @ResponseBody
    public RResult getlist2(Model model,Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        ceshiService.getadminlist3(rResult,param);
        return rResult;
    }

    @RequestMapping(value = "/ceshi3")

    public ModelAndView getlist3(Model model,Getlist3Param param) {
        RResult rResult=createNewResultOfFail();
        param.setPageSize(3);//测试
        ceshiService.getadminlist3(rResult,param);

        model.addAttribute("result", rResult);

        return new ModelAndView("list", "userModel", model);

    }

    @RequestMapping(value = "/users/{id}")
    public ModelAndView getuserbyid(Model model,@PathVariable("id")String id) {

        model.addAttribute("title", "第"+id+"个人");
        return new ModelAndView("AdminAndAdminRole", "userModel", model);

    }

    @RequestMapping(value = "/gotomain")
    public ModelAndView gotomain(Model model) {

        model.addAttribute("title", "layui测试主页");

        return new ModelAndView("main", "main", model);

    }



//    @RequestMapping(value = "/gotolist")
    @RequestMapping(value = "/{param.getActionid()}")
    public ModelAndView gotolist(Model model,@PathVariable("param") GotolistParam param) {

        RResult rResult=createNewResultOfFail();

        ActionAndinterfaceAndPage actionAndinterfaceAndPage= actionService.getAction(param.getActionid(),param.getPageid());//以后给缓存
        if(null!=actionAndinterfaceAndPage){

            if(actionAndinterfaceAndPage.getTypename().equals("base")){
                ceshiService.gotolist_base(rResult,param,actionAndinterfaceAndPage);
            }else if(actionAndinterfaceAndPage.getTypename().equals("police")){

            }else if(actionAndinterfaceAndPage.getTypename().equals("meeting")){

            }else if(actionAndinterfaceAndPage.getTypename().equals("court")){

            }else if(actionAndinterfaceAndPage.getTypename().equals("dis")){

            }
        }else{
            rResult.setMessage("对应处理事件异常 gotolist");
            return new ModelAndView("404", "login", model);//给一个专门处理跳页面出错的页面（如果只是返回数据就不需要用这个）
        }
        model.addAttribute("result", rResult);
        return new ModelAndView(rResult.getNextpageid(), "login", model);
    }


//    public BaseService getBaseService(ActionAndinterfaceAndPage actionAndinterfaceAndPage) {
//
//        if (actionAndinterfaceAndPage.getTypename().equals("base")) {
//            return ceshiService;
//        } else if (actionAndinterfaceAndPage.getTypename().equals("police")) {
//            return ceshiService;
//        } else if (actionAndinterfaceAndPage.getTypename().equals("meeting")) {
//            return ceshiService;
//        } else if (actionAndinterfaceAndPage.getTypename().equals("court")) {
//            return ceshiService;
//        } else if (actionAndinterfaceAndPage.getTypename().equals("dis")) {
//            return ceshiService;
//        }
//        return null;
//    }
//}



}
