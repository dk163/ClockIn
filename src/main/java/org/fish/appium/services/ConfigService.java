package org.fish.appium.services;

import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;

public interface ConfigService {
    ConfigEntity configInfo();
    ConfigEntity setAccount(AccountEntity account);
    ConfigEntity setUdid(String udid);
}
