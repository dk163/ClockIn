package org.fish.appium.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "element", ignoreUnknownFields = false)
public class ElementEntity {
    private String via;
    private String username;
    private String password;
    private String privacy;
    private String login;

    private String work;
    private String clock;
    private String state;
    private String close;

    private String isExist;
    private String mine;
    private String setting;
    private String logout;
    private String affirm;
}
