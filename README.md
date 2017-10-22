[![Release](https://jitpack.io/v/putrabangga/wiseasily.svg)](https://jitpack.io/#putrabangga/wiseasily)

WisEasily
========

Wise and Easily to use Android Wifi Modul Functional by Efishery, Inc.

For more information please see [the website][1].


Download
--------

Grab via Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependency>
  <groupId>com.github.putrabangga</groupId>
  <artifactId>wiseasily</artifactId>
  <version>v0.1-alpha</version>
</dependency>
```
or Gradle:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    compile 'com.github.putrabangga:wiseasily:v0.1-alpha'
}
```

Usage Sample
--------

Connect to wifi with ssid name : efishery_2017

```java
new WifiConnector("efishery_2017", context).connect(new ConnectorCallback.ConnectWifiCallback() {
       @Override
       public void onWifiConnected() {

       }

       @Override
       public void onProgress(String message) {

       }

       @Override
       public void onWifiFail(int errorCode) {

       }
   });
```


License
=======

    Copyright 2017 Efishery, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: http://wiki.efishery.com/kb/wiseasily/
