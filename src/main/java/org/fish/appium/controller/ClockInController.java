package org.fish.appium.controller;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.common.Result;
import org.fish.appium.services.AppiumService;
import org.fish.appium.services.ClockInService;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Getter
@Setter
@RestController
public class ClockInController {
    @Resource
    private AppiumService appiumService;
    @Resource
    private ClockInService clockInService;
    @Resource
    private AccountEntity accountTools;
    private AndroidDriver<AndroidElement> driver;
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/clock/in", method = GET)
    public Result clockIn() {
        try {
            logger.info("====> " + "Launch the application");
            driver = appiumService.getAndroidDriver();
            for (Map<String, String> stringStringMap : accountTools.getAccount()) {
                if (stringStringMap.get("username") != null && !stringStringMap.get("username").equals("") && stringStringMap.get("password") != null && !stringStringMap.get("password").equals("")) {
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

    @RequestMapping(value = "/clock/in", method = POST)
    public Result clockInAction(
            @RequestBody Map<String, String> account
    ) {
        try {
            if (account.get("username") != null && !account.get("username").equals("") && account.get("password") != null && !account.get("password").equals("")) {
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
            for (Map<String, String> stringStringMap : accountTools.getAccount()) {
                if (stringStringMap.get("username") != null && !stringStringMap.get("username").equals("") && stringStringMap.get("password") != null && !stringStringMap.get("password").equals("")) {
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
