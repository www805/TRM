package com.avst.trm.v1.web.sweb.service.policeservice;


import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.AppServerCache;
import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.web.sweb.req.policereq.ServerconfigParam;
import com.avst.trm.v1.web.sweb.vo.basevo.GetServerConfigByIdVO;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class ServerConfigService extends BaseService {

    @Autowired
    private Base_serverconfigMapper serverconfigMapper;

    @Autowired
    private Base_filesaveMapper filesaveMapper;


    /**
     * 通过id查询配置项
     * @param result
     */
    public void getServerConfigById(RResult<GetServerConfigByIdVO> result){

        GetServerConfigByIdVO serverConfigByIdVO = new GetServerConfigByIdVO();

        if(null==result){
            result=new RResult<GetServerConfigByIdVO>();
        }

        Base_serverconfig serverconfig = serverconfigMapper.selectById(1);
        String uploadbasepath=PropertiesListenerConfig.getProperty("upload.basepath");//地址请勿使用获取ip拼接的方式！！！
        String myIP = ServerIpCache.getServerIp();
        if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
            Base_filesave filesaveSyslogo = new Base_filesave();
            filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
            Base_filesave syslogo = filesaveMapper.selectOne(filesaveSyslogo);
            if (null!=syslogo){
                serverConfigByIdVO.setSyslogoimage(uploadbasepath+ syslogo.getRecorddownurl());
            }
        }

        if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
            Base_filesave filesaveClientlogo = new Base_filesave();
            filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
            Base_filesave clientlogo = filesaveMapper.selectOne(filesaveClientlogo);
            if (null!=clientlogo){
                serverConfigByIdVO.setClientimage(uploadbasepath + clientlogo.getRecorddownurl());
            }
        }


        serverConfigByIdVO.setServerconfig(serverconfig);

        if(serverconfig != null){
            result.setData(serverConfigByIdVO);
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

        boolean isip = OpenUtil.isIp(serverconfig.getServerip());
        if(isip == false){
            rResult.setMessage("填写的服务器IP，不是一个正确的IP");
            return;
        }

        AppCache.delAppCacheParam();//清空客户端缓存
        AppServerCache.delAppServerCache();//清空服务器缓存

        Base_serverconfig baseserverconfig = serverconfigMapper.selectById(1);
        baseserverconfig.setClientname(serverconfig.getClientname());
        baseserverconfig.setClient_filesavessid(serverconfig.getClient_filesavessid());
        baseserverconfig.setSyslogo_filesavessid(serverconfig.getSyslogo_filesavessid());
        baseserverconfig.setServerip(serverconfig.getServerip());
        baseserverconfig.setSysname(serverconfig.getSysname());
        baseserverconfig.setServerport(serverconfig.getServerport());

        Integer update = serverconfigMapper.updateById(baseserverconfig);
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
    public void uploadByImg(RResult rResult, MultipartFile file, String datassid) {

        if (file.isEmpty()) {
            rResult.setMessage("上传失败，请选择文件");
            return;
        }


        //1 获取文件名
        String fileName = file.getOriginalFilename();

        //2 获取随机文件名
//        String imageName = getRandomFileName();
        String imageName = OpenUtil.getUUID_32();

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
//        File fileMkdir = new File(filePath);
//        if (!fileMkdir.exists()) {
//            //如果不存在，就创建该目录
//            fileMkdir.mkdirs();
//        }

        String filePath=PropertiesListenerConfig.getProperty("spring.images.filePath");

        String filePathNew = OpenUtil.createpath_fileByBasepath(filePath);

        //6 把图片保存到指定位置
//        upload.transferTo(new File(file, imageName));

        String realpath = filePathNew + imageName;
        File dest = new File(realpath);
        try {
            file.transferTo(dest);

            //解析下载地址
            String uploadpath=OpenUtil.strMinusBasePath(PropertiesListenerConfig.getProperty("file.qg"),realpath);
            rResult.setData(uploadpath);

            //上传的文件保存到数据库表里
            Base_serverconfig serverconfig = serverconfigMapper.selectById(1);

            Base_filesave filesave = new Base_filesave();
            filesave.setRealfilename(imageName);//真实路径文件名
            filesave.setUploadfilename(fileName);//文件本身的文件名
            filesave.setRecordrealurl(realpath);//真实存储地址
            filesave.setRecorddownurl(uploadpath);//下载地址

            String ssid = "";
            if("syslogo_filesavessid".equals(datassid)){
                ssid = serverconfig.getSyslogo_filesavessid();
            }else{
                ssid = serverconfig.getClient_filesavessid();
            }

            filesave.setDatassid(serverconfig.getSsid());//从属表的ssid

            EntityWrapper ew=new EntityWrapper();
            ew.eq("ssid", ssid);

            Integer update = filesaveMapper.update(filesave, ew);
            if (update == 0) {
                filesave.setSsid(OpenUtil.getUUID_32());
                update = filesaveMapper.insert(filesave);
                this.changeResultToSuccess(rResult);
                rResult.setMessage("上传成功");

                //同时把字段更新到系统配置表里
                if ("syslogo_filesavessid".equals(datassid)) {
                    serverconfig.setSyslogo_filesavessid(filesave.getSsid());
                } else {
                    serverconfig.setClient_filesavessid(filesave.getSsid());
                }
                Integer integer = serverconfigMapper.updateById(serverconfig);
                LogUtil.intoLog(this.getClass(),"同步系统配置： " + integer);
            }

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

        if(StringUtils.isEmpty(serverconfig.getSysname()) || StringUtils.isEmpty(serverconfig.getClientname())){
            rResult.setMessage("客户端名称 或 系统名称不能为空");
            return false;
        }
        if(StringUtils.isEmpty(serverconfig.getServerip()) || StringUtils.isEmpty(serverconfig.getServerport())){
            rResult.setMessage("服务器IP 或 服务器端口不能为空");
            return false;
        }
        return true;
    }



}
