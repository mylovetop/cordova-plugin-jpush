## 简介

[极光推送](www.jpush.cn) 的 PhoneGap（cordova）>=3.0 插件

感谢 [@lincolnlk](https://github.com/lincolnlk) 的协助

## PhoneGap（cordova）安装

PhoneGap（cordova）是需要 nodejs 环境的。

所以需要先安装 [nodejs](http://nodejs.org/)

安装 nodejs 后，在 cmd 或者 terminal 中
输入 `npm install -g cordova` 安装 cordova

然后 在任意目录下

	cordova create hello com.example.hello "helloWorld"

hello - 项目目录， com.example.hello 包名，"helloWorld" - 应用名字

	cd hello
	cordova platform add android	

PhoneGap（cordova）的基本结构就建立完成了，然后用 IDE 打开 platform/android
该怎么调试怎么调试

## 本插件的安装 （暂时只有 Android iOS 没写）

定位到 cordova 的项目目录下，比如这里的 hello

`cordova plugin add https://github.com/morlay/cordova-plugin-jpush`

并且在 `platform/android/AndroidManifest.xml` 中，将如下地方修改下

`<meta-data android:name="JPUSH_APPKEY" android:value="修改这里替换为key" />`


## 本插件的卸载

定位到 cordova 的项目目录下，比如这里的 hello

`cordova plugin rm com.jpush.jpush.JPush` 即可。

## 本插件使用

在 PhoneGap 的入口文件 js 中 放到 对应函数里即可。

通过 `window.plugins.JPush` 调用

    document.addEventListener('deviceready',function(){

           window.plugins.JPush.init(true);

    }, false);


更多接口参看: www/jPush.js

## example 的使用

复制 example 中 www 覆盖 项目目录(hello)/www

`cordova prepare`