package org.fish.appium.services.impl;

import org.fish.appium.common.SpringContextUtil;
import org.fish.appium.entity.AccountEntity;
import org.fish.appium.entity.ConfigEntity;
import org.fish.appium.services.ConfigService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Override
    public ConfigEntity configInfo() {
        Object configEntity = SpringContextUtil.getBean("configEntity");
        return (ConfigEntity) configEntity;
    }

    @Override
    public ConfigEntity setAccount(AccountEntity account) {
        List<AccountEntity> list = new ArrayList<>();
        list.add(account);
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        Class<?> beanType = applicationContext.getType("configEntity");
        assert beanType != null;
        Field[] declaredFields = beanType.getDeclaredFields();
        for (Field field : declaredFields) {
            Object beanObject = applicationContext.getBean("configEntity");
            try {
                if ("account".equals(field.getName())) {
                    SpringContextUtil.setFieldData(field, beanObject, list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (ConfigEntity)SpringContextUtil.getBean("configEntity");
    }

    @Override
    public ConfigEntity setUdid(String udid) {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        Class<?> beanType = applicationContext.getType("configEntity");
        assert beanType != null;
        Field[] declaredFields = beanType.getDeclaredFields();
        for (Field field : declaredFields) {
            Object beanObject = applicationContext.getBean("configEntity");
            try {
                if ("udid".equals(field.getName())) {
                    SpringContextUtil.setFieldData(field, beanObject, udid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (ConfigEntity)SpringContextUtil.getBean("configEntity");
    }

    @Override
    public ConfigEntity setCron(String cron) {
        ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
        Class<?> beanType = applicationContext.getType("configEntity");
        assert beanType != null;
        Field[] declaredFields = beanType.getDeclaredFields();
        for (Field field : declaredFields) {
            Object beanObject = applicationContext.getBean("configEntity");
            try {
                if ("cron".equals(field.getName())) {
                    SpringContextUtil.setFieldData(field, beanObject, cron);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (ConfigEntity)SpringContextUtil.getBean("configEntity");
    }
}
