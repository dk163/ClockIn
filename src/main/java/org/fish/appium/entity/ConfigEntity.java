package org.fish.appium.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Data
@Component
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
public class ConfigEntity {
    private String applicationActivity;
    private String applicationPackage;
    private String deviceName;
    private String devicePlatform;
    private String deviceVersion;
    private Boolean noReset;
    private Long timeout;
    private String udid;
    private String url;
    private Long wait;
    private List<AccountEntity> account;

    private String cron;
}
