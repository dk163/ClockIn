package org.fish.appium.services;

import io.appium.java_client.android.AndroidDriver;
import org.fish.appium.entity.AccountEntity;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface ClockInService {
    void setting(AndroidDriver driver, AccountEntity account, WebSocketSession session);
    void quit(AndroidDriver driver) throws IOException;
    void login() throws InterruptedException, IOException;
    void clock(AndroidDriver driver) throws IOException;
    void logout(AndroidDriver driver) throws IOException;
}
