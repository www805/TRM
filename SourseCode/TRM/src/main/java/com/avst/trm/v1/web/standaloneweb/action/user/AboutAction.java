package com.avst.trm.v1.web.standaloneweb.action.user;

import com.avst.trm.v1.common.util.DateUtil;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.baseaction.ReqParam;
import com.avst.trm.v1.web.cweb.req.policereq.UpdateArraignmentParam;
import com.avst.trm.v1.web.standaloneweb.req.GetAboutParam;
import com.avst.trm.v1.web.standaloneweb.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cweb/police/about")
public class AboutAction extends BaseAction {

    @Autowired
    private AboutService aboutService;


    /***
     * 获取关于信息
     * @param param
     * @return
     */
    @RequestMapping("/getAbout")
    @ResponseBody
    public  RResult getAbout(@RequestBody ReqParam<GetAboutParam> param){
        RResult result=this.createNewResultOfFail();
        if (null==param){
            result.setMessage("参数为空");
        }else if (!checkToken(param.getToken())){
            result.setMessage("授权异常");
        }else{
            aboutService.getAbout(result,param.getParam());
        }
        result.setEndtime(DateUtil.getDateAndMinute());
        return result;
    }


    /**
     * 跳转==》关于
     */
    @GetMapping("toabout")
    public ModelAndView toabout(Model model){
        model.addAttribute("title","关于");
        return new ModelAndView("standalone_web/user/about", "Model", model);
    }
}
