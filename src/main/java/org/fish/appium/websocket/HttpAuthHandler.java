package org.fish.appium.websocket;

import lombok.Getter;
import lombok.Setter;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.websocket.handler.PayLoadClockIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class HttpAuthHandler extends TextWebSocketHandler {

    private PayLoadClockIn clockIn;

    @Autowired
    public HttpAuthHandler(PayLoadClockIn clockIn) {
        this.clockIn = clockIn;
    }

    /**
     * socket 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户连接成功，放入在线用户缓存
            WsSessionManager.add(token.toString(), session);
        } else {
            throw new RuntimeException("用户登录已经失效");
        }
    }

    /**
     * 接收消息事件
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object token = session.getAttributes().get("token");
        System.out.printf(String.format("Server接收到 User-%s 发送的: %s%n", token, payload));
        TextMessage msg = new TextMessage(String.format("%s - Server发送给 User-%s 消息: %s", LocalDateTime.now(), token, payload));
        System.out.println(msg.getPayload());
        String[] command = payload.split(" ");
        if (command[0].equals("/clock")) {
            try {
                String[] account = command[1].split("/");
                AccountEntity accountEntity = new AccountEntity();
                accountEntity.setName(account[0]);
                accountEntity.setUsername(account[1]);
                accountEntity.setPassword(account[2]);
                session.sendMessage(new TextMessage(
                        String.format(
                                "Clock In Start...%n%nName: %s%nUsername: %s%nPassword: %s%n",
                                accountEntity.getName(),
                                accountEntity.getUsername(),
                                accountEntity.getPassword()
                        )
                ));
                TextMessage res = clockIn.clock(accountEntity);
                session.sendMessage(res);
                session.sendMessage(new TextMessage("Start..."));
                clockIn.login();
            } catch (Exception e) {
                session.sendMessage(new TextMessage(e.getMessage()));
                session.sendMessage(new TextMessage("Error: 发生错误 请联系管理员-1694599276"));
            }
        } else {
            session.sendMessage(msg);
        }
    }

    /**
     * socket 断开连接时
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @Nonnull CloseStatus status) {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(token.toString());
            System.out.printf(String.format("User-%s 断开连接%n", token));
        }
    }


}
