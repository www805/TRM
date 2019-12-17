package com.avst.trm.v1.web.standaloneweb.service;

import com.avst.trm.v1.common.cache.AppCache;
import com.avst.trm.v1.common.cache.CommonCache;
import com.avst.trm.v1.common.cache.ServerIpCache;
import com.avst.trm.v1.common.cache.param.AppCacheParam;
import com.avst.trm.v1.common.datasourse.base.entity.Base_filesave;
import com.avst.trm.v1.common.datasourse.base.entity.Base_serverconfig;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_filesaveMapper;
import com.avst.trm.v1.common.datasourse.base.mapper.Base_serverconfigMapper;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.baseaction.BaseService;
import com.avst.trm.v1.common.util.baseaction.RResult;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import com.avst.trm.v1.common.util.sq.SQEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class MainStandaloneService extends BaseService {

    @Autowired
    private Base_filesaveMapper base_filesaveMapper;

    @Autowired
    private Base_serverconfigMapper base_serverconfigMapper;

    public void getNavList(RResult result) {
        String nav_file_name= PropertiesListenerConfig.getProperty("nav.file.name");

        AppCacheParam cacheParam = AppCache.getAppCacheParam();
        String path = OpenUtil.getXMSoursePath() + "\\" + nav_file_name + ".yml";
        if(null == cacheParam.getData()){
            String myIP = ServerIpCache.getServerIp();
            FileInputStream fis = null;
            try {
                Base_serverconfig serverconfig = base_serverconfigMapper.selectById(1);

//                if (StringUtils.isNotEmpty(serverconfig.getSyslogo_filesavessid())) {
//                    Base_filesave filesaveSyslogo = new Base_filesave();
//                    filesaveSyslogo.setSsid(serverconfig.getSyslogo_filesavessid());
//                    Base_filesave syslogo = base_filesaveMapper.selectOne(filesaveSyslogo);
//                    if (null!=syslogo){
//                        cacheParam.setSyslogoimage("http://" + myIP + syslogo.getRecorddownurl());
//                    }
//                }

                if (StringUtils.isNotEmpty(serverconfig.getClient_filesavessid())) {
                    Base_filesave filesaveClientlogo = new Base_filesave();
                    filesaveClientlogo.setSsid(serverconfig.getClient_filesavessid());
                    Base_filesave clientlogo = base_filesaveMapper.selectOne(filesaveClientlogo);
                    if (null!=clientlogo){
                        cacheParam.setClientimage("http://" + myIP + clientlogo.getRecorddownurl());
                    }
                }

                //获取授权信息
                CommonCache.gnlist();
                SQEntity getSQEntity = CommonCache.getSQEntity;//获取系统授权信息
                String gnlist = getSQEntity.getGnlist();

                fis = new FileInputStream(path);

                Yaml yaml = new Yaml();
                Map<String,Object> map = yaml.load(fis);

                //判断如果是单机版，就获取单机版的菜单栏
                String cwebFile=PropertiesListenerConfig.getProperty("nav.file.oem");
                String application_name=PropertiesListenerConfig.getProperty("spring.application.name");

                Map<String,Object> avstYml = (Map<String, Object>) map.get(application_name);
                Map<String,Object> fileYml = (Map<String, Object>) avstYml.get(cwebFile);
                Map<String,Object> zkYml = (Map<String, Object>) map.get("zk");
                Map<String,Object> guidepage = (Map<String, Object>) zkYml.get("guidepage");
                String guidepageUrl = (String) guidepage.get("url");
                fileYml.put("bottom", map.get("bottom"));
                fileYml.put("gnlist", map.get(gnlist));
                String hostAddress = ServerIpCache.getServerIp();

                cacheParam.setData(fileYml);
                cacheParam.setGuidepageUrl("http://" + hostAddress + guidepageUrl);
                LogUtil.intoLog(1, this.getClass(), "外部配置文件，获取菜单栏成功");
            } catch (IOException e) {
                LogUtil.intoLog(4, this.getClass(), "没找到外部配置文件：" + path);
            }finally {
                if(null != fis){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        result.setData(cacheParam);
        changeResultToSuccess(result);
    }
}
