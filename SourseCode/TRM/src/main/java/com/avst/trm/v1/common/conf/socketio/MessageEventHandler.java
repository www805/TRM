package com.avst.trm.v1.common.conf.socketio;

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
 * MessageEventHandler消息处理类实现
 */
@Component
public class MessageEventHandler {
    public static SocketIOServer socketIoServer;
    public static List<SocketIOClient> clients = new ArrayList<SocketIOClient>();//用于保存所有客户端

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        this.socketIoServer = server;
    }

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String userssid = client.getHandshakeData().getSingleUrlParam("userssid");
        clients.add(client);
        System.out.println("客户端:" + client.getSessionId() + "_____已连接___"+userssid);
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        clients.remove(client);
        System.out.println("客户端:" + client.getSessionId() + "_____断开连接");
    }

    @OnEvent(value = "getmsg")
    public void onEvent(SocketIOClient client, AckRequest request, MessageUser  data) {
        System.out.println(client.getSessionId()+"______"+data.getUsername()+"发来消息：" + data.getText());

        //返回消息
        MessageUser messageUser=new MessageUser();
        messageUser.setText("已收到消息");
        messageUser.setUsername("后台");
        socketIoServer.getClient(client.getSessionId()).sendEvent("getback", messageUser);
    }

}

