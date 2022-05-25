package org.fish.appium;

import java.util.Arrays;
import java.util.List;

public class SleepTest {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(60000, 120000, 180000, 240000);
        int index = (int) (Math.random() * list.size());
        System.out.println(list.get(index));
    }
}
