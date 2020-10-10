# sugar-maven-plugin
Sugar-maven-plugin 是为Maven复杂项目提供各类简化工具的插件。

**versionUpdate | 项目版本号管理**

`mvn sugar:versionUpdate -Dsugar.versionKey=t2 -Dsugar.versionValue=2.0.3 -Dsugar.dynamicVersion=true -Dsugar.versionType=RELEASE`

|参数名|说明|
|------|---|
|versionKey|需要替换的版本号key，比如 xxx.version|
|versionValue|新的版本号，不需要携带snapshot、release|
|dynamicVersion|默认false，true时自带时间戳MMddHHmm后缀|
|versionType|默认SNAPSHOT|
