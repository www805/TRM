package com.avst.trm.v1.web.service.policeservice;


import com.avst.trm.v1.common.datasourse.base.entity.Base_keyword;
import com.avst.trm.v1.common.datasourse.base.entity.Base_role;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.entity.moreentity.AdminAndAdminRole;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_keywordMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.web.req.basereq.Getlist3Param;
import com.avst.trm.v1.web.req.basereq.KeywordParam;
import com.avst.trm.v1.web.req.policereq.AddOrUpdateKeywordParam;
import com.avst.trm.v1.web.req.policereq.ServerconfigParam;
import com.avst.trm.v1.web.vo.basevo.KeywordListVO;
import com.avst.trm.v1.web.vo.basevo.UserListVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ServerConfigService extends BaseService {

    @Autowired
    private Base_serverconfigMapper serverconfigMapper;

    @Value("${spring.images.filePath}")
    private String filePath;

    /**
     * 通过id查询配置项
     * @param result
     */
    public void getServerConfigById(RResult<Base_serverconfig> result){

        Base_serverconfig serverconfig = serverconfigMapper.selectById(1);

        if(null==result){
            result=new RResult<Base_serverconfig>();
        }

        if(serverconfig != null){
            result.setData(serverconfig);
            this.changeResultToSuccess(result);
        }
    }

    /**
     * 修改配置
     * @param rResult
     * @param serverconfig
     */
    public void UpdateServerConfig(RResult rResult, ServerconfigParam serverconfig) {
        if(!checkKeyword(rResult, serverconfig)){
            return;
        }
        Integer update = serverconfigMapper.updateById(serverconfig);
        if (update > 0) {
            rResult.setData(update);
            this.changeResultToSuccess(rResult);
            rResult.setMessage("系统配置修改成功");
        }
    }

    /**
     * 图片上传
     *
     * @param rResult
     * @param file
     */
    public void uploadByImg(RResult rResult, MultipartFile file) {

        if (file.isEmpty()) {
            rResult.setMessage("上传失败，请选择文件");
        }


        //1 获取文件名
        String fileName = file.getOriginalFilename();

        //2 获取随机文件名
        String imageName = getRandomFileName();

        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        imageName += suffix;

        //3 获取绝对路径
//        String basePath = request.getSession().getServletContext().getRealPath("/upload");

        //4 获取两层目录
//        String path = null;
//        try {
//            String serverpath= ResourceUtils.getURL("classpath:static").getPath().replace("%20"," ").replace('/', '\\');
//            path=serverpath.substring(1);//从路径字符串中取出工程路径
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

//        path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

//        String filePath = "D:/uploadImage/";
//        String filePath = path;

        //5 创建目录
        File fileMkdir = new File(filePath);
        if (!fileMkdir.exists()) {
            //如果不存在，就创建该目录
            fileMkdir.mkdir();
        }

        //6 把图片保存到指定位置
//        upload.transferTo(new File(file, imageName));

        File dest = new File(filePath + imageName);
        try {
            file.transferTo(dest);
            this.changeResultToSuccess(rResult);
            rResult.setMessage("上传成功");
            rResult.setData(filePath + imageName);
        } catch (IOException e) {
            rResult.setMessage(e.toString());
        }

    }


    /****
     * 生成随机名
     * @return
     */
    public static String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();

        String str = simpleDateFormat.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

        return rannum + str;// 当前时间
    }


    /**
     * 校验参数
     * @param rResult
     * @param serverconfig
     * @return
     */
    private Boolean checkKeyword(RResult rResult, ServerconfigParam serverconfig) {

//        if(StringUtils.isEmpty(serverconfig.getText()) || StringUtils.isEmpty(serverconfig.getReplacetext())){
//            rResult.setMessage("关键字名称 或 替换字符不能为空");
//            return false;
//        }
//        if(StringUtils.isEmpty(keyword.getColor()) || StringUtils.isEmpty(keyword.getBackgroundcolor())){
//            rResult.setMessage("字体颜色 或 背景颜色不能为空");
//            return false;
//        }
        return true;
    }


}