package org.fish.appium.websocket.handler;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.common.AppiumUtils;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.entity.ElementEntity;
import org.fish.appium.services.AppiumService;
import org.openqa.selenium.By;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Getter
@Setter
@Component
public class PayLoadClockIn {

    public AppiumService appiumService;

    private ConfigEntity config;

    private ElementEntity element;

    @Autowired
    public PayLoadClockIn(AppiumService appiumService, ConfigEntity config, ElementEntity element) {
        this.appiumService = appiumService;
        this.config = config;
        this.element = element;
    }

    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    private AndroidDriver driver;

    private AccountEntity account;

    private BlockingQueue<String> DEVICE_STATUS_QUEUE;

    private WebSocketSession session;

    private void send(String s) throws IOException {
        session.sendMessage(new TextMessage(s));
    }

    public void clock(AccountEntity account, BlockingQueue<String> queue, WebSocketSession session) throws IOException {
        try {
            AndroidDriver driver = appiumService.getAndroidDriver();
            setDriver(driver);
            setAccount(account);
            setDEVICE_STATUS_QUEUE(queue);
            setSession(session);
            send("====> " + "Setting");
        } catch (Exception e) {
            quit();
            send("====> " + e.getMessage());
        }
    }

    public void login() throws InterruptedException, IOException {
        send("====> " + "Login");
        Thread.sleep(10000);
        if (!AppiumUtils.byElementIsExist(driver, By.xpath(element.getVia()))) {
            try {
                driver.findElement(By.id(element.getUsername())).sendKeys(account.getUsername());
                driver.findElement(By.id(element.getPassword())).sendKeys(account.getPassword());
                driver.findElement(By.id(element.getPrivacy())).click();
                driver.findElement(By.id(element.getLogin())).click();
                clock();
            } catch (Exception e) {
                send("====> " + e.getMessage());
                if (e.toString().startsWith("Unable to create a new remote session.")) {
                    driver.removeApp("io.appium.uiautomator2.server.test");
                }
                driver.terminateApp(config.getApplicationPackage());
                driver.activateApp(config.getApplicationPackage());
                login();
            }
        } else {
            send("====> " + "已有帐号登录 检查中...");
            driver.findElement(By.xpath(element.getVia())).click();
            if (AppiumUtils.byElementIsExist(driver, By.xpath("//*[contains(@text, '" + account.getName() + "')]"))) {
                try {
                    driver.navigate().back();
                    clock();
                } catch (Exception e) {
                    send("====> " + e.getMessage());
                    driver.terminateApp(config.getApplicationPackage());
                    driver.activateApp(config.getApplicationPackage());
                    login();
                }
            } else {
                send("====> " + "Log out");
                driver.navigate().back();
                logout();
                login();
            }
        }
    }

    public void logout() throws IOException {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        send("====> " + String.format("Width: %s | Height: %s", width, height));
        if (AppiumUtils.byElementIsExist(driver, By.xpath(element.getIsExist()))) {
            send("====> " + "Logout the application");
            try {
                driver.findElement(By.xpath(element.getMine())).click();
                Thread.sleep(2000);
                AppiumUtils.touch(driver, width, height);
                driver.findElement(By.xpath(element.getSetting())).click();
                Thread.sleep(2000);
                AppiumUtils.touch(driver, width, height);
                driver.findElement(By.xpath(element.getLogout())).click();
                driver.findElement(By.xpath(element.getAffirm())).click();
            } catch (Exception e) {
                send("<==== " + "Logout Error: " + e.getMessage());
                driver.terminateApp(config.getApplicationPackage());
                driver.activateApp(config.getApplicationPackage());
                logout();
            }
        }
    }

    public void clock() throws IOException {
        send("====> " + "Enter workbench");
        driver.findElement(By.xpath(element.getWork())).click();
        while (true) {
            send("====> " + "Enter clock in page");
            driver.findElement(By.xpath(element.getClock())).click();
            if (!AppiumUtils.byElementIsExist(driver, By.xpath(element.getState()))) {
                send("====> " + "Close clock in page");
                driver.findElement(By.xpath(element.getClose())).click();
            } else {
                send("====> " + "Has clock");
                driver.findElement(By.xpath(element.getClose())).click();
                break;
            }
        }
        quit();
    }

    public void quit() throws IOException {
        send("====> " + "Driver lock");
        driver.lockDevice();
        send("====> " + "Driver quit");
        driver.quit();
        send("====> " + "打卡操作完成");
    }
}
