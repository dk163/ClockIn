package org.fish.appium.common;

import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static java.time.Duration.ofMillis;

@Getter
@Setter
public class ClockInUtil {

    public static Boolean byElementIsExist(AndroidDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");//设置日期格式
        return df.format(new Date());
    }

    public static void touch(AndroidDriver driver, int width, int height) {
        Point pointer = new Point(width, height);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 0);
        sequence.addAction(finger.createPointerMove(ofMillis(2000), PointerInput.Origin.viewport(), pointer.x / 2, pointer.y / 2));
        sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        sequence.addAction(finger.createPointerMove(ofMillis(2000), PointerInput.Origin.viewport(), pointer.x / 2, pointer.y / 10));
        sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
    }

    public static String imageToBase64ByLocal(String imgFile) {
        byte[] data = null;
        try {
            InputStream in = Files.newInputStream(Paths.get(imgFile));
            data = new byte[in.available()];
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

}
