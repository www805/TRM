package com.avst.trm.v1.common.conf.socketio;

import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * NettySocketConfig配置
 */
@org.springframework.context.annotation.Configuration
public class NettySocketConfig implements ApplicationRunner {
    public static SocketIOServer socketIoServer;
    public static Configuration config = new  Configuration();
    public static List<SocketIOClient> clients = new ArrayList<SocketIOClient>();//用于保存所有客户端


    @Bean
    public SocketIOServer socketIOServer() {
        config.setUpgradeTimeout(10000);
        config.setPingInterval(60000);
        config.setPingTimeout(180000);
       final  SocketIOServer socketIoServer = new SocketIOServer(config);
        return socketIoServer;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

    //有缺陷
    public void StartSocketio(){
        String SOCKETIO_HOST= PropertiesListenerConfig.getProperty("socketio.server.host");
        String SOCKETIO_PORT= PropertiesListenerConfig.getProperty("socketio.server.port");
        if (null!=SOCKETIO_HOST&&null!=SOCKETIO_PORT){
            config.setHostname(SOCKETIO_HOST);
            config.setPort(Integer.valueOf(SOCKETIO_PORT));
            final  SocketIOServer socketIoServer =new SocketIOServer(config);
            socketIoServer.addConnectListener(new ConnectListener() {
                public void onConnect(SocketIOClient client) {
                    // 判断是否有客户端连接
                    if (client != null) {
                        clients.add(client);
                        LogUtil.intoLog(this.getClass(),"socketio连接成功__clientId_____" + client.getSessionId().toString());
                    } else {
                        LogUtil.intoLog(this.getClass(),"socketio没有人连接上_____");
                    }
                }
                public void onDisconnect(SocketIOClient client) {
                    // 判断是否有客户端连接
                    if (client != null) {
                        clients.remove(client);
                        LogUtil.intoLog(this.getClass(),"客户端_____" + client.getSessionId() + "_____断开连接");
                    }
                }
            });
            socketIoServer.start();
        }else {
            LogUtil.intoLog(this.getClass(),"socketiode IP和端口为空_____");
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StartSocketio();
    }
}
