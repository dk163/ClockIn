package org.fish.appium.services.impl;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.fish.appium.common.ClockInUtil;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.entity.ElementEntity;
import org.fish.appium.services.ClockInService;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;

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
    private WebSocketSession session;

    @Override
    public void setting(AndroidDriver driver, AccountEntity account, WebSocketSession session) {
        this.driver = driver;
        this.account = account;
        this.session = session;
    }

    public void send(String lev, String msg) throws IOException {
        if (session == null) {
            if (lev.equals("info")) {
                logger.info(msg);
            }
            if (lev.equals("error")) {
                logger.error(msg);
            }
        } else {
            session.sendMessage(new TextMessage(msg));
        }
    }

    @Async("AsyncTaskExecutor")
    @Override
    public void login() throws InterruptedException, IOException {
        Thread.sleep(10000);
        if (!ClockInUtil.byElementIsExist(driver, By.xpath(element.getVia()))) {
            try {
                send("info", "====> " + "Login the application");
                driver.findElement(By.id(element.getUsername())).sendKeys(account.getUsername());
                driver.findElement(By.id(element.getPassword())).sendKeys(account.getPassword());
                driver.findElement(By.id(element.getPrivacy())).click();
                driver.findElement(By.id(element.getLogin())).click();
                Thread.sleep(5000);
                if (ClockInUtil.byElementIsExist(driver, By.xpath("//*[@text=\"为确保帐号安全，需要再进行下一步验证\"]"))) {
                    driver.findElement(By.xpath("//*[@text=\"继 续\"]")).click();
                    driver.findElement(By.xpath("//*[@text=\"好\"]")).click();
                    File screenshot = driver.getScreenshotAs(OutputType.FILE);
                    try {
                        FileUtils.copyFile(screenshot, new File("D:\\AutoScreenCapture\\" + ClockInUtil.getCurrentDateTime() + ".jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    clock(driver);
                }
            } catch (Exception e) {
                send("error", "<==== " + e.getMessage());
                if (e.toString().startsWith("Unable to create a new remote session.")) {
                    driver.removeApp("io.appium.uiautomator2.server.test");
                }
                send("info", "====> " + "Log back in");
                driver.terminateApp(config.getApplicationPackage());
                driver.activateApp(config.getApplicationPackage());
                login();
            }
        } else {
            send("info", "====> " + "已有账号登录 账号检查...");
            driver.findElement(By.xpath(element.getVia())).click();
            if (ClockInUtil.byElementIsExist(driver, By.xpath("//*[contains(@text, '" + account.getName() + "')]"))) {
                try {
                    driver.navigate().back();
                    clock(driver);
                } catch (Exception e) {
                    e.printStackTrace();
                    send("error", "<==== " + e.getMessage());
                    send("info", "====> " + "Log back in");
                    driver.terminateApp(config.getApplicationPackage());
                    driver.activateApp(config.getApplicationPackage());
                    login();
                }
            } else {
                send("info", "====> " + "Log out");
                driver.navigate().back();
                logout(driver);
                login();
            }
        }
    }

    @Override
    public void verify(AndroidDriver driver) {
        if (ClockInUtil.byElementIsExist(driver, By.xpath("//*[contains(@text, '为确保账号安全')]"))) {
            System.out.println("=============================================");
        }
    }

    @Override
    public void clock(AndroidDriver driver) throws IOException {
        send("info", "====> " + "Enter workbench");
        driver.findElement(By.xpath(element.getWork())).click();
        while (true) {
            send("info", "====> " + "Enter clock in page");
            driver.findElement(By.xpath(element.getClock())).click();
            if (!ClockInUtil.byElementIsExist(driver, By.xpath(element.getState()))) {
                send("info", "====> " + "Close clock in page");
                driver.findElement(By.xpath(element.getClose())).click();
            } else {
                send("info", "====> " + "Has clock");
                driver.findElement(By.xpath(element.getClose())).click();
                break;
            }
        }
        quit(driver);
    }

    @Override
    public void logout(AndroidDriver driver) throws IOException {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        send("info", "====> " + String.format("Width: %s | Height: %s", width, height));
        if (ClockInUtil.byElementIsExist(driver, By.xpath(element.getIsExist()))) {
            try {
                send("info", "====> " + "Logout the application");
                driver.findElement(By.xpath(element.getMine())).click();
                Thread.sleep(2000);
                ClockInUtil.touch(driver, width, height);
                driver.findElement(By.xpath(element.getSetting())).click();
                Thread.sleep(2000);
                ClockInUtil.touch(driver, width, height);
                driver.findElement(By.xpath(element.getLogout())).click();
                driver.findElement(By.xpath(element.getAffirm())).click();
            } catch (Exception e) {
                send("error", "<==== " + e.getMessage());
                driver.terminateApp(config.getApplicationPackage());
                driver.activateApp(config.getApplicationPackage());
                logout(driver);
            }
        }
    }

    @Override
    public void quit(AndroidDriver driver) throws IOException {
        send("info", "====> " + "Driver lock");
        driver.lockDevice();
        send("info", "====> " + "Driver quit");
        send("info", "====> " + "完成 请执行 /ws 0 断开连接");
        driver.quit();
    }
}
