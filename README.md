# ClockIn

手机配合Appium 实现钉钉自动蓝牙打卡

## 安装Appium Server

```shell
npm install -g appium
```

## 配置ANDROID_HOME

```shell
将Android SDK的目录放到环境变量
```

## 使用Powershell启动

```shell
# 启动Appium
# 将 E:\Document\Test\Appium.log 修改为自定义log路径
Start-Job -name appium -ScriptBlock { appium -p 4723 >> E:\Document\Test\Appium.log }

# 启动ClockIn
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