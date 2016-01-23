# Wechat API使用文档

+ Wechat已实现以下组件:

	+ <a href="#base-api">基础</a> 
	+ <a href="#user-api">用户</a>
	+ <a href="#menu-api">菜单</a>
	+ <a href="#cs-api">多客服</a>
	+ <a href="#message-api">消息</a>
	+ <a href="#qr-api">二维码</a>
	+ <a href="#material-api">素材</a>
	+ <a href="#jssdk-api">JS调用相关</a>
	+ <a href="#data-api">数据统计</a>

+ API介绍:
	
	+ <a id="base-api">**```base()```**</a>: 
		
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
	
	+ <a id="user-api">**```user()```**</a>: 
		
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
        Boolean mvUserGroup(String accessToken, String openId, Integer groupId)
        
        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        User getUser(String accessToken, String openId)
        
        /**
         * 备注用户
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param remark 备注
         * @return 备注成功返回true，或抛WechatException
         */
        Boolean remarkUser(String accessToken, String openId, String remark)
		```
	
	+ <a id="menu-api">**```menu()```**</a>: 
		
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
	
	+ <a id="cs-api">**```cs()```**</a>: 
		
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
	     * 上传客服头像(jpg/png等格式)
	       注意: 当kfAccount不存在时，微信服务器返回的json数据格式是错误的。
	     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
	     * @param image 文件
	     * @return 上传成功返回true，或抛WechatException
	     */
	    public Boolean uploadHead(String kfAccount, File image)
    
        /**
         * 删除客服账户
         * @param accessToken accessToken
         * @param kfAccount 客服登录帐号(包含域名)
         * @return 添加成功返回true，或抛WechatException
         */
        Boolean deleteAccount(String accessToken, String kfAccount)
                
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
	
	+ <a id="message-api">**```msg()```**</a>: 
	
		```java
		/**
	     * 被动回复微信服务器文本消息
	     * @param recv 微信发来的XML消息
	     * @param content 文本内容
	     * @return XML文本消息
	     */
	    String respText(RecvMessage recv, String content)
        
       /**
	     * 被动回复微信服务器图片消息
	     * @param recv 微信发来的XML消息
	     * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
	     * @return XML图片消息
	     */
	    String respImage(RecvMessage recv, String mediaId)
        
       /**
	     * 被动回复微信服务器语音消息
	     * @param recv 微信发来的XML消息
	     * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
	     * @return XML语音消息
	     */
	    String respVoice(RecvMessage recv, String mediaId)
        
       /**
	     * 被动回复微信服务器视频消息
	     * @param recv 微信发来的XML消息
	     * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
	     * @param title 标题
	     * @param desc 描述
	     * @return XML视频消息
	     */
	    String respVideo(RecvMessage recv, String mediaId, String title, String desc)
        
       /**
	     * 被动回复微信服务器音乐消息
	     * @param recv 微信发来的XML消息
	     * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
	     * @param title 标题
	     * @param desc 描述
	     * @param url 音乐链接
	     * @param hqUrl 高质量音乐链接，WIFI环境优先使用该链接播放音乐
	     * @return XML音乐消息
	     */
    String respMusic(RecvMessage recv, String mediaId,
                            String title, String desc, String url, String hqUrl){
        
       /**
	     * 被动回复微信服务器图文消息
	     * @param recv 微信发来的XML消息
	     * @param articles 图片消息对象列表，长度小于10
	     * @return XML图文消息
	     */
	    String respNews(RecvMessage recv, List<Article> articles)
        
       /**
	     * 构建转发客服的XML消息(指定一个在线的客服，若该客服不在线，消息将不再转发给其他在线客服)
	     * @param recv 微信发来的XML消息
	     * @param kfAccount 客服帐号(包含域名)
	     * @return 转发客服的XML消息
	     */
	    String forward(RecvMessage recv, String kfAccount)

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
	
	+ <a id="qr-api">**```qr()```**</a>: 
		
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
	
	+ <a id="material-api">**```material()```**</a>:

		```java
		/**
         * 获取素材总数统计
         * @param accessToken accessToken
         * @return 素材总数统计，或抛WechatException
         */
        MaterialCount count(String accessToken)
        
        /**
         * 获取素材列表
         * @param accessToken accessToken
         * @param type 素材类型
         * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
         * @param count 返回素材的数量，取值在1到20之间
         * @param <T> Material范型
         * @return 素材分页对象，或抛WechatException
         */
        <T> Page<T> gets(String accessToken, MaterialType type, Integer offset, Integer count)
        
        /**
         * 删除永久素材
         * @param accessToken accessToken
         * @param mediaId 永久素材mediaId
         * @return 删除成功返回true，或抛WechatException
         */
        Boolean delete(String accessToken, String mediaId)
        
        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param accessToken accessToken
         * @param type 文件类型
         * @param input 输入流
         * @return TempMaterial对象，或抛WechatException
         */
        TempMaterial uploadTemp(String accessToken, MaterialUploadType type, String fileName, InputStream input)
        
        /**
         * 下载临时素材
         * @param accessToken accessToken
         * @param mediaId mediaId
         * @return 文件二进制数据
         */
        byte[] downloadTemp(String accessToken, String mediaId)
        
        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param accessToken accessToken
         * @param items 图文素材列表
         * @return mediaId
         */
        String uploadPermNews(String accessToken, List<NewsContentItem> items)
        
        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param accessToken accessToken
         * @param mediaId 图文mediaId
         * @param itemIndex 对应图文素材中的第几个图文项，从0开始
         * @param newItem 新的图文项
         * @return 更新成功返回true，反之false
         */
        Boolean updatePermNews(String accessToken, String mediaId, Integer itemIndex, NewsContentItem newItem)
        
        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param in 文件输入流
         * @return 微信内部图片链接
         */
        String uploadPermNewsImage(String accessToken, String fileName, InputStream in)
        
        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param accessToken accessToken
         * @param type 文件类型
         * @param file 文件
         * @return PermMaterial对象，或抛WechatException
         */
        PermMaterial uploadPerm(String accessToken, MaterialUploadType type, File file)
        
        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param input 输入流
         * @param title 标题
         * @param desc 描述
         * @return PermMaterial对象，或抛WechatException
         */
        PermMaterial uploadPermVideo(String accessToken, String fileName, InputStream input, String title, String desc)
		``` 
	+ <a id="jssdk-api">**```js()```**</a>:

		```java
		/**
         * 获取临时凭证
         * @param accessToken accessToken
         * @param type 凭证类型
         *             @see me.hao0.wechat.model.js.TicketType
         * @return Ticket对象，或抛WechatException
         */
        Ticket getTicket(String accessToken, TicketType type)
        
        /**
         * 获取JSSDK调用前的配置信息
         * @param jsApiTicket jsapi凭证
         * @param nonStr 随机字符串
         * @param timestamp 时间戳(s)
         * @param url 调用JSSDK的页面URL全路径(去除#后的)
         * @return Config对象
         */
        Config getConfig(String jsApiTicket, String nonStr, Long timestamp, String url)
		```

	+ <a id="data-api">**```data()```**</a>:

		```java
		/**
	     * 查询用户增量数据(最多跨度7天，endDate - startDate < 7)
	     * @param accessToken accessToken
	     * @param startDate 起始日期
	     * @param endDate 结束日期
	     * @return 用户增量统计
	     */
	    List<UserSummary> userSummary(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 查询用户总量数据(最多跨度7天，endDate - startDate < 7)
	     * @param accessToken accessToken
	     * @param startDate 起始日期
	     * @param endDate 结束日期
	     * @return 用户增量统计
	     */
	    List<UserCumulate> userCumulate(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取图文群发每日数据:
	     *  某天所有被阅读过的文章（仅包括群发的文章）在当天的阅读次数等数据
	     * @param accessToken accessToken
	     * @param date 日期
	     * @return 图文群发每日数据
	     */
	    List<ArticleDailySummary> articleSummaryDaily(String accessToken, String date)
	    
	    /**
	     * 获取图文群发总数据
	     * @param accessToken accessToken
	     * @param date 日期
	     * @return 图文群发总数据
	     */
	    List<ArticleTotal> articleTotal(String accessToken, String date)
	    
	    /**
	     * 获取图文统计数据(最多跨度3天，endDate - startDate < 3)
	     * @param accessToken accessToken
	     * @param startDate 起始日期
	     * @param endDate 结束日期
	     * @return 图文统计数据
	     */
	    List<ArticleSummary> articleSummary(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取图文统计数据
	     * @param accessToken accessToken
	     * @param date 日期
	     * @return 图文统计分时数据
	     */
	    List<ArticleSummaryHour> articleSummaryHourly(String accessToken, String date)
	    
	    /**
	     * 获取图文分享转发数据(最多跨度7天，endDate - startDate < 7)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 图文分享转发数据
	     */
	    List<ArticleShare> articleShare(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取图文分享转发分时数据
	     * @param accessToken accessToken
	     * @param date 日期
	     * @return 图文分享转发分时数据
	     */
	    List<ArticleShareHour> articleShareByHourly(String accessToken, String date)
	    
	    /**
	     * 获取接口分析数据(最多跨度30天，endDate - startDate < 30)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 接口分析数据
	     */
	    List<InterfaceSummary> interfaceSummary(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取接口分析分时数据
	     * @param accessToken accessToken
	     * @param date 日期
	     * @return 接口分析分时数据
	     */
	    List<InterfaceSummaryHour> interfaceSummaryHourly(String accessToken, String date)
	    
	    /**
	     * 获取消息分析数据(最多跨度30天，endDate - startDate < 30)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 消息分析数据
	     */
	    List<MsgSendSummary> msgSendSummary(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取消息分析分时数据
	     * @param accessToken accessToken
	     * @param date 日期
	     * @return 消息分析分时数据
	     */
	    List<MsgSendSummaryHour> msgSendSummaryHourly(String accessToken, String date)
	    
	    /**
	     * 获取消息分析周数据(最多跨度30天，endDate - startDate < 30)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 消息分析数据
	     */
	    List<MsgSendSummary> msgSendSummaryWeekly(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取消息分析月数据(最多跨度30天，endDate - startDate < 30)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 消息分析数据
	     */
	    List<MsgSendSummary> msgSendSummaryMonthly(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取消息发送分布数据(最多跨度15天，endDate - startDate < 15)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 发送消息分布周数据
	     */
	    List<MsgSendDist> msgSendDist(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取消息发送分布周数据(最多跨度30天，endDate - startDate < 30)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 发送消息分布周数据
	     */
	    List<MsgSendDist> msgSendDistWeekly(String accessToken, String startDate, String endDate)
	    
	    /**
	     * 获取消息发送分布月数据(最多跨度30天，endDate - startDate < 30)
	     * @param accessToken accessToken
	     * @param startDate 开始日期
	     * @param endDate 结束日期
	     * @return 发送消息分布月数据
	     */
	    List<MsgSendDist> msgSendDistMonthly(String accessToken, String startDate, String endDate)
		```
		        