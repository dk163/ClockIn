package org.fish.appium.services.impl;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fish.appium.common.AppiumUtils;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.entity.ElementEntity;
import org.fish.appium.services.ClockInService;
import org.openqa.selenium.By;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@NoArgsConstructor
public class ClockInServiceImpl implements ClockInService {
    private ConfigEntity config;

    private ElementEntity element;

    @Autowired
    public ClockInServiceImpl(ConfigEntity config, ElementEntity element) {
        this.config = config;
        this.element = element;
    }

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private AndroidDriver driver;
    private AccountEntity account;

    @Override
    public void setDriver(AndroidDriver driver) {
        this.driver = driver;
    }

    @Override
    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Async("AsyncTaskExecutor")
    @Override
    public void login() throws InterruptedException {
        String capabilities = driver.getCapabilities().toString();
        logger.info("<==== " + capabilities);
        Thread.sleep(10000);
        if (!AppiumUtils.byElementIsExist(driver, By.xpath(element.getVia()))) {
            try {
                logger.info("====> " + "Login the application");
                logger.info("====> " + "Input username " + account.getUsername());
                driver.findElement(By.id(element.getUsername())).sendKeys(account.getUsername());
                logger.info("====> " + "Input password " + account.getPassword());
                driver.findElement(By.id(element.getPassword())).sendKeys(account.getPassword());
                logger.info("====> " + "Check the agreement");
                driver.findElement(By.id(element.getPrivacy())).click();
                logger.info("====> " + "Click login");
                driver.findElement(By.id(element.getLogin())).click();
                clock(driver);
            } catch (Exception e) {
                logger.error("<==== " + e.getMessage());
                if (e.toString().startsWith("Unable to create a new remote session.")) {
                    driver.removeApp("io.appium.uiautomator2.server.test");
                }
                logger.info("====> " + "Log back in");
                driver.terminateApp(config.getApplicationPackage());
                driver.activateApp(config.getApplicationPackage());
                login();
            }
        } else {
            logger.info("====> " + "Examine name");
            driver.findElement(By.xpath(element.getVia())).click();
            if (AppiumUtils.byElementIsExist(driver, By.xpath("//*[contains(@text, '" + account.getName() + "')]"))) {
                try {
                    driver.navigate().back();
                    clock(driver);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("<==== " + e.getMessage());
                    logger.info("====> " + "Log back in");
                    driver.terminateApp(config.getApplicationPackage());
                    driver.activateApp(config.getApplicationPackage());
                    login();
                }
            } else {
                logger.info("====> " + "Log out");
                driver.navigate().back();
                logout(driver);
                login();
            }
        }
    }

    @Override
    public void clock(AndroidDriver driver) {
        logger.info("====> " + "Enter workbench");
        driver.findElement(By.xpath(element.getWork())).click();
        while (true) {
            logger.info("====> " + "Enter clock in page");
            driver.findElement(By.xpath(element.getClock())).click();
            if (!AppiumUtils.byElementIsExist(driver, By.xpath(element.getState()))) {
                logger.info("====> " + "Close clock in page");
                driver.findElement(By.xpath(element.getClose())).click();
            } else {
                logger.info("====> " + "Has clock");
                driver.findElement(By.xpath(element.getClose())).click();
                break;
            }
        }
        quit(driver);
    }

    @Override
    public void logout(AndroidDriver driver) {
        String capabilities = driver.getCapabilities().toString();
        int width = driver.manage().window().getSize().width;
        logger.info("====> Width: " + width);
        int height = driver.manage().window().getSize().height;
        logger.info("====> Height: " + height);
        logger.info("<==== " + capabilities);
        if (AppiumUtils.byElementIsExist(driver, By.xpath(element.getIsExist()))) {
            try {
                logger.info("====> " + "Logout the application");
                driver.findElement(By.xpath(element.getMine())).click();
                Thread.sleep(2000);
                AppiumUtils.touch(driver, width, height);
                driver.findElement(By.xpath(element.getSetting())).click();
                Thread.sleep(2000);
                AppiumUtils.touch(driver, width, height);
                driver.findElement(By.xpath(element.getLogout())).click();
                driver.findElement(By.xpath(element.getAffirm())).click();
            } catch (Exception e) {
                logger.error("<==== " + e.getMessage());
                driver.terminateApp(config.getApplicationPackage());
                driver.activateApp(config.getApplicationPackage());
                logout(driver);
            }
        }
    }

    @Override
    public void quit(AndroidDriver driver) {
        logger.info("====> " + "Driver lock");
        driver.lockDevice();
        logger.info("====> " + "Driver quit");
        driver.quit();
    }

}
