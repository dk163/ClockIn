package org.fish.appium.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class MyInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, @Nonnull ServerHttpResponse response, @Nonnull WebSocketHandler wsHandler, @Nonnull Map<String, Object> attributes) {
        System.out.println("开始建立连接...");
        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), StandardCharsets.UTF_8);
        String uid = paramMap.get("token");
        if (StrUtil.isNotBlank(uid)) {
            // 放入属性域
            attributes.put("token", uid);
            System.out.printf("User-%s 连接成功%n", uid);
            return true;
        }
        System.out.println("用户登录已失效");
        return false;
    }

    /**
     * 握手后
     */
    @Override
    public void afterHandshake(@Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response, @Nonnull WebSocketHandler wsHandler, Exception exception) {
        System.out.println("连接已建立");
    }

}
