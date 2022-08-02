package org.fish.appium;

import org.fish.appium.common.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "application.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "config.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "element.properties", ignoreResourceNotFound = true)
})
@ComponentScan("org.fish")
@EnableScheduling
@EnableAsync
@EnableWebSocket
public class ClockInApplication {

    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(ClockInApplication.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

}
