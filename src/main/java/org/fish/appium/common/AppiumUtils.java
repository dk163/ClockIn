package org.fish.appium.common;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.util.Collections;

import static java.time.Duration.ofMillis;

public class AppiumUtils {

    public static Boolean byElementIsExist(AndroidDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
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
}
