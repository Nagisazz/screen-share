package com.nagisazz.screenshare.common.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SocketConfig {

    @Value("${socketio.port}")
    private Integer port;

//    @Value("${socketio.workCount}")
//    private int workCount;
//
//    @Value("${socketio.allowCustomRequests}")
//    private boolean allowCustomRequests;
//
//    @Value("${socketio.upgradeTimeout}")
//    private int upgradeTimeout;
//
//    @Value("${socketio.pingTimeout}")
//    private int pingTimeout;
//
//    @Value("${socketio.pingInterval}")
//    private int pingInterval;
//
//    @Value("${socketio.maxFramePayloadLength}")
//    private int maxFramePayloadLength;
//
//    @Value("${socketio.maxHttpContentLength}")
//    private int maxHttpContentLength;

    /**
     * SocketIOServer配置
     *
     */
    @Bean("socketIOServer")
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        // 配置端口
        config.setPort(port);
        // 开启Socket端口复用
        com.corundumstudio.socketio.SocketConfig socketConfig = new com.corundumstudio.socketio.SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        // 连接数大小
        config.setBossThreads(1);
        config.setWorkerThreads(20);
        // 允许客户请求
        config.setAllowCustomRequests(true);
        // 协议升级超时时间(毫秒)，默认10秒，HTTP握手升级为ws协议超时时间
//        config.setUpgradeTimeout(upgradeTimeout);
        // Ping消息超时时间(毫秒)，默认60秒，这个时间间隔内没有接收到心跳消息就会发送超时事件
//        config.setPingTimeout(pingTimeout);
        // Ping消息间隔(毫秒)，默认25秒。客户端向服务器发送一条心跳消息间隔
//        config.setPingInterval(pingInterval);
        // 设置HTTP交互最大内容长度
//        config.setMaxHttpContentLength(maxHttpContentLength);
        // 设置最大每帧处理数据的长度，防止他人利用大数据来攻击服务器
//        config.setMaxFramePayloadLength(maxFramePayloadLength);
        return new SocketIOServer(config);
    }

    /**
     * 开启SocketIOServer注解支持
     *
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
