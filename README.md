# Wechat

一个简洁的微信基本接口工具包
---

+ 包引入	

+ 依赖包，注意引入项目时是否需要**exclude**:

	```xml
	<!-- slf4j-api -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.2</version>
    </dependency>
    <!-- jackson -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.4.2</version>
    </dependency>
	```

+ 基本使用:

	```java
	Wechat wechat = Wechat.newWechat("appId", "appSecret");
	wecaht.{component}.{api};
	```	

+ ``Wechat``中包含几个基本组件:

	+ 基础API: ```BASE```
	+ 用户API: ```USER```
	+ 菜单API: ```MENU```
	+ 多客服API: ```CS```
	+ 消息API: ```MESSAGE```
	+ 素材API: ```MATERIAL```
	
        