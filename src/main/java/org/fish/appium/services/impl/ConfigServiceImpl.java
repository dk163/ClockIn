package org.fish.appium.services.impl;

import org.fish.appium.common.ConfigTool;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.services.ConfigService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Override
    public ConfigEntity configInfo() {
        ConfigTool configs = new ConfigTool();
        return configs.loadConfig();
    }

    @Override
    public void setAccount(AccountEntity account) {
        ConfigTool configs = new ConfigTool();
        ConfigEntity config = configs.loadConfig();
        config.setAccount(Stream.of(account).collect(Collectors.toList()));
        configs.update(config);
    }

    @Override
    public void setUdid(String udid) {
        ConfigTool configs = new ConfigTool();
        ConfigEntity config = configs.loadConfig();
        config.setUdid(udid);
        configs.update(config);
    }
}
