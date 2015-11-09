# Wechat

一个简单基本的微信公众号接口工具包
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

+ 基本用法:

	```java
	Wechat wechat = Wechat.newWechat("appId", "appSecret");
	wechat.{component}.{api};
	```	

+ ``Wechat``中包含几个基本组件:

	+ [基础API](#base-api): ```BASE```
	+ [用户API](#user-api): ```USER```
	+ [菜单API](#menu-api): ```MENU```
	+ [多客服API](#cs-api): ```CS```
	+ [消息API](#message-api): ```MESSAGE```
	+ [二维码API](#qr-api): ```QRCODE```
	+ [素材API](#material-api): ```MATERIAL```

+ API使用:
	
	+ <span id="base-api">**```BASE```**</span>: 
		
		```java
		/**
         * 构建授权跳转URL
         * @param redirectUrl 授权后的跳转URL(我方服务器URL)
         * @param quiet 是否静默: true: 仅获取openId，false: 获取openId和个人信息(需用户手动确认)
         * @return 微信授权跳转URL
         * @throws UnsupportedEncodingException
         */
		String authUrl(String redirectUrl, Boolean quiet)
		
		/**
         * 获取accessToken(失效为2小时，应该尽量临时保存一个地方，每隔一段时间来获取)
         * @return accessToken，或抛WechatException
         */
		String accessToken()
		
		/**
         * 获取用户openId
         * @param code 用户授权的code
         * @return 用户的openId，或抛WechatException
         */
		String openId(String code)
		
		/**
         * 获取微信服务器IP列表
         * @param accessToken accessToken
         * @return 微信服务器IP列表，或抛WechatException
         */
		List<String> ip(String accessToken);
		```
	
	+ <span id="user-api">**```USER```**</span>: 
		
		```java
		/**
         * 创建用户分组
         * @param accessToken accessToken
         * @param name 名称
         * @return 分组ID，或抛WechatException
         */
		Boolean createGroup(String accessToken, String name)
		
		/**
         * 获取所有分组列表
         * @param accessToken accessToken
         * @return 分组列表，或抛WechatException
         */
        List<Group> getGroup(String accessToken)
        
        /**
         * 删除分组
         * @param accessToken accessToken
         * @param id 分组ID
         * @return 删除成功返回true，或抛WechatException
         */
        Boolean deleteGroup(String accessToken, Integer id)
        
        /**
         * 更新分组名称
         * @param accessToken accessToken
         * @param id 分组ID
         * @param newName 分组新名称
         * @return 更新成功返回true，或抛WechatException
         */
        Boolean updateGroup(String accessToken, Integer id, String newName)
        
        /**
         * 获取用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 组ID，或抛WechatException
         */
        Integer getUserGroup(String accessToken, String openId)
        
         /**
         * 移动用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param groupId 组ID
         * @return 移动成功返回true，或抛WechatException
         */
        Boolean moveUserGroup(String accessToken, String openId, Integer groupId)
        
        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        User getUserInfo(String accessToken, String openId)
        
        /**
         * 备注用户
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param remark 备注
         * @return 备注成功返回true，或抛WechatException
         */
        Boolean remarkUser(String accessToken, String openId, String remark)
		```
	
	+ <span id="menu-api">**```MENU```**</span>: 
		
		```java
		/**
         * 查询菜单
         * @param accessToken accessToken
         * @return 菜单列表
         */
        List<Menu> get(String accessToken)
        
        /**
         * 创建菜单
         * @param accessToken 访问token
         * @param jsonMenu 菜单json
         * @return 创建成功返回true，或抛WechatException
         */
        Boolean create(String accessToken, String jsonMenu)
        
        /**
         * 删除菜单
         * @param accessToken accessToken
         * @return 删除成功返回true，或抛WechatException
         */
        Boolean delete(String accessToken)
		```
	
	+ <span id="cs-api">**```CS```**</span>: 
		
		```java
		/**
         * 添加客服账户
         * @param accessToken accessToken
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @return 添加成功返回true，反之false
         */
        Boolean createAccount(String accessToken, String account, String nickName, String password)
        
        /**
         * 更新客服账户
         * @param accessToken accessToken
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @return 添加成功返回true，或抛WechatException
         */
        Boolean updateAccount(String accessToken, String account, String nickName, String password)
        
        /**
         * 删除客服账户
         * @param accessToken accessToken
         * @param kfAccount 客服登录帐号(包含域名)
         * @return 添加成功返回true，或抛WechatException
         */
        Boolean deleteAccount(String accessToken, String kfAccount)
        
        /**
         * 构建转发客服的XML消息(指定一个在线的客服，若该客服不在线，消息将不再转发给其他在线客服)
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @return 转发客服的XML消息
         */
        String forward(String openId, String kfAccount)
        
        /**
         * 查询客服聊天记录
         * @param accessToken accessToken
         * @param pageNo 页码
         * @param pageSize 分页大小
         * @param startTime 起始时间
         * @param endTime 结束时间
         * @return 客服聊天记录，或抛WechatException
         */
        List<MsgRecord> getMsgRecords(String accessToken, Integer pageNo, Integer pageSize, Date startTime, Date endTime)
        
        /**
         * 创建会话(该客服必需在线)
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @return 创建成功返回true，或抛WechatException
         */
        Boolean createSession(String accessToken, String openId, String kfAccount, String text)
        
        /**
         * 关闭会话
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @return 关闭成功返回true，或抛WechatException
         */
        Boolean closeSession(String accessToken, String openId, String kfAccount, String text)
        
        /**
         * 获取用户的会话状态
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 客户的会话状态，或抛WechatException
         */
       UserSession getUserSession(String accessToken, String openId)
       
       /**
         * 获取客服的会话列表
         * @param accessToken accessToken
         * @param kfAccount 客服帐号(包含域名)
         * @return 客服的会话列表，或抛WechatException
         */
        List<CsSession> getCsSessions(String accessToken, String kfAccount)
        
        /**
         * 获取未接入的会话列表
         * @param accessToken accessToken
         * @return 未接入的会话列表，或抛WechatException
         */
        List<WaitingSession> getWaitingSessions(String accessToken)
		```
	
	+ <span id="message-api">**```MESSAGE```**</span>: 
	
		```java
		/**
         * 被动回复微信服务器文本消息
         * @return XML文本消息
         */
        String respText(String openId, String content)
        
         /**
         * 被动回复微信服务器图片消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @return XML图片消息
         */
        String respImage(String openId, String mediaId)
        
        /**
         * 被动回复微信服务器语音消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @return XML语音消息
         */
        String respVoice(String openId, String mediaId)
        
        /**
         * 被动回复微信服务器视频消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @param title 标题
         * @param desc 描述
         * @return XML视频消息
         */
        String respVideo(String openId, String mediaId, String title, String desc)
        
        /**
         * 被动回复微信服务器音乐消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @param title 标题
         * @param desc 描述
         * @param url 音乐链接
         * @param hqUrl 高质量音乐链接，WIFI环境优先使用该链接播放音乐
         * @return XML音乐消息
         */
        String respMusic(String openId, String mediaId, String title, String desc, String url, String hqUrl)
        
        /**
         * 被动回复微信服务器图文消息
         * @param openId 用户openId
         * @param articles 图片消息对象列表，长度 <= 10
         * @return XML图文消息
         */
        String respNews(String openId, List<Article> articles)
        
        /**
         * 接收微信服务器发来的XML消息，将解析xml为RecvMessage的子类，
         * 应用可根据具体解析出的RecvMessage是何种消息(msg instanceof ...)，做对应的业务处理
         * @param xml xml字符串
         * @return RecvMessage消息类，或抛WechatException
         */
        RecvMessage receive(String xml)
        
        /**
         * 向用户发送模版消息
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param link 点击链接
         * @param fields 字段列表
         * @return 消息ID，或抛WechatException
         */
        Integer sendTemplate(String accessToken, String openId, String templateId, String link, List<TemplateField> fields)  
        
         /**
         * 群发消息:
         *  1. 分组群发:【订阅号与服务号认证后均可用】
         *  2. 按OpenId列表发: 订阅号不可用，服务号认证后可用
         * @see me.hao0.wechat.model.message.send.SendMessageScope
         * @param accessToken accessToken
         * @param msg 消息
         * @return 消息ID，或抛WechatException
         */
        Long send(String accessToken, SendMessage msg)    
        
        /**
         * 发送预览消息
         * @param accessToken accessToken
         * @param msg 预览消息
         * @return 发送成功返回true，或抛WechatException
         */
        Boolean previewSend(String accessToken, SendPreviewMessage msg)         
        
        /**
         * 删除群发消息: 订阅号与服务号认证后均可用:
             1、只有已经发送成功的消息才能删除
             2、删除消息是将消息的图文详情页失效，已经收到的用户，还是能在其本地看到消息卡片。
             3、删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
             4、如果多次群发发送的是一个图文消息，那么删除其中一次群发，就会删除掉这个图文消息也，导致所有群发都失效
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @return 删除成功，或抛WechatException
         */
        Boolean deleteSend(String accessToken, Long id)  
        
        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @return 群发消息状态，或抛WechatException
         */
        String getSend(String accessToken, Long id)   
            
		```
	
	+ <span id="qr-api">**```QRCODE```**</span>: 
		
		```java
		
		/**
         * 获取临时二维码
         * @param accessToken accessToken
         * @param sceneId 业务场景ID，32位非0整型
         * @param expire 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
         * @return 临时二维码链接，或抛WechatException
         */
        String getTempQrcode(String accessToken, String sceneId, Integer expire)
        
        /**
         * 获取永久二维码
         * @param accessToken accessToken
         * @param sceneId 业务场景ID，最大值为100000（目前参数只支持1--100000）
         * @return 永久二维码链接，或抛WechatException
         */
        String getPermQrcode(String accessToken, String sceneId)
		
		/**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param accessToken accessToken
         * @param longUrl 长链接
         * @return 短链接，或抛WechatException
         */
        String shortUrl(String accessToken, String longUrl)
		```
	
	+ <span id="material-api">**```MATERIAL```**</span>(待码): 

+ 具体例子，可见[测试用例](https://github.com/ihaolin/wechat/blob/master/src/test/java/me/hao0/wechat/WechatTests.java)。

+ 微信相关文档

	+ [公众号接口权限说明](http://mp.weixin.qq.com/wiki/8/71e1908fa08e67c6251ebdd78fd6b6b4.html)
	+ [接口频率限制说明](http://mp.weixin.qq.com/wiki/0/2e2239fa5f49388d5b5136ecc8e0e440.html)
	+ [接口返回码说明](http://mp.weixin.qq.com/wiki/17/fa4e1434e57290788bde25603fa2fcbd.html)	
	+ [报警排查指引](http://mp.weixin.qq.com/wiki/13/8348156d0e25c9e27b21462322d41149.html)

+ 你是好人

	+ 倘若你钱多人傻花不完，小弟乐意效劳😊，掏出你的微信神器做回好人吧:
		
		<img src="wechat.png" width="200">
	
	+ 倘若你还不够尽兴，继续掏出你的支付宝神器，疯狂扫吧:

		<img src="alipay.png" width="200">
        