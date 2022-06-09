package org.fish.appium.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Data
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
}
