package org.fish.appium.services;

import io.appium.java_client.android.AndroidDriver;
import org.fish.appium.entity.AccountEntity;

public interface ClockInService {
    void setDriver(AndroidDriver driver);
    void quit();
    void setAccount(AccountEntity account);
    void login() throws InterruptedException;
    void clock();
    void logout();
}
