package org.fish.appium.controller;

import lombok.Getter;
import lombok.Setter;
import org.fish.appium.common.Result;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.scheduled.ClockInSchedule;
import org.fish.appium.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Getter
@Setter
@RestController
@RequestMapping("/config")
@CrossOrigin
public class ConfigController {
    private ConfigService configService;

    @Autowired
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public ClockInSchedule schedule;

    @Autowired
    public void setSchedule(ClockInSchedule schedule) {
        this.schedule = schedule;
    }

    @GetMapping(value = "/get")
    public Result getConfig() {
        ConfigEntity info = configService.configInfo();
        return Result.ok(info);
    }

    @GetMapping(value = "/account")
    public Result setAccount(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password
    ) {
        AccountEntity account = new AccountEntity();
        account.setName(name);
        account.setUsername(username);
        account.setPassword(password);
        ConfigEntity config = configService.setAccount(account);
        return Result.ok(config);
    }

    @GetMapping(value = "/udid")
    public Result setUdid(@RequestParam(name = "udid") String udid) {
        ConfigEntity config = configService.setUdid(udid);
        return Result.ok(config);
    }

    @GetMapping(value = "/cron")
    public Result setCron(@RequestParam(name = "cron") String cron) {
        ConfigEntity config = configService.setCron(cron);
        schedule.stop();
        schedule.start();
        return Result.ok(config);
    }
}
