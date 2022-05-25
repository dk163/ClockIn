package org.fish.appium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySources({
        @PropertySource(value = "application.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "config.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "element.properties", ignoreResourceNotFound = true)
})
@ComponentScan("org.fish")
@EnableScheduling
@EnableAsync
public class AppiumDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppiumDemoApplication.class, args);
    }

}
