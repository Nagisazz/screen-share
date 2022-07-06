package com.nagisazz.screenshare.common.runner;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
public class SocketServer implements CommandLineRunner {

    /**
     * socketIOServer
     */
    private final SocketIOServer socketIOServer;

    @Autowired
    public SocketServer(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @Override
    public void run(String... args) {
        log.info("---------- NettySocket通知服务开始启动 ----------");
        socketIOServer.start();
        log.info("---------- NettySocket通知服务启动成功 ----------");
    }

}
