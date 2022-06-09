package org.fish.appium.services.impl;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.functions.ExpectedCondition;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.common.ConfigTool;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.entity.ElementEntity;
import org.fish.appium.services.ClockInService;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

@Getter
@Setter
@Service
public class ClockInServiceImpl implements ClockInService {
    @Resource
    private ElementEntity element;

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private AndroidDriver<AndroidElement> driver;
    private AccountEntity account;

    @Override
    public void setDriver(AndroidDriver<AndroidElement> driver) {
        this.driver = driver;
    }

    @Override
    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    @Override
    public void login() throws InterruptedException {
        String capabilities = driver.getCapabilities().toString();
        logger.info("<==== " + capabilities);
        Thread.sleep(10000);
        if (!byElementIsExist(driver, By.xpath(element.getVia()))) {
            try {
                logger.info("====> " + "Login the application");
                logger.info("====> " + "Input username " + account.getUsername());
                waitForElement(driver, By.id(element.getUsername())).sendKeys(account.getUsername());
                logger.info("====> " + "Input password " + account.getPassword());
                waitForElement(driver, By.id(element.getPassword())).sendKeys(account.getPassword());
                logger.info("====> " + "Check the agreement");
                waitForElement(driver, By.id(element.getPrivacy())).click();
                logger.info("====> " + "Click login");
                waitForElement(driver, By.id(element.getLogin())).click();
                clock();
            } catch (Exception e) {
                logger.error("<==== " + e.getMessage());
                if (e.toString().startsWith("Unable to create a new remote session.")) {
                    driver.removeApp("io.appium.uiautomator2.server.test");
                }
                logger.info("====> " + "Log back in");
                driver.closeApp();
                driver.launchApp();
                login();
            }
        } else {
            logger.info("====> " + "Examine name");
            waitForElement(driver, By.xpath(element.getVia())).click();
            if (byElementIsExist(driver, By.xpath("//*[contains(@text, '" + account.getName() + "')]"))) {
                try {
                    driver.navigate().back();
                    clock();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("<==== " + e.getMessage());
                    logger.info("====> " + "Log back in");
                    driver.closeApp();
                    driver.launchApp();
                    login();
                }
            } else {
                logger.info("====> " + "Log out");
                logout();
                login();
            }
        }
    }

    @Override
    public void clock() {
        logger.info("====> " + "Enter workbench");
        waitForElement(driver, By.xpath(element.getWork())).click();
        while (true) {
            logger.info("====> " + "Enter clock in page");
            waitForElement(driver, By.xpath(element.getClock())).click();
            if (!byElementIsExist(driver, By.xpath(element.getState()))) {
                logger.info("====> " + "Close clock in page");
                waitForElement(driver, By.xpath(element.getClose())).click();
            } else {
                logger.info("====> " + "Has clock");
                waitForElement(driver, By.xpath(element.getClose())).click();
                break;
            }
        }
    }

    @Override
    public void logout() {
        String capabilities = driver.getCapabilities().toString();
        int width = driver.manage().window().getSize().width;
        logger.info("====> Width: " + width);
        int height = driver.manage().window().getSize().height;
        logger.info("====> Height: " + height);
        logger.info("<==== " + capabilities);
        if (byElementIsExist(driver, By.xpath(element.getIsExist()))) {
            try {
                logger.info("====> " + "Logout the application");
                waitForElement(driver, By.xpath(element.getMine())).click();
                new TouchAction<>(driver).press(point(width / 2, height / 2)).waitAction(waitOptions(ofMillis(2000))).moveTo(point(width / 2, height / 10)).release().perform();
                waitForElement(driver, By.xpath(element.getSetting())).click();
                Thread.sleep(2000);
                new TouchAction<>(driver).press(point(width / 2, height / 2)).waitAction(waitOptions(ofMillis(2000))).moveTo(point(width / 2, height / 10)).release().perform();
                waitForElement(driver, By.xpath(element.getLogout())).click();
                waitForElement(driver, By.xpath(element.getAffirm())).click();
            } catch (Exception e) {
                logger.error("<==== " + e.getMessage());
                driver.closeApp();
                driver.launchApp();
                logout();
            }
        }
    }

    @Override
    public AndroidElement waitForElement(AndroidDriver<AndroidElement> driver, By locator) {
        ConfigTool configTool = new ConfigTool();
        ConfigEntity config = configTool.loadConfig();
        WebDriverWait web = new WebDriverWait(driver, config.getTimeout());
        return web.until((ExpectedCondition<AndroidElement>) input -> driver.findElement(locator));
    }

    public Boolean byElementIsExist(AndroidDriver<AndroidElement> driver, By locator) {
        try {
            waitForElement(driver, locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void quit() {
        logger.info("====> " + "Driver lock");
        driver.lockDevice();
        logger.info("====> " + "Driver quit");
        driver.quit();
    }
}
