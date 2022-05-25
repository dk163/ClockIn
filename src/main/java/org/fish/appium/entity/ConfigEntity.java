package org.fish.appium.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Data
@Configuration
@ConfigurationProperties(prefix = "appium.config", ignoreUnknownFields = false)
public class ConfigEntity {
    private String udid;
    private String deviceName;
    private String deviceVersion;
    private String devicePlatform;
    private String applicationPackage;
    private String applicationActivity;
    private Boolean noReset;
    private String url;
    private Long wait;
    private Long timeout;
}
