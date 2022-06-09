package org.fish.appium.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AccountEntity {
    private String name;
    private String password;
    private String username;
}
