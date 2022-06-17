# ClockIn

手机配合Appium 实现钉钉自动蓝牙打卡

## 安装Appium Server 此处使用管理员运行Powershell

```shell
# 安装Choco
Set-ExecutionPolicy Bypass -Scope Process -Force; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))

# 安装Nvm
choco install nvm –version 1.1.9

# 安装Node
nvm install v16.14.2

# 检查Node是否安装
node --version

# 更新npm
npm install npm@latest -g

# 安装Appium Server
npm install -g appium
```

## 配置ANDROID_HOME

```shell
将Android SDK的目录放到环境变量
```

## 此处使用管理员运行Powershell

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
