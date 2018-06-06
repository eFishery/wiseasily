[![RELEASE](https://jitpack.io/v/eFishery/wiseasily.svg)](https://jitpack.io/#eFishery/wiseasily)

WisEasily
========

Wise and Easily to use Android Wifi Modul Functional by Efishery, Inc.

For more information please see [the website][1].

For Sample [Download][2].

Download
--------

Grab via Maven:
1. Add the JitPack repository to your build file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
2. Add the dependency
```xml
<dependency>
  <groupId>com.github.eFishery</groupId>
  <artifactId>wiseasily</artifactId>
  <version>v0.4.4</version>
</dependency>
```
or Gradle:
1. Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2. Add the dependency
```groovy
dependencies {
    compile 'com.github.eFishery:wifi-wiseasily:v0.4.4'
}
```

Usage Sample
--------

Connect to wifi with ssid name : efishery_2018

```java
new WisEasily(this).connect("efishery_2018", new SourceCallback.WisEasilyCallback() {
    @Override
    public void onSuccess() {
        
    }

    @Override
    public void onError(String s) {

    }
});
```

Scan Wifi

```java

@Override
protected void onResume() {
    super.onResume();
    new WisEasily(this).scan(this);
}
@Override
public void onAPChanged(List<ScanResult> scanResults) {
    adapter.replaceData(scanResults);
}
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


 [1]: http://wiki.efishery.com/kb/5a4f47ea46c64f25ba36cee9
 [2]: https://drive.google.com/open?id=1LeuABggbQJqk5itoM6Gv1Q2zgqvm84Am
