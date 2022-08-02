package org.fish.appium.scheduled;

import ch.qos.logback.classic.Logger;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import lombok.Setter;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.services.AppiumService;
import org.fish.appium.services.ClockInService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
@Configuration
@EnableScheduling
public class ClockInSchedule implements ScheduleObjectInterface {
    private ScheduledFuture<?> future;

    private TaskScheduler scheduler;

    private AppiumService appiumService;

    private ClockInService clockInService;

    private ConfigEntity config;

    @Autowired
    public ClockInSchedule(TaskScheduler scheduler, AppiumService appiumService, ClockInService clockInService, ConfigEntity config) {
        this.scheduler = scheduler;
        this.appiumService = appiumService;
        this.clockInService = clockInService;
        this.config = config;
    }

    private AndroidDriver driver;
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @Override
    public void start() {
        future = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Integer> list = Arrays.asList(60000, 120000, 180000, 240000);
                    int index = (int) (Math.random() * list.size());
                    Thread.sleep(list.get(index));
                    logger.info("====> " + "Scheduled start");
                    logger.info("====> " + "Launch the application");
                    driver = appiumService.getAndroidDriver();
                    for (AccountEntity stringStringMap : config.getAccount()) {
                        if (stringStringMap.getUsername() != null && !"".equals(stringStringMap.getUsername()) && stringStringMap.getPassword() != null && !"".equals(stringStringMap.getPassword())) {
                            logger.info("====> " + "Login account " + stringStringMap);
                            clockInService.setAccount(stringStringMap);
                            clockInService.setDriver(driver);
                            clockInService.login();
                        } else {
                            logger.error("====> " + "Username and Password can not be empty!");
                            break;
                        }
                    }
                } catch (Exception e) {
                    clockInService.quit();
                    logger.error("====> " + e.getMessage());
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(@Nonnull TriggerContext triggerContext) {
                String cron = config.getCron();
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        });
    }

    @Override
    public void stop() {
        future.cancel(false);
    }
}
