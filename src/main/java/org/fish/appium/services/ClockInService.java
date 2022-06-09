package org.fish.appium.services;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.fish.appium.entity.AccountEntity;
import org.openqa.selenium.By;

import java.util.Map;

public interface ClockInService {
    void setDriver(AndroidDriver<AndroidElement> driver);
    void quit();
    void setAccount(AccountEntity account);
    void login() throws InterruptedException;
    void clock();
    void logout();
    AndroidElement waitForElement(AndroidDriver<AndroidElement> driver, By locator);
}
