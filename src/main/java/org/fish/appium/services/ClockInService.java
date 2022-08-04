package org.fish.appium.services;

import io.appium.java_client.android.AndroidDriver;
import org.fish.appium.entity.AccountEntity;
import org.openqa.selenium.By;

public interface ClockInService {
    void setDriver(AndroidDriver driver);
    void quit(AndroidDriver driver);
    void setAccount(AccountEntity account);
    void login() throws InterruptedException;
    void clock(AndroidDriver driver);
    void logout(AndroidDriver driver);
    Boolean byElementIsExist(AndroidDriver driver, By locator);
}
