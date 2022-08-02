package org.fish.appium.controller;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.common.Result;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.services.AppiumService;
import org.fish.appium.services.ClockInService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Getter
@Setter
@RestController
@RequestMapping("/clock")
@CrossOrigin
public class ClockInController {
    private AppiumService appiumService;

    private ClockInService clockInService;

    private ConfigEntity config;

    @Autowired
    public ClockInController(AppiumService appiumService, ClockInService clockInService, ConfigEntity config) {
        this.appiumService = appiumService;
        this.clockInService = clockInService;
        this.config = config;
    }

    private AndroidDriver driver;
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/in", method = GET)
    public Result clockIn() {
        try {
            logger.info("====> " + "Launch the application");
            driver = appiumService.getAndroidDriver();
            for (AccountEntity stringStringMap : config.getAccount()) {
                if (stringStringMap.getUsername() != null && !"".equals(stringStringMap.getUsername()) && stringStringMap.getPassword() != null && !"".equals(stringStringMap.getPassword())) {
                    logger.info("====> " + "Login account " + stringStringMap);
                    clockInService.setAccount(stringStringMap);
                    clockInService.setDriver(driver);
                    clockInService.login();
                } else {
                    logger.error("====> " + "Username and Password can not be empty!");
                    return Result.error("Username and Password can not be empty!");
                }
            }
            return Result.ok("OK!");
        } catch (Exception e) {
            clockInService.quit();
            logger.error("<==== " + e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/in", method = POST)
    public Result clockInAction(@RequestBody AccountEntity account) {
        try {
            if (account.getUsername() != null && !"".equals(account.getUsername()) && account.getPassword() != null && !"".equals(account.getPassword())) {
                logger.info("====> " + "Launch the application");
                driver = appiumService.getAndroidDriver();
                logger.info("====> " + "Login account " + account);
                clockInService.setAccount(account);
                clockInService.setDriver(driver);
                clockInService.login();
                return Result.ok("OK!");
            } else {
                logger.error("====> " + "Username and Password can not be empty!");
                return Result.error("Username and Password can not be empty!");
            }
        } catch (Exception e) {
            clockInService.quit();
            logger.error("<==== " + e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
