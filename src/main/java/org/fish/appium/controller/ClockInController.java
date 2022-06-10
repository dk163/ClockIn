package org.fish.appium.controller;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.common.Result;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.services.AppiumService;
import org.fish.appium.services.ClockInService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Getter
@Setter
@RestController
@RequestMapping("/clock")
@CrossOrigin
public class ClockInController {
    private AppiumService appiumService;

    @Autowired
    public void setAppiumService(AppiumService appiumService) {
        this.appiumService = appiumService;
    }

    private ClockInService clockInService;

    @Autowired
    public void setClockInService(ClockInService clockInService) {
        this.clockInService = clockInService;
    }

    private ConfigEntity config;

    @Autowired
    public void setConfigEntity(ConfigEntity config) {
        this.config = config;
    }

    private AndroidDriver<AndroidElement> driver;
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
            clockInService.quit();
            return Result.ok("OK!");
        } catch (Exception e) {
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
                clockInService.quit();
                return Result.ok("OK!");
            } else {
                logger.error("====> " + "Username and Password can not be empty!");
                return Result.error("Username and Password can not be empty!");
            }
        } catch (Exception e) {
            logger.error("<==== " + e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 25 9 ? * MON-FRI")
    public void scheduledClockIn() {
        try {
            List<Integer> list = Arrays.asList(60000, 120000, 180000, 240000);
            int index = (int) (Math.random() * list.size());
            Thread.sleep(list.get(index));
            logger.info("====> " + "Scheduled start");
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
                    break;
                }
            }
            clockInService.quit();
        } catch (Exception e) {
            logger.error("====> " + e.getMessage());
        }
    }
}
