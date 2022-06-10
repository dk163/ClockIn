# ClockIn

使用闲置的手机 配合Appium 实现钉钉自动蓝牙打卡

## 启动方式

```yacas
java -jar ./ClockIn-Version.1.2.0.jar
```

## 通过接口修改配置文件

```yacas
# 查询配置
http://127.0.0.1:9090/config/get

# Response
{
    "version": "1.2.0",
    "code": 200,
    "messages": "Success!",
    "result": {
        "applicationActivity": "com.alibaba.android.rimet.biz.LaunchHomeActivity",
        "applicationPackage": "com.alibaba.android.rimet",
        "deviceName": "Android",
        "devicePlatform": "Android",
        "deviceVersion": "10",
        "noReset": true,
        "timeout": 20,
        "udid": "Z81QAEYPA725H",
        "url": "http://127.0.0.1:4723/wd/hub",
        "wait": 10,
        "account": [
            {
                "name": "Name",
                "password": "Password",
                "username": "Username"
            }
        ]
    }
}
```

```yacas
# 修改账号密码
http://127.0.0.1:9090/config/account?name=Fish&username=Fish&password=Fish

# Response
{
    "version": "1.2.0",
    "code": 200,
    "messages": "Success!",
    "result": {
        "applicationActivity": "com.alibaba.android.rimet.biz.LaunchHomeActivity",
        "applicationPackage": "com.alibaba.android.rimet",
        "deviceName": "Android",
        "devicePlatform": "Android",
        "deviceVersion": "10",
        "noReset": true,
        "timeout": 20,
        "udid": "Z81QAEYPA725H",
        "url": "http://127.0.0.1:4723/wd/hub",
        "wait": 10,
        "account": [
            {
                "name": "Fish",
                "password": "Fish",
                "username": "Fish"
            }
        ]
    }
}
```

```yacas
# 修改设备udid
http://127.0.0.1:9090/config/udid?udid=Z81QAEYPA725H

# 设备udid获取
adb devices

# Response
{
    "version": "1.2.0",
    "code": 200,
    "messages": "Success!",
    "result": {
        "applicationActivity": "com.alibaba.android.rimet.biz.LaunchHomeActivity",
        "applicationPackage": "com.alibaba.android.rimet",
        "deviceName": "Android",
        "devicePlatform": "Android",
        "deviceVersion": "10",
        "noReset": true,
        "timeout": 20,
        "udid": "Z81QAEYPA725H",
        "url": "http://127.0.0.1:4723/wd/hub",
        "wait": 10,
        "account": [
            {
                "name": "Fish",
                "password": "Fish",
                "username": "Fish"
            }
        ]
    }
}
```

```yacas
# 启动
http://127.0.0.1:9090/clock/in
```
