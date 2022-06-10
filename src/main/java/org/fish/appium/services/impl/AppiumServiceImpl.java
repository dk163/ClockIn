package org.fish.appium.services.impl;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.services.AppiumService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Service
public class AppiumServiceImpl implements AppiumService {
    private ConfigEntity config;

    @Autowired
    public void setConfigEntity(ConfigEntity config) {
        this.config = config;
    }

    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    private AndroidDriver<AndroidElement> driver;
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @Override
    public DesiredCapabilities getCapabilities() {
        capabilities.setCapability("udid", config.getUdid());
        capabilities.setCapability("deviceVersion", config.getDeviceVersion());
        capabilities.setCapability("deviceName", config.getDeviceName());
        capabilities.setCapability("platformName", config.getDevicePlatform());
        capabilities.setCapability("appPackage", config.getApplicationPackage());
        capabilities.setCapability("appActivity", config.getApplicationActivity());
        capabilities.setCapability("noReset", config.getNoReset());
        return capabilities;
    }

    @Override
    public AndroidDriver<AndroidElement> getAndroidDriver() throws MalformedURLException {
        if (driver != null) {
            driver = null;
        }
        logger.info("<==== " + getCapabilities().toString());
        driver = new AndroidDriver<>(new URL(config.getUrl()), getCapabilities());
        driver.manage().timeouts().implicitlyWait(config.getWait(), TimeUnit.SECONDS);
        return driver;
    }
}
