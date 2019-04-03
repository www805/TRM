package com.avst.trm.v1.web.action;

import com.avst.trm.v1.common.datasourse.mysql.entity.Admininfo;
import com.avst.trm.v1.common.util.baseaction.BaseAction;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.service.CeshiService;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ceshi")
public class CeshiAction extends BaseAction{

    @Autowired
    private CeshiService ceshiService;

    @GetMapping(value = "/ceshi")
    public RResult getlist(String username) {
        RResult rResult=createNewResultOfFail();
        ceshiService.getadminlist(rResult,username);
        return rResult;
    }

}
