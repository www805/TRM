package com.avst.trm.v1.common.conf.socketio;

import com.avst.trm.v1.common.util.LogUtil;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * MessageEventHandler消息处理类实现:已经弃用
 */
/*@Component*/
public class MessageEventHandler {
    public static SocketIOServer socketIoServer;
    public static List<SocketIOClient> clients = new ArrayList<SocketIOClient>();//用于保存所有客户端

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        clients.add(client);
        LogUtil.intoLog(this.getClass(),"客户端_____" + client.getSessionId() + "_____已连接");
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        clients.remove(client);
        LogUtil.intoLog(this.getClass(),"客户端_____" + client.getSessionId() + "_____断开连接");
    }

}

