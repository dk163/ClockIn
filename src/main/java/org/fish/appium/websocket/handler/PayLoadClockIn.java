package org.fish.appium.websocket.handler;

import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.services.AppiumService;
import org.fish.appium.services.ClockInService;
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
    public ClockInService clockInService;

    @Autowired
    public PayLoadClockIn(AppiumService appiumService, ClockInService clockInService) {
        this.appiumService = appiumService;
        this.clockInService = clockInService;
    }

    private BlockingQueue<String> DEVICE_STATUS_QUEUE;

    public void clock(AccountEntity account, BlockingQueue<String> queue, WebSocketSession session) throws IOException {
        try {
            AndroidDriver driver = appiumService.getAndroidDriver();
            setDEVICE_STATUS_QUEUE(queue);
            clockInService.setting(driver, account, session);
            session.sendMessage(new TextMessage("====> " + "Setting"));
        } catch (Exception e) {
            session.sendMessage(new TextMessage("====> " + e.getMessage()));
        }
    }

    public void login() throws IOException, InterruptedException {
        clockInService.login();
    }
}
