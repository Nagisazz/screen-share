package com.nagisazz.screenshare.common.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SocketHandler {

    /**
     * socketIOServer
     */
    @Autowired
    private SocketIOServer socketIOServer;
    /**
     * 房间内人员SessionID
     */
    private Map<String, List<UUID>> clientMap = new ConcurrentHashMap<>(16);

    /**
     * 当客户端发起连接时调用
     */
    @OnConnect
    public void onConnect(SocketIOClient socketIOClient) {
        log.info("【connect】通知, SocketSessionId: {}, SocketRemoteAddress: {}",
                socketIOClient.getSessionId().toString(), socketIOClient.getRemoteAddress().toString());
    }

    /**
     * 客户端断开连接时调用，刷新客户端信息
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient socketIOClient) {
        log.info("【disConnect】通知, SocketSessionId: {}, SocketRemoteAddress: {}",
                socketIOClient.getSessionId().toString(), socketIOClient.getRemoteAddress().toString());
        clientMap.forEach((key, value) -> {
            value.remove(socketIOClient.getSessionId());
        });
    }

    /**
     * sendMsg发送消息事件
     */
    @OnEvent("message")
    public void message(SocketIOClient socketIOClient, AckRequest ackRequest, Object content) {
        log.info("【message】通知, SocketSessionId: {}, SocketRemoteAddress: {}",
                socketIOClient.getSessionId().toString(), socketIOClient.getRemoteAddress().toString());
        // 发送socket通知：聚合消息，对象：房间内其他人
        // todo 获取房间信息
        socketIOServer.getAllClients().forEach(e -> {
            if (!e.getSessionId().equals(socketIOClient.getSessionId())) {
                // 发送用户加入消息
                e.sendEvent("message", content);
            }
        });
    }

    @OnEvent("create or join")
    public void create(SocketIOClient socketIOClient, AckRequest ackRequest, String content) {
        log.info("【create or join】通知,room: {}, SocketSessionId: {}, SocketRemoteAddress: {}",
                content, socketIOClient.getSessionId().toString(), socketIOClient.getRemoteAddress().toString());
        UUID sessionId = socketIOClient.getSessionId();
        if (clientMap.containsKey(content)) {
            // 发送socket通知：用户加入，对象：房间内其他人
            clientMap.get(content).forEach(e -> {
                // 发送用户加入消息
                socketIOServer.getClient(e).sendEvent("join", content);
            });

            // 加入房间
            clientMap.get(content).add(sessionId);

            // 发送socket通知：加入房间，对象：本人
            socketIOServer.getClient(sessionId).sendEvent("joined", content);
        } else {
            // 创建房间
            clientMap.put(content, Lists.newArrayList(socketIOClient.getSessionId()));

            // 发送socket通知：创建房间消息，对象：本人
            socketIOServer.getClient(sessionId).sendEvent("created", content);
        }
    }
}
