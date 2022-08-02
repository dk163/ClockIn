package org.fish.appium.config;

import lombok.Getter;
import lombok.Setter;
import org.fish.appium.websocket.HttpAuthHandler;
import org.fish.appium.websocket.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Getter
@Setter
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private HttpAuthHandler httpAuthHandler;
    private MyInterceptor myInterceptor;

    @Autowired
    public WebSocketConfig(HttpAuthHandler httpAuthHandler, MyInterceptor myInterceptor) {
        this.httpAuthHandler = httpAuthHandler;
        this.myInterceptor = myInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(httpAuthHandler, "websocket")
                .addInterceptors(myInterceptor)
                .setAllowedOrigins("*");
    }
}
