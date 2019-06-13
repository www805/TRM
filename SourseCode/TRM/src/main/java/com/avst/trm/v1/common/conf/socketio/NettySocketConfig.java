package com.avst.trm.v1.common.conf.socketio;

import com.avst.trm.v1.common.util.LogUtil;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * NettySocketConfig配置
 */
@org.springframework.context.annotation.Configuration
public class NettySocketConfig {
    @Value("${socketio.server.port}")
    private int SOCKETIO_PORT;
    @Value("${socketio.server.host}")
    private String SOCKETIO_HOST;

    @Bean
    public SocketIOServer socketIOServer() {
        //创建Socket，并设置监听端口
        Configuration config = new  Configuration();
        // 设置主机名，默认是0.0.0.0
        config.setHostname(SOCKETIO_HOST);
         // 设置监听端口
        config.setPort(SOCKETIO_PORT);
        // 协议升级超时时间（毫秒），默认10000。HTTP握手升级为ws协议超时时间
        config.setUpgradeTimeout(10000);
        // Ping消息间隔（毫秒），默认25000。客户端向服务器发送一条心跳消息间隔
         config.setPingInterval(60000);
         // Ping消息超时时间（毫秒），默认60000，这个时间间隔内没有接收到心跳消息就会发送超时事件
         config.setPingTimeout(180000);
        final SocketIOServer server = new SocketIOServer(config);
        /*
         * 添加连接监听事件，监听是否与客户端连接到服务器
         */
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                // 判断是否有客户端连接
                if (client != null) {
                    LogUtil.intoLog(this.getClass(),"连接成功__clientId_____" + client.getSessionId().toString());
                } else {
                    LogUtil.intoLog(this.getClass(),"没有人连接上_____");
                }
            }
        });
        server.start();
        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }

}
