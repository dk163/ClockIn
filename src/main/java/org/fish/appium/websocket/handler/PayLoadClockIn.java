package org.fish.appium.websocket.handler;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.entity.ElementEntity;
import org.fish.appium.services.AppiumService;
import org.fish.appium.services.ClockInService;
import org.openqa.selenium.By;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Getter
@Setter
@Component
public class PayLoadClockIn {

    public AppiumService appiumService;

    private ClockInService clockInService;

    private ConfigEntity config;

    private ElementEntity element;

    @Autowired
    public PayLoadClockIn(AppiumService appiumService, ClockInService clockInService, ConfigEntity config, ElementEntity element) {
        this.appiumService = appiumService;
        this.clockInService = clockInService;
        this.config = config;
        this.element = element;
    }

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private AndroidDriver driver;

    private AccountEntity account;

    public void setDriver(AndroidDriver driver) {
        this.driver = driver;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public TextMessage clock(AccountEntity account) {
        try {
            AndroidDriver driver = appiumService.getAndroidDriver();
            setDriver(driver);
            setAccount(account);
            return new TextMessage("Setting...");
        } catch (Exception e) {
            clockInService.quit(driver);
            return new TextMessage(e.getMessage());
        }
    }

    public void login() throws InterruptedException {
        Thread.sleep(10000);
        if (!clockInService.byElementIsExist(driver, By.xpath(element.getVia()))) {
            try {
                driver.findElement(By.id(element.getUsername())).sendKeys(account.getUsername());
                driver.findElement(By.id(element.getPassword())).sendKeys(account.getPassword());
                driver.findElement(By.id(element.getPrivacy())).click();
                driver.findElement(By.id(element.getLogin())).click();
                clockInService.clock(driver);
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
                if (e.toString().startsWith("Unable to create a new remote session.")) {
                    driver.removeApp("io.appium.uiautomator2.server.test");
                }
                driver.terminateApp(config.getApplicationPackage());
                driver.launchApp();
                login();
            }
        } else {
            driver.findElement(By.xpath(element.getVia())).click();
            if (clockInService.byElementIsExist(driver, By.xpath("//*[contains(@text, '" + account.getName() + "')]"))) {
                try {
                    driver.navigate().back();
                    clockInService.clock(driver);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("<==== " + e.getMessage());
                    driver.terminateApp(config.getApplicationPackage());
                    driver.launchApp();
                    login();
                }
            } else {
                logger.info("Error: " + "Log out");
                driver.navigate().back();
                clockInService.logout(driver);
                login();
            }
        }
    }
}
