package com.avst.trm.v1.common.conf.socketio;

import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * NettySocketConfig配置
 */
@org.springframework.context.annotation.Configuration
public class SocketIOConfig implements ApplicationRunner {
    public static List<SocketIOClient> clients = new ArrayList<SocketIOClient>();//用于保存所有客户端

    public void StartSocketio(){
        String SOCKETIO_HOST= PropertiesListenerConfig.getProperty("socketio.server.host");
        String SOCKETIO_PORT= PropertiesListenerConfig.getProperty("socketio.server.port");
        if (null!=SOCKETIO_HOST&&null!=SOCKETIO_PORT){
            Configuration config = new  Configuration();
            config.setHostname(SOCKETIO_HOST);
            config.setPort(Integer.valueOf(SOCKETIO_PORT));
            config.setUpgradeTimeout(10000);
            config.setPingInterval(60000);
            config.setPingTimeout(180000);
            final SocketIOServer socketIoServer =new SocketIOServer(config);

            //监听连接时
            socketIoServer.addConnectListener(new ConnectListener() {
                @Override
                public void onConnect(SocketIOClient socketIOClient) {
                    // 判断是否有客户端连接
                    if (socketIOClient != null) {
                        LogUtil.intoLog(this.getClass(),"客户端_____" + socketIOClient.getSessionId() + "_____已连接");
                        clients.add(socketIOClient);
                    }
                }
            });
            //监听断开时
            socketIoServer.addDisconnectListener(new DisconnectListener() {
                @Override
                public void onDisconnect(SocketIOClient socketIOClient) {
                    if (socketIOClient != null) {
                        LogUtil.intoLog(this.getClass(),"客户端_____" + socketIOClient.getSessionId() + "_____断开连接");
                        clients.remove(socketIOClient);
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
