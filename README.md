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
	+ 二维码API: ```QRCODE```
	+ 素材API: ```MATERIAL```

+ 微信相关文档

	+ [公众号接口权限说明](http://mp.weixin.qq.com/wiki/8/71e1908fa08e67c6251ebdd78fd6b6b4.html)
	+ [接口频率限制说明](http://mp.weixin.qq.com/wiki/0/2e2239fa5f49388d5b5136ecc8e0e440.html)
	+ [接口返回码说明](http://mp.weixin.qq.com/wiki/17/fa4e1434e57290788bde25603fa2fcbd.html)	
	+ [报警排查指引](http://mp.weixin.qq.com/wiki/13/8348156d0e25c9e27b21462322d41149.html)
        