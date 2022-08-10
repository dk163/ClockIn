//package org.fish.appium.scheduled;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//@Component
//@Order(value = 1)
//public class StartService implements ApplicationRunner {
//    public ClockInSchedule schedule;
//
//    @Autowired
//    public void setSchedule(ClockInSchedule schedule) {
//        this.schedule = schedule;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        schedule.start();
//    }
//}
