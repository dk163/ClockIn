package org.fish.appium.services;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;

public interface AppiumService {
    DesiredCapabilities getCapabilities();
    AndroidDriver getAndroidDriver() throws MalformedURLException;
}
