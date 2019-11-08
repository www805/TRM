package com.avst.trm.v1.common.conf.socketio;

import com.avst.trm.v1.common.util.log.LogUtil;
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
        try {
            String SOCKETIO_HOST= PropertiesListenerConfig.getProperty("socketio.server.host");
            String SOCKETIO_PORT= PropertiesListenerConfig.getProperty("socketio.server.port");
            if (null!=SOCKETIO_HOST&&null!=SOCKETIO_PORT){
                Configuration config = new  Configuration();
                config.setHostname(SOCKETIO_HOST);
                config.setPort(Integer.valueOf(SOCKETIO_PORT));
                config.setUpgradeTimeout(10000);
                config.setPingInterval(60000);
                config.setPingTimeout(180000);

                config.setAuthorizationListener(new AuthorizationListener() {
                    @Override
                    public boolean isAuthorized(HandshakeData data) {
                        // String token = data.getSingleUrlParam("token");
                        // String username = JWTUtil.getSocketUsername(token);
                        // return JWTUtil.verifySocket(token, "secret");
                        return true;
                    }
                });

                final SocketIOServer socketIoServer =new SocketIOServer(config);

                //监听连接时
                socketIoServer.addConnectListener(new ConnectListener() {
                    @Override
                    public void onConnect(SocketIOClient socketIOClient) {
                        // 判断是否有客户端连接
                        try {
                            if (socketIOClient != null) {
                                boolean bool=true;
                                if(clients.size() > 0){
                                    for(SocketIOClient c:clients){

                                        if(c.getSessionId().toString().equals(socketIOClient.getSessionId().toString())){
                                            c= socketIOClient;
                                            bool=false;
                                            LogUtil.intoLog(this.getClass(),"更新客户端_____" + socketIOClient.getSessionId().toString() + "_____已连接__"+clients.size());
                                        }
                                    }
                                }

                                if(true){
                                    clients.add(socketIOClient);
                                    LogUtil.intoLog(this.getClass(),"添加新客户端_____" + socketIOClient.getSessionId().toString() + "_____已连接__"+clients.size());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                //监听断开时
                socketIoServer.addDisconnectListener(new DisconnectListener() {
                    @Override
                    public void onDisconnect(SocketIOClient socketIOClient) {
                        if (socketIOClient != null) {
                            try {
                                if(clients.size() > 0){
                                    for(int i=0;i<clients.size();i++){
                                        SocketIOClient c=clients.get(i);
                                        if(c.getSessionId().toString().equals(socketIOClient.getSessionId().toString())){
                                            LogUtil.intoLog(3,this.getClass(),"客户端_____" + socketIOClient.getSessionId().toString() + "_____断开连接__"+clients.size());
                                            clients.remove(i);
                                            i--;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                socketIoServer.start();
            }else {
                LogUtil.intoLog(4,this.getClass(),"socketiode IP和端口为空_____");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.intoLog(4,this.getClass(),"socketio连接异常___，"+e.getMessage());
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        LogUtil.intoLog(1,this.getClass(),"socketiode 开始启动_____");
        StartSocketio();
    }
}
