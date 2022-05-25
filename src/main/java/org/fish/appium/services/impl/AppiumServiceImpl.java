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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Service
public class AppiumServiceImpl implements AppiumService {
    private ConfigEntity configEntity;

    @Autowired
    public void setConfigEntity(ConfigEntity configEntity) {
        this.configEntity = configEntity;
    }

    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    private AndroidDriver<AndroidElement> driver;
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @Bean
    public void setup() {
        capabilities.setCapability("udid", configEntity.getUdid());
        capabilities.setCapability("deviceName", configEntity.getDeviceName());
        capabilities.setCapability("deviceName", configEntity.getDeviceVersion());
        capabilities.setCapability("platformName", configEntity.getDevicePlatform());
        capabilities.setCapability("appPackage", configEntity.getApplicationPackage());
        capabilities.setCapability("appActivity", configEntity.getApplicationActivity());
        capabilities.setCapability("noReset", configEntity.getNoReset());
    }

    @Override
    public AndroidDriver<AndroidElement> getAndroidDriver() throws MalformedURLException {
        if (driver != null) {
            driver = null;
        }
        logger.info("<==== " + getCapabilities().toString());
        driver = new AndroidDriver<>(new URL(configEntity.getUrl()), getCapabilities());
        driver.manage().timeouts().implicitlyWait(configEntity.getWait(), TimeUnit.SECONDS);
        return driver;
    }

    @Override
    public DesiredCapabilities getCapabilities() {
        return capabilities;
    }
}
