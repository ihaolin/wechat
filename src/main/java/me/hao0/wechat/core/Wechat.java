package me.hao0.wechat.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.Page;
import me.hao0.wechat.model.base.AccessToken;
import me.hao0.wechat.model.js.Ticket;
import me.hao0.wechat.model.js.TicketType;
import me.hao0.wechat.model.customer.WaitingSession;
import me.hao0.wechat.model.js.Config;
import me.hao0.wechat.model.material.CommonMaterial;
import me.hao0.wechat.model.material.MaterialCount;
import me.hao0.wechat.model.material.MaterialType;
import me.hao0.wechat.model.material.NewsContentItem;
import me.hao0.wechat.model.material.PermMaterial;
import me.hao0.wechat.model.material.TempMaterial;
import me.hao0.wechat.model.material.MaterialUploadType;
import me.hao0.wechat.model.material.NewsMaterial;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.menu.MenuType;
import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.RecvMessageType;
import me.hao0.wechat.model.message.receive.event.RecvEvent;
import me.hao0.wechat.model.message.receive.event.RecvEventType;
import me.hao0.wechat.model.message.receive.event.RecvLocationEvent;
import me.hao0.wechat.model.message.receive.event.RecvMenuEvent;
import me.hao0.wechat.model.message.receive.event.RecvScanEvent;
import me.hao0.wechat.model.message.receive.event.RecvSubscribeEvent;
import me.hao0.wechat.model.message.receive.event.RecvUnSubscribeEvent;
import me.hao0.wechat.model.message.receive.msg.RecvImageMessage;
import me.hao0.wechat.model.message.receive.msg.RecvLinkMessage;
import me.hao0.wechat.model.message.receive.msg.RecvLocationMessage;
import me.hao0.wechat.model.message.receive.msg.RecvMsg;
import me.hao0.wechat.model.message.receive.msg.RecvShortVideoMessage;
import me.hao0.wechat.model.message.receive.msg.RecvTextMessage;
import me.hao0.wechat.model.message.receive.msg.RecvVideoMessage;
import me.hao0.wechat.model.message.receive.msg.RecvVoiceMessage;
import me.hao0.wechat.model.qrcode.Qrcode;
import me.hao0.wechat.model.qrcode.QrcodeType;
import me.hao0.wechat.model.user.Group;
import me.hao0.wechat.model.user.User;
import me.hao0.wechat.model.base.AuthType;
import me.hao0.wechat.model.customer.CsSession;
import me.hao0.wechat.model.customer.MsgRecord;
import me.hao0.wechat.model.customer.UserSession;
import me.hao0.wechat.model.message.resp.Article;
import me.hao0.wechat.model.message.send.SendMessage;
import me.hao0.wechat.model.message.send.SendMessageScope;
import me.hao0.wechat.model.message.send.SendMessageType;
import me.hao0.wechat.model.message.send.SendPreviewMessage;
import me.hao0.wechat.model.message.send.TemplateField;
import me.hao0.wechat.model.message.resp.RespMessageType;
import me.hao0.wechat.utils.Jsons;
import me.hao0.wechat.utils.XmlReaders;
import me.hao0.wechat.utils.XmlWriters;
import me.hao0.wechat.utils.Http;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public final class Wechat {

    /**
     * 微信APP ID
     */
    private String appId;

    /**
     * 微信APP 密钥
     */
    private String appSecret;

    /**
     * 微信APP (令牌)Token
     */
    String appToken;

    /**
     * 消息加密Key
     */
    String msgKey;

    /**
     * AccessToken加载器
     */
    AccessTokenLoader tokenLoader = DEFAULT_ACCESS_TOKEN_LOADER;

    /**
     * Ticket加载器
     */
    TicketLoader ticketLoader = DEFAULT_TICKET_LOADER;

    /**
     * 异步执行器
     */
    ExecutorService executor = DEFAULT_EXECUTOR;

    /**
     * 微信错误码变量
     */
    private final String ERROR_CODE = "errcode";

    private static final AccessTokenLoader DEFAULT_ACCESS_TOKEN_LOADER = new DefaultAccessTokenLoader();

    private static final DefaultTicketLoader DEFAULT_TICKET_LOADER = new DefaultTicketLoader();

    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("wechat");
                    return t;
                }
            });

    /**
     * 基础API
     */
    public final Bases BASE = new Bases();

    /**
     * 多客服API
     */
    public final CustomerServices CS = new CustomerServices();

    /**
     * 菜单API
     */
    public final Menus MENU = new Menus();

    /**
     * 用户API
     */
    public final Users USER = new Users();

    /**
     * 消息API
     */
    public final Messages MESSAGE = new Messages();

    /**
     * 二维码API
     */
    public final QrCodes QRCODE = new QrCodes();

    /**
     * 素材API
     */
    public final Materials MATERIAL = new Materials();

    /**
     * 数据统计API
     */
    public final Datas DATA = new Datas();

    /**
     * JSSDK API
     */
    public final JsSdks JSSDK = new JsSdks();

    Wechat(String appId, String appSecret){
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getAppToken() {
        return appToken;
    }

    public String getMsgKey() {
        return msgKey;
    }

    /**
     * 基础API类
     */
    public final class Bases {

        /**
         * 授权
         */
        private final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?";

        /**
         * 获取用户openId
         */
        private final String OPEN_ID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";

        /**
         * 获取accessToken
         */
        private final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

        /**
         * 获取微信服务器的IP地址列表
         */
        private final String WX_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";

        private Bases(){}

        /**
         * 构建授权跳转URL(静默授权，仅获取用户openId，不包括个人信息)
         * @param redirectUrl 授权后的跳转URL(我方服务器URL)
         * @return 微信授权跳转URL
         * @throws java.io.UnsupportedEncodingException
         */
        public String authUrl(String redirectUrl) throws UnsupportedEncodingException {
            return authUrl(redirectUrl, Boolean.TRUE);
        }

        /**
         * 构建授权跳转URL
         * @param redirectUrl 授权后的跳转URL(我方服务器URL)
         * @param quiet 是否静默: true: 仅获取openId，false: 获取openId和个人信息(需用户手动确认)
         * @return 微信授权跳转URL
         * @throws UnsupportedEncodingException
         */
        public String authUrl(String redirectUrl, Boolean quiet) throws UnsupportedEncodingException {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
            return AUTH_URL +
                    "appid=" + appId +
                    "&redirect_uri=" + redirectUrl +
                    "&response_type=code&scope=" +
                    (quiet ? AuthType.BASE.scope() : AuthType.USER_INFO.scope())
                    + "&state=1#wechat_redirect";
        }

        /**
         * 获取用户openId
         * @param code 用户授权的code
         * @param cb 回调
         * @return 用户的openId，或抛WechatException
         */
        public void openId(final String code, final Callback<String> cb){
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() {
                    return openId(code);
                }
            });
        }

        /**
         * 获取用户openId
         * @param code 用户授权的code
         * @return 用户的openId，或抛WechatException
         */
        public String openId(String code){
            String url = OPEN_ID_URL +
                    "appid=" + appId +
                    "&secret=" + appSecret +
                    "&code=" + code +
                    "&grant_type=authorization_code";

            Map<String, Object> resp = doGet(url);

            return (String)resp.get("openid");
        }

        /**
         * 获取accessToken(应该尽量临时保存一个地方，每隔一段时间来获取)
         * @param cb 回调
         */
        public void accessToken(final Callback<AccessToken> cb){
            doAsync(new AsyncFunction<AccessToken>(cb) {
                @Override
                public AccessToken execute() {
                    return accessToken();
                }
            });
        }

        /**
         * 获取accessToken(应该尽量临时保存一个地方，每隔一段时间来获取)
         * @return accessToken，或抛WechatException
         */
        public AccessToken accessToken(){
            String url = ACCESS_TOKEN_URL + "&appid=" + appId + "&secret=" + appSecret;

            Map<String, Object> resp = doGet(url);

            AccessToken token = new AccessToken();
            token.setAccessToken((String)resp.get("access_token"));
            Integer expire = (Integer)resp.get("expires_in");
            token.setExpire(expire);
            token.setExpiredAt(System.currentTimeMillis() + expire * 1000);
            return token;
        }

        /**
         * 获取微信服务器IP列表
         * @return 微信服务器IP列表，或抛WechatException
         */
        public List<String> ip(){
            return ip(loadAccessToken());
        }

        /**
         * 获取微信服务器IP列表
         * @param cb 回调
         */
        public void ip(Callback<List<String>> cb){
            ip(loadAccessToken(), cb);
        }

        /**
         * 获取微信服务器IP列表
         * @param accessToken accessToken
         *                  @see Wechat.Bases#accessToken()
         * @param cb 回调
         */
        public void ip(final String accessToken, Callback<List<String>> cb){
            doAsync(new AsyncFunction<List<String>>(cb) {
                @Override
                public List<String> execute() {
                    return ip(accessToken);
                }
            });
        }

        /**
         * 获取微信服务器IP列表
         * @param accessToken accessToken
         *                  @see Wechat.Bases#accessToken()
         * @return 微信服务器IP列表，或抛WechatException
         */
        @SuppressWarnings("unchecked")
        public List<String> ip(String accessToken){
            String url = WX_IP_URL + accessToken;

            Map<String, Object> resp = doGet(url);

            return (List<String>)resp.get("ip_list");
        }

    }

    /**
     * 多客服API
     */
    public final class CustomerServices {

        /**
         * 添加客服账号
         */
        private final String CREATE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=";

        /**
         * 更新客服账号
         */
        private final String UPDATE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=";

        /**
         * 删除客服帐号
         */
        private final String DELETE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=";

        /**
         * 客服聊天记录
         */
        private final String RECORD = "https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token=";

        /**
         * 创建客服会话
         */
        private final String CREATE_SESSION = "https://api.weixin.qq.com/customservice/kfsession/create?access_token=";

        /**
         * 关闭客服会话
         */
        private final String CLOSE_SESSION = "https://api.weixin.qq.com/customservice/kfsession/close?access_token=";

        /**
         * 获取用户会话状态
         */
        private final String USER_SESSION_STATUS = "https://api.weixin.qq.com/customservice/kfsession/getsession?access_token=";

        /**
         * 获取客服会话状态
         */
        private final String CS_SESSION_STATUS = "https://api.weixin.qq.com/customservice/kfsession/getsessionlist?access_token=";

        /**
         * 未接入会话
         */
        private final String WAITING_SESSION = "https://api.weixin.qq.com/customservice/kfsession/getwaitcase?access_token=";

        private CustomerServices(){}

        /**
         * 添加客服账户
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @return 添加成功返回true，反之false
         */
        public Boolean createAccount(String account, String nickName, String password){
            return createAccount(loadAccessToken(), account, nickName, password);
        }

        /**
         * 添加客服账户
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @param cb 回调
         */
        public void createAccount(final String account, final String nickName, final String password, final Callback<Boolean> cb){
            createAccount(loadAccessToken(), account, nickName, password, cb);
        }

        /**
         * 添加客服账户
         * @param accessToken accessToken
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @param cb 回调
         */
        public void createAccount(final String accessToken, final String account, final String nickName, final String password, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return closeSession(accessToken, account, nickName, password);
                }
            });
        }

        /**
         * 添加客服账户
         * @param accessToken accessToken
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @return 添加成功返回true，反之false
         */
        public Boolean createAccount(String accessToken, String account, String nickName, String password){
            String url = CREATE_ACCOUNT + accessToken;
            return createOrUpdateAccount(account, nickName, password, url);
        }

        /**
         * 更新客服账户
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @return 添加成功返回true，或抛WechatException
         */
        public Boolean updateAccount(String account, String nickName, String password){
            return updateAccount(loadAccessToken(), account, nickName, password);
        }

        /**
         * 更新客服账户
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @param cb 回调
         */
        public void updateAccount(final String account, final String nickName, final String password, Callback<Boolean> cb){
            updateAccount(loadAccessToken(), account, nickName, password, cb);
        }

        /**
         * 更新客服账户
         * @param accessToken accessToken
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @param cb 回调
         */
        public void updateAccount(final String accessToken, final String account, final String nickName, final String password, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return updateAccount(accessToken, account, nickName, password);
                }
            });
        }

        /**
         * 更新客服账户
         * @param accessToken accessToken
         * @param account 登录帐号(包含域名)
         * @param nickName 昵称
         * @param password 明文密码
         * @return 添加成功返回true，或抛WechatException
         */
        public Boolean updateAccount(String accessToken, String account, String nickName, String password){
            String url = UPDATE_ACCOUNT + accessToken;
            return createOrUpdateAccount(account, nickName, password, url);
        }

        private Boolean createOrUpdateAccount(String account, String nickName, String password, String url) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);

            params.put("kf_account", account);
            params.put("nickname", nickName);
            params.put("password", Hashing.md5().hashString(password, Charsets.UTF_8).toString());

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 删除客服账户
         * @param kfAccount 客服登录帐号(包含域名)
         * @param cb 回调
         */
        public void deleteAccount(String kfAccount, Callback<Boolean> cb){
            deleteAccount(loadAccessToken(), kfAccount, cb);
        }

        /**
         * 删除客服账户
         * @param kfAccount 客服登录帐号(包含域名)
         * @return 添加成功返回true，或抛WechatException
         */
        public Boolean deleteAccount(String kfAccount){
            return deleteAccount(loadAccessToken(), kfAccount);
        }

        /**
         * 删除客服账户
         * @param accessToken accessToken
         * @param kfAccount 客服登录帐号(包含域名)
         * @param cb 回调
         */
        public void deleteAccount(final String accessToken, final String kfAccount, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return deleteAccount(accessToken, kfAccount);
                }
            });
        }

        /**
         * 删除客服账户
         * @param accessToken accessToken
         * @param kfAccount 客服登录帐号(包含域名)
         * @return 添加成功返回true，或抛WechatException
         */
        public Boolean deleteAccount(String accessToken, String kfAccount){
            String url = DELETE_ACCOUNT + accessToken + "&kf_account=" + kfAccount;

            doGet(url);

            return Boolean.TRUE;
        }

        /**
         * 构建转发客服的XML消息(该消息自动转发给一个在线的客服)
         * @param openId 用户openId
         * @return 转发客服的XML消息
         */
        public String forward(String openId){
            return forward(openId, null);
        }

        /**
         * 构建转发客服的XML消息(指定一个在线的客服，若该客服不在线，消息将不再转发给其他在线客服)
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @return 转发客服的XML消息
         */
        public String forward(String openId, String kfAccount){
            XmlWriters xmlWriters = XmlWriters.create();

            xmlWriters.element("ToUserName", openId)
                .element("FromUserName", appId)
                .element("CreateTime", System.currentTimeMillis() / 1000);

            if (!Strings.isNullOrEmpty(kfAccount)){
                xmlWriters.element("TransInfo", "KfAccount", kfAccount);
            }
            xmlWriters.element("MsgType", "transfer_customer_service");

            return xmlWriters.build();
        }

        /**
         * 查询客服聊天记录
         * @param pageNo 页码
         * @param pageSize 分页大小
         * @param startTime 起始时间
         * @param endTime 结束时间
         * @return 客服聊天记录，或抛WechatException
         */
        public List<MsgRecord> getMsgRecords(Integer pageNo, Integer pageSize, Date startTime, Date endTime){
            return getMsgRecords(loadAccessToken(), pageNo, pageSize, startTime, endTime);
        }

        /**
         * 查询客服聊天记录
         * @param pageNo 页码
         * @param pageSize 分页大小
         * @param startTime 起始时间
         * @param endTime 结束时间
         */
        public void getMsgRecords(final Integer pageNo, final Integer pageSize, final Date startTime, final Date endTime, Callback<List<MsgRecord>> cb){
            getMsgRecords(loadAccessToken(), pageNo, pageSize, startTime, endTime, cb);
        }

        /**
         * 查询客服聊天记录
         * @param accessToken accessToken
         * @param pageNo 页码
         * @param pageSize 分页大小
         * @param startTime 起始时间
         * @param endTime 结束时间
         */
        public void getMsgRecords(final String accessToken, final Integer pageNo, final Integer pageSize, final Date startTime, final Date endTime, Callback<List<MsgRecord>> cb){
            doAsync(new AsyncFunction<List<MsgRecord>>(cb) {
                @Override
                public List<MsgRecord> execute() {
                    return getMsgRecords(accessToken, pageNo, pageSize, startTime, endTime);
                }
            });
        }

        /**
         * 查询客服聊天记录
         * @param accessToken accessToken
         * @param pageNo 页码
         * @param pageSize 分页大小
         * @param startTime 起始时间
         * @param endTime 结束时间
         * @return 客服聊天记录，或抛WechatException
         */
        @SuppressWarnings("unchecked")
        public List<MsgRecord> getMsgRecords(String accessToken, Integer pageNo, Integer pageSize, Date startTime, Date endTime){
            String url = RECORD + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
            params.put("pageindex", pageNo);
            params.put("pagesize", pageSize);
            params.put("starttime", startTime.getTime());
            params.put("endtime", endTime.getTime());

            Map<String, Object> resp = doPost(url, params);
            List<Map<String, Object>> records = (List<Map<String, Object>>)resp.get("recordlist");
            if (records.isEmpty()){
                return Collections.EMPTY_LIST;
            }
            List<MsgRecord> msgs = new ArrayList<>();
            for (Map<String, Object> record : records){
                msgs.add(renderMsgRecord(record));
            }
            return msgs;
        }

        private MsgRecord renderMsgRecord(Map<String, Object> record) {
            MsgRecord msg = new MsgRecord();
            msg.setOpenid((String)record.get("openid"));
            msg.setOpercode((String)record.get("opercode"));
            msg.setText((String)record.get("text"));
            msg.setTime(new Date((Integer)record.get("time") * 1000L));
            msg.setWorker((String)record.get("worker"));
            return msg;
        }

        /**
         * 创建会话(该客服必需在线)
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @return 创建成功返回true，或抛WechatException
         */
        public Boolean createSession(String openId, String kfAccount, String text){
            return createSession(loadAccessToken(), openId, kfAccount, text);
        }

        /**
         * 创建会话(该客服必需在线)
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @param cb 回调
         */
        public void createSession(final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
            createSession(loadAccessToken(), openId, kfAccount, text, cb);
        }

        /**
         * 创建会话(该客服必需在线)
         * @param accessToken accessToken
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @param cb 回调
         */
        public void createSession(final String accessToken, final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return createSession(accessToken, openId, kfAccount, text);
                }
            });
        }

        /**
         * 创建会话(该客服必需在线)
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @return 创建成功返回true，或抛WechatException
         */
        public Boolean createSession(String accessToken, String openId, String kfAccount, String text){
            return createOrCloseSession(openId, kfAccount, text, CREATE_SESSION + accessToken);
        }

        /**
         * 关闭会话
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @return 关闭成功返回true，或抛WechatException
         */
        public Boolean closeSession(String openId, String kfAccount, String text){
            return closeSession(loadAccessToken(), openId, kfAccount, text);
        }

        /**
         * 关闭会话
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @param cb 回调
         */
        public void closeSession(final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
            closeSession(loadAccessToken(), openId, kfAccount, text, cb);
        }

        /**
         * 关闭会话
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @param cb 回调
         */
        public void closeSession(final String accessToken, final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return closeSession(accessToken, openId, kfAccount, text);
                }
            });
        }

        /**
         * 关闭会话
         * @param openId 用户openId
         * @param kfAccount 客服帐号(包含域名)
         * @param text 附加文本
         * @return 关闭成功返回true，或抛WechatException
         */
        public Boolean closeSession(String accessToken, String openId, String kfAccount, String text){
            return createOrCloseSession(openId, kfAccount, text, CLOSE_SESSION + accessToken);
        }

        private Boolean createOrCloseSession(String openId, String kfAccount, String text, String url){

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("openid", openId);
            params.put("kf_account", kfAccount);
            params.put("text", text);

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 获取用户的会话状态
         * @param openId 用户openId
         * @return 客户的会话状态，或抛WechatException
         */
        public UserSession getUserSession(String openId){
            return getUserSession(loadAccessToken(), openId);
        }

        /**
         * 获取用户的会话状态
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param cb 回调
         */
        public void getUserSession(final String accessToken, final String openId, Callback<UserSession> cb){
            doAsync(new AsyncFunction<UserSession>(cb) {
                @Override
                public UserSession execute() {
                    return getUserSession(accessToken, openId);
                }
            });
        }

        /**
         * 获取用户的会话状态
         * @param openId 用户openId
         * @param cb 回调
         */
        public void getUserSession(final String openId, Callback<UserSession> cb){
            getUserSession(loadAccessToken(), openId, cb);
        }

        /**
         * 获取用户的会话状态
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 客户的会话状态，或抛WechatException
         */
        public UserSession getUserSession(String accessToken, String openId){
            String url = USER_SESSION_STATUS + accessToken + "&openid=" + openId;

            Map<String, Object> resp = doGet(url);
            UserSession status = new UserSession();
            status.setKfAccount(String.valueOf(resp.get("kf_account")));
            status.setCreateTime(new Date((Integer)resp.get("createtime") * 1000L));

            return status;
        }

        /**
         * 获取客服的会话列表
         * @param kfAccount 客服帐号(包含域名)
         * @return 客服的会话列表，或抛WechatException
         */
        public List<CsSession> getCsSessions(String kfAccount){
            return getCsSessions(loadAccessToken(), kfAccount);
        }

        /**
         * 获取客服的会话列表
         * @param kfAccount 客服帐号(包含域名)
         * @param cb 回调
         */
        public void getCsSessions(final String kfAccount, Callback<List<CsSession>> cb){
            getCsSessions(loadAccessToken(), kfAccount, cb);
        }


        /**
         * 获取客服的会话列表
         * @param accessToken accessToken
         * @param kfAccount 客服帐号(包含域名)
         * @param cb 回调
         */
        public void getCsSessions(final String accessToken, final String kfAccount, Callback<List<CsSession>> cb){
            doAsync(new AsyncFunction<List<CsSession>>(cb) {
                @Override
                public List<CsSession> execute() {
                    return getCsSessions(accessToken, kfAccount);
                }
            });
        }

        /**
         * 获取客服的会话列表
         * @param accessToken accessToken
         * @param kfAccount 客服帐号(包含域名)
         * @return 客服的会话列表，或抛WechatException
         */
        @SuppressWarnings("unchecked")
        public List<CsSession> getCsSessions(String accessToken, String kfAccount){
            String url = CS_SESSION_STATUS + accessToken + "&kf_account=" + kfAccount;

            Map<String, Object> resp = doGet(url);
            List<Map<String, Object>> sessions = (List<Map<String, Object>>)resp.get("sessionlist");
            if (sessions.isEmpty()){
                return Collections.emptyList();
            }
            List<CsSession> ss = new ArrayList<>();
            for (Map<String, Object> session : sessions){
                ss.add(renderCsSession(session));
            }

            return ss;
        }

        private CsSession renderCsSession(Map<String, Object> session) {
            CsSession s = new CsSession();
            s.setOpenId((String)session.get("openid"));
            s.setCreateTime(new Date((Integer)session.get("createtime") * 1000L));
            return s;
        }

        /**
         * 获取未接入的会话列表
         * @return 未接入的会话列表，或抛WechatException
         */
        public List<WaitingSession> getWaitingSessions(){
            return getWaitingSessions(loadAccessToken());
        }

        /**
         * 获取未接入的会话列表
         * @param cb 回调
         */
        public void getWaitingSessions(Callback<List<WaitingSession>> cb){
            getWaitingSessions(loadAccessToken(), cb);
        }

        /**
         * 获取未接入的会话列表
         * @param accessToken accessToken
         * @param cb 回调
         */
        public void getWaitingSessions(final String accessToken, Callback<List<WaitingSession>> cb){
            doAsync(new AsyncFunction<List<WaitingSession>>(cb) {
                @Override
                public List<WaitingSession> execute() {
                    return getWaitingSessions(accessToken);
                }
            });
        }

        /**
         * 获取未接入的会话列表
         * @param accessToken accessToken
         * @return 未接入的会话列表，或抛WechatException
         */
        @SuppressWarnings("unchecked")
        public List<WaitingSession> getWaitingSessions(String accessToken){
            String url = WAITING_SESSION + accessToken;

            Map<String, Object> resp = doGet(url);
            List<Map<String, Object>> sessions = (List<Map<String, Object>>)resp.get("waitcaselist");
            if (sessions.isEmpty()){
                return Collections.emptyList();
            }
            List<WaitingSession> ss = new ArrayList<>();
            for (Map<String, Object> session : sessions){
                ss.add(renderWaitingSession(session));
            }

            return ss;
        }

        private WaitingSession renderWaitingSession(Map<String, Object> session) {
            WaitingSession s = new WaitingSession();
            s.setOpenId((String)session.get("openid"));
            s.setKfAccount((String)session.get("kf_account"));
            s.setCreateTime(new Date((Integer)session.get("createtime") * 1000L));
            return s;
        }
    }

    /**
     * 菜单API
     */
    public final class Menus {

        /**
         * 查询菜单
         */
        private final String GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

        /**
         * 创建菜单
         */
        private final String CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

        /**
         * 删除菜单
         */
        private final String DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

        private Menus(){}

        /**
         * 查询菜单
         * @return 菜单列表
         */
        public List<Menu> get(){
            return get(loadAccessToken());
        }

        /**
         * 查询菜单
         * @param cb 回调
         */
        public void get(Callback<List<Menu>> cb){
            get(loadAccessToken(), cb);
        }

        /**
         * 查询菜单
         * @param accessToken accessToken
         * @param cb 回调
         */
        public void get(final String accessToken, Callback<List<Menu>> cb){
            doAsync(new AsyncFunction<List<Menu>>(cb) {
                @Override
                public List<Menu> execute() {
                    return get(accessToken);
                }
            });
        }

        /**
         * 查询菜单
         * @param accessToken accessToken
         * @return 菜单列表
         */
        public List<Menu> get(String accessToken){
            String url = GET + accessToken;
            Map<String, Object> resp = Http.get(url).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            String jsonMenu = Jsons.DEFAULT.toJson(((Map) resp.get("menu")).get("button"));
            return Jsons.EXCLUDE_DEFAULT.fromJson(jsonMenu, Types.ARRAY_LIST_MENU_TYPE);
        }

        /**
         * 创建菜单
         * @param jsonMenu 菜单json
         * @return 创建成功返回true，或抛WechatException
         */
        public Boolean create(String jsonMenu){
            return create(loadAccessToken(), jsonMenu);
        }

        /**
         * 创建菜单
         * @param accessToken 访问token
         * @param jsonMenu 菜单json
         * @param cb 回调
         */
        public void create(final String accessToken, final String jsonMenu, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return create(accessToken, jsonMenu);
                }
            });
        }

        /**
         * 创建菜单
         * @param jsonMenu 菜单json
         * @param cb 回调
         */
        public void create(final String jsonMenu, Callback<Boolean> cb){
            create(loadAccessToken(), jsonMenu, cb);
        }

        /**
         * 创建菜单
         * @param accessToken 访问token
         * @param jsonMenu 菜单json
         * @return 创建成功返回true，或抛WechatException
         */
        public Boolean create(String accessToken, String jsonMenu){
            String url = CREATE + accessToken;
            Map<String, Object> resp =
                    Http.post(url).body(jsonMenu).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 删除菜单
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean delete(){
            return delete(loadAccessToken());
        }

        /**
         * 删除菜单
         * @param cb 回调
         */
        public void delete(Callback<Boolean> cb){
            delete(loadAccessToken(), cb);
        }

        /**
         * 删除菜单
         * @param accessToken accessToken
         * @param cb 回调
         */
        public void delete(final String accessToken, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return delete(accessToken);
                }
            });
        }

        /**
         * 删除菜单
         * @param accessToken accessToken
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean delete(String accessToken){
            String url = DELETE + accessToken;
            Map<String, Object> resp =
                    Http.get(url).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }
    }

    public static final class MenuBuilder{

        @JsonProperty("button")
        private List<Menu> menus = new ArrayList<>();

        private MenuBuilder(){}

        public static MenuBuilder newBuilder(){
            return new MenuBuilder();
        }

        /**
         * 创建一个一级菜单
         * @param m 菜单对象
         */
        public MenuBuilder menu(Menu m){
            menus.add(m);
            return this;
        }

        /**
         * 创建一个CLICK一级菜单
         * @param name 名称
         * @param key 键
         */
        public MenuBuilder click(String name, String key){
            Menu m = newClickMenu(name, key);
            menus.add(m);
            return this;
        }

        /**
         * 创建一个CLICK二级菜单
         * @param parent 父级菜单
         * @param name 名称
         * @param key 键
         */
        public MenuBuilder click(Menu parent, String name, String key){
            Menu m = newClickMenu(name, key);
            parent.getChildren().add(m);
            return this;
        }

        /**
         * 创建一个VIEW一级菜单
         * @param name 名称
         * @param url 链接
         */
        public MenuBuilder view(String name, String url){
            Menu m = newViewMenu(name, url);
            menus.add(m);
            return this;
        }

        /**
         * 创建一个VIEW二级菜单
         * @param parent 父级菜单
         * @param name 名称
         * @param url 链接
         */
        public MenuBuilder view(Menu parent, String name, String url){
            Menu m = newViewMenu(name, url);
            parent.getChildren().add(m);
            return this;
        }

        /**
         * 创建一个VIEW菜单
         * @param name 名称
         * @param url 链接
         */
        public Menu newViewMenu(String name, String url) {
            Menu m = new Menu();
            m.setName(name);
            m.setType(MenuType.VIEW.value());
            m.setUrl(url);
            return m;
        }

        /**
         * 创建一个CLICK菜单
         * @param name 名称
         * @param key 键
         */
        public Menu newClickMenu(String name, String key) {
            Menu m = new Menu();
            m.setName(name);
            m.setType(MenuType.CLICK.value());
            m.setKey(key);
            return m;
        }

        /**
         * 创建一个一级菜单
         * @param name 名称
         */
        public Menu newParentMenu(String name) {
            Menu m = new Menu();
            m.setName(name);
            return m;
        }

        /**
         * 返回菜单的json数据
         */
        public String build(){
            return Jsons.EXCLUDE_EMPTY.toJson(this);
        }
    }

    /**
     * 用户API
     */
    public final class Users {

        /**
         * 创建用户分组
         */
        private final String CREATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";

        /**
         * 获取用户分组列表
         */
        private final String GET_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";

        /**
         * 删除分组
         */
        private final String DELETE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=";

        /**
         * 更新分组名称
         */
        private final String UPDATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";

        /**
         * 获取用户所在分组
         */
        private final String GROUP_OF_USER = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";

        /**
         * 移动用户所在组
         */
        private final String MOVE_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";

        /**
         * 拉取用户信息
         */
        private final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&access_token=";

        /**
         * 备注用户
         */
        private final String REMARK_USER = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";

        private Users(){}

        /**
         * 创建用户分组
         * @param name 名称
         * @return 分组ID，或抛WechatException
         */
        public Integer createGroup(String name){
            return createGroup(loadAccessToken(), name);
        }

        /**
         * 创建用户分组
         * @param name 名称
         * @param cb 回调
         */
        public void createGroup(final String name, Callback<Integer> cb){
            createGroup(loadAccessToken(), name, cb);
        }

        /**
         * 创建用户分组
         * @param accessToken accessToken
         * @param name 名称
         * @param cb 回调
         */
        public void createGroup(final String accessToken, final String name, Callback<Integer> cb){
            doAsync(new AsyncFunction<Integer>(cb) {
                @Override
                public Integer execute() {
                    return createGroup(accessToken, name);
                }
            });
        }

        /**
         * 创建用户分组
         * @param accessToken accessToken
         * @param name 名称
         * @return 分组ID，或抛WechatException
         */
        public Integer createGroup(String accessToken, String name){
            String url = CREATE_GROUP + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            Group g = new Group();
            g.setName(name);
            params.put("group", g);

            Map<String, Object> resp = doPost(url, params);
            return (Integer)((Map)resp.get("group")).get("id");
        }

        /**
         * 获取所有分组列表
         * @return 分组列表，或抛WechatException
         */
        public List<Group> getGroup(){
            return getGroup(loadAccessToken());
        }

        /**
         * 获取所有分组列表
         * @param accessToken accessToken
         * @param cb 回调
         */
        public void getGroup(final String accessToken, Callback<List<Group>> cb){
            doAsync(new AsyncFunction<List<Group>>(cb) {
                @Override
                public List<Group> execute() {
                    return getGroup(accessToken);
                }
            });
        }

        /**
         * 获取所有分组列表
         * @param accessToken accessToken
         * @return 分组列表，或抛WechatException
         */
        public List<Group> getGroup(String accessToken){
            String url = GET_GROUP + accessToken;

            Map<String, Object> resp = doGet(url);

            return Jsons.EXCLUDE_DEFAULT
                        .fromJson(Jsons.DEFAULT.toJson(resp.get("groups")), Types.ARRAY_LIST_GROUP_TYPE);
        }

        /**
         * 删除分组
         * @param id 分组ID
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean deleteGroup(Integer id){
            return deleteGroup(loadAccessToken(), id);
        }

        /**
         * 删除分组
         * @param id 分组ID
         * @param cb 回调
         */
        public void deleteGroup(final Integer id, Callback<Boolean> cb){
            deleteGroup(id, cb);
        }

        /**
         * 删除分组
         * @param accessToken accessToken
         * @param id 分组ID
         * @param cb 回调
         */
        public void deleteGroup(final String accessToken, final Integer id, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return deleteGroup(accessToken, id);
                }
            });
        }

        /**
         * 删除分组
         * @param accessToken accessToken
         * @param id 分组ID
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean deleteGroup(String accessToken, Integer id){
            String url = DELETE_GROUP + accessToken;

            Group g = new Group();
            g.setId(id);
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("group", g);

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 更新分组名称
         * @param id 分组ID
         * @param newName 分组新名称
         * @return 更新成功返回true，或抛WechatException
         */
        public Boolean updateGroup(Integer id, String newName){
            return updateGroup(loadAccessToken(), id, newName);
        }

        /**
         * 更新分组名称
         * @param id 分组ID
         * @param newName 分组新名称
         * @param cb 回调
         */
        public void updateGroup(final Integer id, final String newName, Callback<Boolean> cb){
            updateGroup(loadAccessToken(), id, newName, cb);
        }

        /**
         * 更新分组名称
         * @param accessToken accessToken
         * @param id 分组ID
         * @param newName 分组新名称
         * @param cb 回调
         */
        public void updateGroup(final String accessToken, final Integer id, final String newName, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return updateGroup(accessToken, id, newName);
                }
            });
        }

        /**
         * 更新分组名称
         * @param accessToken accessToken
         * @param id 分组ID
         * @param newName 分组新名称
         * @return 更新成功返回true，或抛WechatException
         */
        public Boolean updateGroup(String accessToken, Integer id, String newName){
            String url = UPDATE_GROUP + accessToken;

            Group g = new Group();
            g.setId(id);
            g.setName(newName);
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("group", g);

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 获取用户所在组
         * @param openId 用户openId
         * @return 组ID，或抛WechatException
         */
        public Integer getUserGroup(String openId){
            return getUserGroup(loadAccessToken(), openId);
        }

        /**
         * 获取用户所在组
         * @param openId 用户openId
         * @param cb 回调
         */
        public void getUserGroup(final String openId, Callback<Integer> cb){
            getUserGroup(loadAccessToken(), openId, cb);
        }

        /**
         * 获取用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param cb 回调
         */
        public void getUserGroup(final String accessToken, final String openId, Callback<Integer> cb){
            doAsync(new AsyncFunction<Integer>(cb) {
                @Override
                public Integer execute() {
                    return getUserGroup(accessToken, openId);
                }
            });
        }

        /**
         * 获取用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 组ID，或抛WechatException
         */
        public Integer getUserGroup(String accessToken, String openId){
            String url = GROUP_OF_USER + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("openid", openId);

            Map<String, Object> resp = doPost(url, params);
            return (Integer)resp.get("groupid");
        }

        /**
         * 移动用户所在组
         * @param openId 用户openId
         * @param groupId 新组ID
         * @return 移动成功返回true，或抛WechatException
         */
        public Boolean mvUserGroup(String openId, Integer groupId){
            return mvUserGroup(loadAccessToken(), openId, groupId);
        }

        /**
         * 移动用户所在组
         * @param openId 用户openId
         * @param groupId 新组ID
         * @param cb 回调
         */
        public void mvUserGroup(final String openId, final Integer groupId, Callback<Boolean> cb){
            mvUserGroup(loadAccessToken(), openId, groupId, cb);
        }

        /**
         * 移动用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param groupId 新组ID
         * @param cb 回调
         */
        public void mvUserGroup(final String accessToken, final String openId, final Integer groupId, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return mvUserGroup(accessToken, openId, groupId);
                }
            });
        }

        /**
         * 移动用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param groupId 新组ID
         * @return 移动成功返回true，或抛WechatException
         */
        public Boolean mvUserGroup(String accessToken, String openId, Integer groupId){
            String url = MOVE_USER_GROUP + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("openid", openId);
            params.put("to_groupid", groupId);

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        public User getUser(String openId){
            return getUser(loadAccessToken(), openId);
        }

        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param openId 用户openId
         * @param cb 回调
         */
        public void getUser(final String openId, Callback<User> cb){
            getUser(loadAccessToken(), openId, cb);
        }

        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param cb 回调
         */
        public void getUser(final String accessToken, final String openId, Callback<User> cb){
            doAsync(new AsyncFunction<User>(cb) {
                @Override
                public User execute() {
                    return getUser(accessToken, openId);
                }
            });
        }

        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        public User getUser(String accessToken, String openId){
            String url = GET_USER_INFO + accessToken + "&openid=" + openId;

            Map<String, Object> resp = doGet(url);

            return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), User.class);
        }

        /**
         * 备注用户
         * @param openId 用户openId
         * @param remark 备注
         * @return 备注成功返回true，或抛WechatException
         */
        public Boolean remarkUser(String openId, String remark){
            return remarkUser(loadAccessToken(), openId, remark);
        }

        /**
         * 备注用户
         * @param openId 用户openId
         * @param remark 备注
         * @param cb 回调
         */
        public void remarkUser(final String openId, final String remark, Callback<Boolean> cb){
            remarkUser(loadAccessToken(), openId, remark, cb);
        }

        /**
         * 备注用户
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param remark 备注
         * @param cb 回调
         */
        public void remarkUser(final String accessToken, final String openId, final String remark, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return remarkUser(accessToken, openId, remark);
                }
            });
        }

        /**
         * 备注用户
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param remark 备注
         * @return 备注成功返回true，或抛WechatException
         */
        public Boolean remarkUser(String accessToken, String openId, String remark){
            String url = REMARK_USER + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("openid", openId);
            params.put("remark", remark);

            doPost(url, params);
            return Boolean.TRUE;
        }
    }

    /**
     * 消息API
     */
    public final class Messages {

        /**
         * 发送模板消息
         */
        private final String TEMPLATE_SEND = "http://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

        /**
         * 分组群发消息
         */
        private final String SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";

        /**
         * 按openId列表群发消息
         */
        private final String SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";

        /**
         * 删除群发消息
         */
        private final String DELETE_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=";

        /**
         * 预览群发消息
         */
        private final String PREVIEW_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=";

        /**
         * 查询群发消息状态
         */
        private final String GET_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=";


        private Messages(){}

        /**
         * 被动回复微信服务器文本消息
         * @param openId 用户openId
         * @param content 文本内容
         * @return XML文本消息
         */
        public String respText(String openId, String content){
            XmlWriters msg = respCommonElements(openId, RespMessageType.TEXT);
            msg.element("Content", content);
            return msg.build();
        }

        /**
         * 被动回复微信服务器图片消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @return XML图片消息
         */
        public String respImage(String openId, String mediaId){
            XmlWriters msg = respCommonElements(openId, RespMessageType.IMAGE);
            msg.element("Image", "MediaId", mediaId);
            return msg.build();
        }

        /**
         * 被动回复微信服务器语音消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @return XML语音消息
         */
        public String respVoice(String openId, String mediaId){
            XmlWriters msg = respCommonElements(openId, RespMessageType.VOICE);
            msg.element("Voice", "MediaId", mediaId);
            return msg.build();
        }

        /**
         * 被动回复微信服务器视频消息
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @param title 标题
         * @param desc 描述
         * @return XML视频消息
         */
        public String respVideo(String openId, String mediaId, String title, String desc){
            XmlWriters msg = respCommonElements(openId, RespMessageType.VIDEO);
            msg.element("Video", "MediaId", mediaId, "Title", title, "Description", desc);
            return msg.build();
        }

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
        public String respMusic(String openId, String mediaId,
                                String title, String desc, String url, String hqUrl){
            XmlWriters msg = respCommonElements(openId, RespMessageType.MUSIC);
            msg.element("Music",
                    "Title", title,
                    "Description", desc,
                    "MusicURL", url,
                    "HQMusicUrl", hqUrl,
                    "ThumbMediaId", mediaId);
            return msg.build();
        }

        /**
         * 被动回复微信服务器图文消息
         * @param openId 用户openId
         * @param articles 图片消息对象列表，长度小于10
         * @return XML图文消息
         */
        public String respNews(String openId, List<Article> articles){
            if (articles.size() > 10){
                articles = articles.subList(0, 10);
            }
            XmlWriters xmlWriters = respCommonElements(openId, RespMessageType.NEWS);
            xmlWriters.element("ArticleCount", articles.size());
            List<XmlWriters.E> items = new ArrayList<>();
            XmlWriters.E item;
            for (Article article : articles){
                item = xmlWriters.newElement("item",
                                        "Title", article.getTitle(),
                                        "Description", article.getDesc(),
                                        "PicUrl", article.getPicUrl(),
                                        "Url", article.getUrl());
                items.add(item);
            }
            xmlWriters.element("Articles", items);
            return xmlWriters.build();
        }

        private XmlWriters respCommonElements(String openId, RespMessageType type) {
            XmlWriters xmlWriters = XmlWriters.create();
            xmlWriters.element("ToUserName", openId)
                .element("FromUserName", appId)
                .element("CreateTime", System.currentTimeMillis() / 1000)
                .element("MsgType", type.value());
            return xmlWriters;
        }

        /**
         * 接收微信服务器发来的XML消息
         * @param xml xml字符串
         * @return 消息类，或抛WechatException
         */
        public RecvMessage receive(String xml){
            XmlReaders readers = XmlReaders.create(xml);
            return receiveRecvMessage(readers);
        }

        /**
         * 接收微信服务器发来的XML消息
         * @param xml xml字符串
         * @return 消息类，或抛WechatException
         */
        public RecvMessage receive(InputStream xml){
            XmlReaders readers = XmlReaders.create(xml);
            return receiveRecvMessage(readers);
        }

        private RecvMessage receiveRecvMessage(XmlReaders readers) {
            RecvMessage msg = parse2RecvMessage(readers);
            RecvMessageType type = RecvMessageType.from(msg.getMsgType());
            if (RecvMessageType.EVENT == type){
                return parse2RecvEvent(readers, msg);
            } else {
                return parse2RecvMsg(readers, msg);
            }
        }

        private RecvMessage parse2RecvMessage(XmlReaders readers) {
            RecvMessage m = new RecvMessage();
            m.setFromUserName(readers.getNodeStr("FromUserName"));
            m.setToUserName(readers.getNodeStr("ToUserName"));
            m.setCreateTime(readers.getNodeInt("CreateTime"));
            m.setMsgType(readers.getNodeStr("MsgType"));
            return m;
        }

        /**
         * 接收事件消息
         */
        private RecvMessage parse2RecvEvent(XmlReaders readers, RecvMessage msg) {

            String eventValue = readers.getNodeStr("Event");
            RecvEvent event = new RecvEvent(msg);
            event.setEventType(eventValue);

            RecvEventType type = RecvEventType.from(eventValue);
            switch (type){

                case SUBSCRIBE:
                    RecvSubscribeEvent subscribe = new RecvSubscribeEvent(event);
                    // 用户未关注时，扫码关注后会有这两个属性
                    subscribe.setEventKey(readers.getNodeStr("EventKey"));
                    subscribe.setTicket(readers.getNodeStr("Ticket"));
                    return subscribe;

                case UN_SUBSCRIBE:
                    return new RecvUnSubscribeEvent(event);

                case MENU_CLICK:
                case MENU_VIEW:
                    RecvMenuEvent menu = new RecvMenuEvent(event);
                    menu.setEventKey(readers.getNodeStr("EventKey"));
                    return menu;

                case LOCATION:
                    RecvLocationEvent location = new RecvLocationEvent(event);
                    location.setLatitude(readers.getNodeStr("Latitude"));
                    location.setLongitude(readers.getNodeStr("Longitude"));
                    location.setPrecision(readers.getNodeStr("Precision"));
                    return location;

                case SCAN:
                    RecvScanEvent scan = new RecvScanEvent(event);
                    scan.setEventKey(readers.getNodeStr("EventKey"));
                    scan.setTicket(readers.getNodeStr("Ticket"));
                    return scan;

                default:
                    throw new IllegalArgumentException("unknown event msg");
            }
        }

        /**
         * 接收普通消息
         */
        private RecvMessage parse2RecvMsg(XmlReaders readers, RecvMessage message) {

            RecvMessageType type = RecvMessageType.from(message.getMsgType());
            RecvMsg msg = new RecvMsg(message);
            msg.setMsgId(readers.getNodeLong("MsgId"));

            switch (type){
                case TEXT:
                    RecvTextMessage text = new RecvTextMessage(msg);
                    text.setContent(readers.getNodeStr("Content"));
                    return text;

                case IMAGE:
                    RecvImageMessage image = new RecvImageMessage(msg);
                    image.setPicUrl(readers.getNodeStr("PicUrl"));
                    image.setMediaId(readers.getNodeStr("MediaId"));
                    return image;

                case VOICE:
                    RecvVoiceMessage voice = new RecvVoiceMessage(msg);
                    voice.setFormat(readers.getNodeStr("Format"));
                    voice.setMediaId(readers.getNodeStr("MediaId"));
                    voice.setRecognition(readers.getNodeStr("Recognition"));
                    return voice;

                case VIDEO:
                    RecvVideoMessage video = new RecvVideoMessage(msg);
                    video.setMediaId(readers.getNodeStr("MediaId"));
                    video.setThumbMediaId(readers.getNodeStr("ThumbMediaId"));
                    return video;

                case SHORT_VIDEO:
                    RecvShortVideoMessage svideo = new RecvShortVideoMessage(msg);
                    svideo.setMediaId(readers.getNodeStr("MediaId"));
                    svideo.setThumbMediaId(readers.getNodeStr("ThumbMediaId"));
                    return svideo;

                case LINK:
                    RecvLinkMessage link = new RecvLinkMessage(msg);
                    link.setTitle(readers.getNodeStr("Title"));
                    link.setDescription(readers.getNodeStr("Description"));
                    link.setUrl(readers.getNodeStr("Url"));
                    return link;

                case LOCATION:
                    RecvLocationMessage location = new RecvLocationMessage(msg);
                    location.setLabel(readers.getNodeStr("Label"));
                    location.setLocationX(readers.getNodeStr("LocationX"));
                    location.setLocationY(readers.getNodeStr("LocationY"));
                    location.setScale(readers.getNodeInt("Scale"));
                    return location;

                default:
                    throw new IllegalArgumentException("unknown msg type");
            }
        }

        /**
         * 向用户发送模版消息
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param fields 字段列表
         * @return 消息ID，或抛WechatException
         */
        public Integer sendTemplate(String openId, String templateId, List<TemplateField> fields){
            return sendTemplate(loadAccessToken(), openId, templateId, null, fields);
        }

        /**
         * 向用户发送模版消息
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param fields 字段列表
         * @return 消息ID，或抛WechatException
         */
        public Integer sendTemplate(String accessToken, String openId, String templateId, List<TemplateField> fields){
            return sendTemplate(accessToken, openId, templateId, null, fields);
        }

        /**
         * 向用户发送模版消息
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param fields 字段列表
         * @param link 点击链接
         * @return 消息ID，或抛WechatException
         */
        public Integer sendTemplate(String openId, String templateId, List<TemplateField> fields, String link){
            return sendTemplate(loadAccessToken(), openId, templateId, link, fields);
        }

        /**
         * 向用户发送模版消息
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param fields 字段列表
         * @param cb 回调
         */
        public void sendTemplate(final String openId, final String templateId, final List<TemplateField> fields, Callback<Integer> cb){
            sendTemplate(loadAccessToken(), openId, templateId, null, fields, cb);
        }

        /**
         * 向用户发送模版消息
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param link 点击链接
         * @param fields 字段列表
         * @param cb 回调
         */
        public void sendTemplate(final String openId, final String templateId,
                                 final String link, final List<TemplateField> fields, Callback<Integer> cb){
            sendTemplate(loadAccessToken(), openId, templateId, link, fields, cb);
        }

        /**
         * 向用户发送模版消息
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param link 点击链接
         * @param fields 字段列表
         * @param cb 回调
         */
        public void sendTemplate(final String accessToken, final String openId, final String templateId,
                                 final String link, final List<TemplateField> fields, Callback<Integer> cb){
            doAsync(new AsyncFunction<Integer>(cb) {
                @Override
                public Integer execute() {
                    return sendTemplate(accessToken, openId, templateId, link, fields);
                }
            });
        }

        /**
         * 向用户发送模版消息
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param templateId 模版ID
         * @param link 点击链接
         * @param fields 字段列表
         * @return 消息ID，或抛WechatException
         */
        public Integer sendTemplate(String accessToken, String openId, String templateId, String link, List<TemplateField> fields){
            String url = TEMPLATE_SEND + accessToken;

            Map<String, Object> params = buildTemplateParams(openId, templateId, link, fields);

            Map<String, Object> resp = doPost(url, params);
            return (Integer)resp.get("msgid");
        }

        private Map<String, Object> buildTemplateParams(String openId, String templateId, String link, List<TemplateField> fields) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
            params.put("touser", openId);
            params.put("template_id", templateId);
            if (!Strings.isNullOrEmpty(link)){
                params.put("url", link);
            }
            if (fields != null && !fields.isEmpty()){
                Map<String, Map<String, String>> data = Maps.newHashMapWithExpectedSize(fields.size());
                Map<String, String> dataItem;
                for (TemplateField field : fields){
                    dataItem = Maps.newHashMapWithExpectedSize(2);
                    dataItem.put("value", field.getValue());
                    dataItem.put("color", field.getColor());
                    data.put(field.getName(), dataItem);
                }
                params.put("data", data);
            }
            return params;
        }

        /**
         * 群发消息:
         *  1. 分组群发:【订阅号与服务号认证后均可用】
         *  2. 按OpenId列表发: 订阅号不可用，服务号认证后可用
         *  @see me.hao0.wechat.model.message.send.SendMessageScope
         * @param msg 消息
         * @return 消息ID，或抛WechatException
         */
        public Long send(SendMessage msg){
            return send(loadAccessToken(), msg);
        }

        /**
         * 群发消息:
         *  1. 分组群发:【订阅号与服务号认证后均可用】
         *  2. 按OpenId列表发: 订阅号不可用，服务号认证后可用
         *  @see me.hao0.wechat.model.message.send.SendMessageScope
         * @param msg 消息
         * @param cb 回调
         */
        public void send(final SendMessage msg, Callback<Long> cb){
            send(loadAccessToken(), msg, cb);
        }

        /**
         * 群发消息:
         *  1. 分组群发:【订阅号与服务号认证后均可用】
         *  2. 按OpenId列表发: 订阅号不可用，服务号认证后可用
         *  @see me.hao0.wechat.model.message.send.SendMessageScope
         * @param accessToken accessToken
         * @param msg 消息
         * @param cb 回调
         */
        public void send(final String accessToken, final SendMessage msg, Callback<Long> cb){
           doAsync(new AsyncFunction<Long>(cb) {
               @Override
               public Long execute() {
                   return send(accessToken, msg);
               }
           });
        }

        /**
         * 群发消息:
         *  1. 分组群发:【订阅号与服务号认证后均可用】
         *  2. 按OpenId列表发: 订阅号不可用，服务号认证后可用
         *  @see me.hao0.wechat.model.message.send.SendMessageScope
         * @param accessToken accessToken
         * @param msg 消息
         * @return 消息ID，或抛WechatException
         */
        public Long send(String accessToken, SendMessage msg){
            String url = (SendMessageScope.GROUP == msg.getScope() ? SEND_ALL : SEND) + accessToken;
            Map<String, Object> params = buildSendParams(msg);

            Map<String, Object> resp = doPost(url, params);
            return (Long)resp.get("msg_id");
        }

        private Map<String, Object> buildSendParams(SendMessage msg) {
            Map<String, Object> params = Maps.newHashMap();

            if (SendMessageScope.GROUP == msg.getScope()){
                Map<String, Object> scope = Maps.newHashMapWithExpectedSize(2);
                scope.put("is_to_all", msg.getIsToAll());
                scope.put("group_id", msg.getGroupId());
                params.put("filter", scope);
            } else {
                params.put("touser", msg.getOpenIds());
            }

            // send content
            Map<String, Object> msgContent = Maps.newHashMapWithExpectedSize(1);
            if (SendMessageType.TEXT == msg.getType()){
                // 文本
                msgContent.put("content", msg.getContent());
            } else if (SendMessageType.CARD == msg.getType()){
                // 卡券
                msgContent.put("card_id", msg.getCardId());
            } else {
                // 图文，图片，语音，视频
                msgContent.put("media_id", msg.getMediaId());
            }
            params.put(msg.getType().value(), msgContent);
            params.put("msgtype", msg.getType().value());

            if (!Strings.isNullOrEmpty(msg.getTitle())){
                params.put("title", msg.getTitle());
            }
            if (!Strings.isNullOrEmpty(msg.getDescription())){
                params.put("description", msg.getDescription());
            }
            if (!Strings.isNullOrEmpty(msg.getThumbMediaId())){
                params.put("thumb_media_id", msg.getThumbMediaId());
            }

            return params;
        }

        /**
         * 发送预览消息
         * @param msg 预览消息
         * @return 发送成功返回true，或抛WechatException
         */
        public Boolean previewSend(SendPreviewMessage msg){
            return previewSend(loadAccessToken(), msg);
        }

        /**
         * 发送预览消息
         * @param msg 预览消息
         * @param cb 回调
         */
        public void previewSend(final SendPreviewMessage msg, Callback<Boolean> cb){
            previewSend(loadAccessToken(), msg, cb);
        }

        /**
         * 发送预览消息
         * @param accessToken accessToken
         * @param msg 预览消息
         * @param cb 回调
         */
        public void previewSend(final String accessToken, final SendPreviewMessage msg, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return previewSend(accessToken, msg);
                }
            });
        }

        /**
         * 发送预览消息
         * @param accessToken accessToken
         * @param msg 预览消息
         * @return 发送成功返回true，或抛WechatException
         */
        public Boolean previewSend(String accessToken, SendPreviewMessage msg){
            String url = PREVIEW_SEND + accessToken;
            Map<String, Object> params = buildPreviewParams(msg);

            doPost(url, params);
            return Boolean.TRUE;
        }

        private Map<String, Object> buildPreviewParams(SendPreviewMessage msg) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);

            params.put("touser", msg.getOpenId());

            // send content
            Map<String, Object> msgContent = Maps.newHashMapWithExpectedSize(1);
            if (SendMessageType.TEXT == msg.getType()){
                // 文本
                msgContent.put("content", msg.getContent());
            } else if (SendMessageType.CARD == msg.getType()){
                // 卡券
                msgContent.put("card_id", msg.getCardId());
            } else {
                // 图文，图片，语音，视频
                msgContent.put("media_id", msg.getMediaId());
            }
            params.put(msg.getType().value(), msgContent);
            params.put("msgtype", msg.getType().value());

            return params;
        }

        /**
         * 删除群发消息: 订阅号与服务号认证后均可用:
         1、只有已经发送成功的消息才能删除
         2、删除消息是将消息的图文详情页失效，已经收到的用户，还是能在其本地看到消息卡片。
         3、删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
         4、如果多次群发发送的是一个图文消息，那么删除其中一次群发，就会删除掉这个图文消息也，导致所有群发都失效
         * @param id 群发消息ID
         * @return 删除成功，或抛WechatException
         */
        public Boolean deleteSend(Long id){
            return deleteSend(loadAccessToken(), id);
        }

        /**
         * 删除群发消息: 订阅号与服务号认证后均可用:
         1、只有已经发送成功的消息才能删除
         2、删除消息是将消息的图文详情页失效，已经收到的用户，还是能在其本地看到消息卡片。
         3、删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
         4、如果多次群发发送的是一个图文消息，那么删除其中一次群发，就会删除掉这个图文消息也，导致所有群发都失效
         * @param id 群发消息ID
         * @param cb 回调
         */
        public void deleteSend(final Long id, Callback<Boolean> cb){
            deleteSend(loadAccessToken(), id, cb);
        }

        /**
         * 删除群发消息: 订阅号与服务号认证后均可用:
         1、只有已经发送成功的消息才能删除
         2、删除消息是将消息的图文详情页失效，已经收到的用户，还是能在其本地看到消息卡片。
         3、删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
         4、如果多次群发发送的是一个图文消息，那么删除其中一次群发，就会删除掉这个图文消息也，导致所有群发都失效
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @param cb 回调
         */
        public void deleteSend(final String accessToken, final Long id, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return deleteSend(accessToken, id);
                }
            });
        }

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
        public Boolean deleteSend(String accessToken, Long id){
            String url = DELETE_SEND + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("msg_id", id);

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param id 群发消息ID
         * @return 群发消息状态，或抛WechatException
         */
        public String getSend(Long id){
            return getSend(loadAccessToken(), id);
        }

        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param id 群发消息ID
         * @param cb 回调
         */
        public void getSend(final Long id, Callback<String> cb){
            getSend(loadAccessToken(), id, cb);
        }

        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @param cb 回调
         */
        public void getSend(final String accessToken, final Long id, Callback<String> cb){
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() {
                    return getSend(accessToken, id);
                }
            });
        }

        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @return 群发消息状态，或抛WechatException
         */
        public String getSend(String accessToken, Long id){
            String url = GET_SEND + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("msg_id", id);

            Map<String, Object> resp = doPost(url, params);
            return (String)resp.get("msg_status");
        }
    }

    /**
     * 二维码API
     */
    public final class QrCodes {

        /**
         * 获取Ticket
         */
        private final String TICKET_GET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

        /**
         * 显示二维码链接
         */
        private final String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

        /**
         * 将原长链接通过此接口转成短链接
         */
        private final String LONG_TO_SHORT = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";

        private QrCodes(){}

        /**
         * 获取临时二维码
         * @param sceneId 业务场景ID，32位非0整型
         * @param expire 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
         * @return 临时二维码链接，或抛WechatException
         */
        public String getTempQrcode(String sceneId, Integer expire){
            return getTempQrcode(loadAccessToken(), sceneId, expire);
        }

        /**
         * 获取临时二维码
         * @param sceneId 业务场景ID，32位非0整型
         * @param expire 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
         * @param cb 回调
         */
        public void getTempQrcode(final String sceneId, final Integer expire, Callback<String> cb){
            getTempQrcode(loadAccessToken(), sceneId, expire, cb);
        }

        /**
         * 获取临时二维码
         * @param accessToken accessToken
         * @param sceneId 业务场景ID，32位非0整型
         * @param expire 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
         * @param cb 回调
         */
        public void getTempQrcode(final String accessToken, final String sceneId, final Integer expire, Callback<String> cb){
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() {
                    return getTempQrcode(accessToken, sceneId, expire);
                }
            });
        }

        /**
         * 获取临时二维码
         * @param accessToken accessToken
         * @param sceneId 业务场景ID，32位非0整型
         * @param expire 该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
         * @return 临时二维码链接，或抛WechatException
         */
        public String getTempQrcode(String accessToken, String sceneId, Integer expire){
            String url = TICKET_GET + accessToken;

            Map<String, Object> params = buildQrcodeParams(sceneId, QrcodeType.QR_SCENE);
            params.put("expire_seconds", expire);

            Map<String, Object> resp = doPost(url, params);
            Qrcode qr = Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), Qrcode.class);
            return showQrcode(qr.getTicket());
        }

        /**
         * 获取永久二维码
         * @param sceneId 业务场景ID，最大值为100000（目前参数只支持1--100000）
         * @return 永久二维码链接，或抛WechatException
         */
        public String getPermQrcode(String sceneId){
            return getPermQrcode(loadAccessToken(), sceneId);
        }

        /**
         * 获取永久二维码
         * @param sceneId 业务场景ID，最大值为100000（目前参数只支持1--100000）
         * @param cb 回调
         */
        public void getPermQrcode(final String sceneId, Callback<String> cb){
            getPermQrcode(loadAccessToken(), sceneId, cb);
        }

        /**
         * 获取永久二维码
         * @param accessToken accessToken
         * @param sceneId 业务场景ID，最大值为100000（目前参数只支持1--100000）
         * @param cb 回调
         */
        public void getPermQrcode(final String accessToken, final String sceneId, Callback<String> cb){
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() {
                    return getPermQrcode(accessToken, sceneId);
                }
            });
        }

        /**
         * 获取永久二维码
         * @param accessToken accessToken
         * @param sceneId 业务场景ID，最大值为100000（目前参数只支持1--100000）
         * @return 永久二维码链接，或抛WechatException
         */
        public String getPermQrcode(String accessToken, String sceneId){
            String url = TICKET_GET + accessToken;

            Map<String, Object> params = buildQrcodeParams(sceneId, QrcodeType.QR_LIMIT_SCENE);

            Map<String, Object> resp = doPost(url, params);
            Qrcode qr = Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), Qrcode.class);

            return showQrcode(qr.getTicket());
        }

        private Map<String, Object> buildQrcodeParams(String sceneId, QrcodeType type) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("action_name", type.value());

            Map<String, Object> sceneIdMap = Maps.newHashMapWithExpectedSize(1);
            sceneIdMap.put("scene_id", sceneId);

            Map<String, Object> scene = Maps.newHashMapWithExpectedSize(1);
            scene.put("scene", sceneIdMap);

            params.put("action_info", scene);
            return params;
        }

        /**
         * 获取二维码链接
         * @param ticket 二维码的ticket
         * @return 二维码链接，或抛WechatException
         */
        private String showQrcode(String ticket){
            try {
                return SHOW_QRCODE + URLEncoder.encode(ticket, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param longUrl 长链接
         * @return 短链接，或抛WechatException
         */
        public String shortUrl(String longUrl){
            return shortUrl(loadAccessToken(), longUrl);
        }

        /**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param longUrl 长链接
         * @param cb 回调
         */
        public void shortUrl(final String longUrl, Callback<String> cb){
            shortUrl(longUrl, longUrl, cb);
        }

        /**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param accessToken accessToken
         * @param longUrl 长链接
         * @param cb 回调
         */
        public void shortUrl(final String accessToken, final String longUrl, Callback<String> cb){
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() {
                    return shortUrl(accessToken, longUrl);
                }
            });
        }

        /**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param accessToken accessToken
         * @param longUrl 长链接
         * @return 短链接，或抛WechatException
         */
        public String shortUrl(String accessToken, String longUrl){
            String url = LONG_TO_SHORT + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("action", "long2short");
            params.put("long_url", longUrl);

            Map<String, Object> resp = doPost(url, params);
            return (String)resp.get("short_url");
        }
    }

    /**
     * 素材API
     */
    public final class Materials {

        /**
         * 素材总数
         */
        private final String COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=";

        /**
         * 素材列表
         */
        private final String GETS = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";

        /**
         * 删除永久素材
         */
        private final String DELETE = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";

        /**
         * 临时素材上传
         */
        private final String UPLOAD_TEMP = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=";

        /**
         * 临时素材下载
         */
        private final String DOWNLOAD_TEMP = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=";

        /**
         * 添加永久图文素材
         */
        private final String ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";

        /**
         * 更新永久图文素材
         */
        private final String UPDATE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=";

        /**
         * 上传永久图文素材内容中引用的图片
         */
        private final String UPLOAD_NEWS_IMAGE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=";

        /**
         * 上传永久素材(图片，语音，视频)
         */
        private final String UPLOAD_PERM = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";

        private Materials(){}

        /**
         * 获取素材总数统计
         * @return 素材总数统计
         */
        public MaterialCount count(){
            return count(loadAccessToken());
        }

        /**
         * 获取素材总数统计
         * @param cb 回调
         */
        public void count(Callback<MaterialCount> cb){
            count(loadAccessToken(), cb);
        }

        /**
         * 获取素材总数统计
         * @param accessToken accessToken
         * @param cb 回调
         */
        public void count(final String accessToken, Callback<MaterialCount> cb){
            doAsync(new AsyncFunction<MaterialCount>(cb) {
                @Override
                public MaterialCount execute() {
                    return count(accessToken);
                }
            });
        }

        /**
         * 获取素材总数统计
         * @param accessToken accessToken
         * @return 素材总数统计，或抛WechatException
         */
        public MaterialCount count(String accessToken){
            String url = COUNT + accessToken;

            Map<String, Object> resp = doGet(url);

            return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), MaterialCount.class);
        }

        /**
         * 获取素材列表
         * @param type 素材类型
         * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
         * @param count 返回素材的数量，取值在1到20之间
         * @param <T> Material范型
         * @return 素材分页对象，或抛WechatException
         */
        public <T> Page<T> gets(MaterialType type, Integer offset, Integer count){
            return gets(loadAccessToken(), type, offset, count);
        }

        /**
         * 获取素材列表
         * @param type 素材类型
         * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
         * @param count 返回素材的数量，取值在1到20之间
         * @param <T> Material范型
         * @param cb 回调
         */
        public <T> void gets(final MaterialType type, final Integer offset, final Integer count, Callback<Page<T>> cb){
            gets(loadAccessToken(), type, offset, count, cb);
        }

        /**
         * 获取素材列表
         * @param accessToken accessToken
         * @param type 素材类型
         * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
         * @param count 返回素材的数量，取值在1到20之间
         * @param <T> Material范型
         * @param cb 回调
         */
        public <T> void gets(final String accessToken, final MaterialType type, final Integer offset, final Integer count, Callback<Page<T>> cb){
            doAsync(new AsyncFunction<Page<T>>(cb) {
                @Override
                public Page<T> execute() {
                    return gets(accessToken, type, offset, count);
                }
            });
        }

        /**
         * 获取素材列表
         * @param accessToken accessToken
         * @param type 素材类型
         * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
         * @param count 返回素材的数量，取值在1到20之间
         * @param <T> Material范型
         * @return 素材分页对象，或抛WechatException
         */
        public <T> Page<T> gets(String accessToken, MaterialType type, Integer offset, Integer count){
            String url = GETS + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("type", type.value());
            params.put("offset", offset);
            params.put("count", count);

            Map<String, Object> resp = doPost(url, params);
            return renderMaterialPage(type, resp);
        }

        private <T> Page<T> renderMaterialPage(MaterialType type, Map<String, Object> resp) {
            Integer itemCount = (Integer)resp.get("item_count");
            if (itemCount == null || itemCount <= 0){
                return Page.empty();
            }

            Integer itemTotal = (Integer)resp.get("total_count");

            JavaType materialType = MaterialType.NEWS == type ?
                    Types.ARRAY_LIST_NEWS_MATERIAL_TYPE :Types.ARRAY_LIST_COMMON_MATERIAL_TYPE ;
            List<T> materials = Jsons.DEFAULT.fromJson(
                    Jsons.DEFAULT.toJson(resp.get("item")), materialType);

            return new Page<>(itemTotal, materials);
        }

        /**
         * 删除永久素材
         * @param mediaId 永久素材mediaId
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean delete(String mediaId){
            return delete(loadAccessToken(), mediaId);
        }

        /**
         * 删除永久素材
         * @param mediaId 永久素材mediaId
         * @param cb 回调
         */
        public void delete(final String mediaId, Callback<Boolean> cb){
            delete(loadAccessToken(), mediaId, cb);
        }

        /**
         * 删除永久素材
         * @param accessToken accessToken
         * @param mediaId 永久素材mediaId
         * @param cb 回调
         */
        public void delete(final String accessToken, final String mediaId, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return delete(accessToken, mediaId);
                }
            });
        }

        /**
         * 删除永久素材
         * @param accessToken accessToken
         * @param mediaId 永久素材mediaId
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean delete(String accessToken, String mediaId){
            String url = DELETE + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("media_id", mediaId);
            doPost(url, params);

            return Boolean.TRUE;
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param type 文件类型
         * @param fileName 文件名
         * @param fileData 文件数据
         * @return TempMaterial对象，或抛WechatException
         */
        public TempMaterial uploadTemp(MaterialUploadType type, String fileName, byte[] fileData) {
            return uploadTemp(loadAccessToken(), type, fileName, new ByteArrayInputStream(fileData));
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param accessToken accessToken
         * @param type 文件类型
         * @param fileName 文件名
         * @param fileData 文件数据
         * @return TempMaterial对象，或抛WechatException
         */
        public TempMaterial uploadTemp(String accessToken, MaterialUploadType type, String fileName, byte[] fileData) {
            return uploadTemp(accessToken, type, fileName, new ByteArrayInputStream(fileData));
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param type 文件类型
         * @param media 媒体文件输入流
         * @return TempMaterial对象，或抛WechatException
         */
        public TempMaterial uploadTemp(MaterialUploadType type, File media) {
            return uploadTemp(loadAccessToken(), type, media);
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param accessToken accessToken
         * @param type 文件类型
         * @param media 媒体文件输入流
         * @return TempMaterial对象，或抛WechatException
         */
        public TempMaterial uploadTemp(String accessToken, MaterialUploadType type, File media) {
            try {
                return uploadTemp(accessToken, type, media.getName(), new FileInputStream(media));
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param type 文件类型
         * @param media 媒体文件输入流
         * @param cb 回调
         */
        public void uploadTemp( MaterialUploadType type, File media, Callback<TempMaterial> cb) {
            try {
                uploadTemp(loadAccessToken(), type, media.getName(), new FileInputStream(media), cb);
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param accessToken accessToken
         * @param type 文件类型
         * @param media 媒体文件输入流
         * @param cb 回调
         */
        public void uploadTemp(String accessToken, MaterialUploadType type, File media, Callback<TempMaterial> cb) {
            try {
                uploadTemp(accessToken, type, media.getName(), new FileInputStream(media), cb);
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param type 文件类型
         * @param input 输入流
         * @return TempMaterial对象，或抛WechatException
         */
        public TempMaterial uploadTemp(MaterialUploadType type, String fileName, InputStream input) {
            return uploadTemp(loadAccessToken(), type, fileName, input);
        }

        /**
         * 上传临时素材:
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             视频（video）：10MB，支持MP4格式
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
             媒体文件在后台保存时间为3天，即3天后media_id失效。
         * @param type 文件类型
         * @param input 输入流
         * @param cb 回调
         */
        public void uploadTemp(final MaterialUploadType type, final String fileName, final InputStream input, Callback<TempMaterial> cb) {
            uploadTemp(loadAccessToken(), type, fileName, input, cb);
        }

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
         * @param cb 回调
         */
        public void uploadTemp(final String accessToken, final MaterialUploadType type, final String fileName, final InputStream input, Callback<TempMaterial> cb) {
            doAsync(new AsyncFunction<TempMaterial>(cb) {
                @Override
                public TempMaterial execute() {
                    return uploadTemp(accessToken, type, fileName, input);
                }
            });
        }

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
        public TempMaterial uploadTemp(String accessToken, MaterialUploadType type, String fileName, InputStream input) {
            String url = UPLOAD_TEMP + accessToken;

            Map<String, String> params = Maps.newHashMapWithExpectedSize(1);
            params.put("type", type.value());

            Map<String, Object> resp = doUpload(url, "media", fileName, input, params);
            return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), TempMaterial.class);
        }

        /**
         * 下载临时素材
         * @param mediaId mediaId
         * @return 文件二进制数据
         */
        public byte[] downloadTemp(String mediaId){
            return downloadTemp(loadAccessToken(), mediaId);
        }

        /**
         * 下载临时素材
         * @param mediaId mediaId
         * @param cb 回调
         */
        public void downloadTemp(final String mediaId, Callback<byte[]> cb){
            downloadTemp(loadAccessToken(), mediaId, cb);
        }

        /**
         * 下载临时素材
         * @param accessToken accessToken
         * @param mediaId mediaId
         * @param cb 回调
         */
        public void downloadTemp(final String accessToken, final String mediaId, Callback<byte[]> cb){
            doAsync(new AsyncFunction<byte[]>(cb) {
                @Override
                public byte[] execute() {
                    return downloadTemp(accessToken, mediaId);
                }
            });
        }

        /**
         * 下载临时素材
         * @param accessToken accessToken
         * @param mediaId mediaId
         * @return 文件二进制数据
         */
        public byte[] downloadTemp(String accessToken, String mediaId){
            String url = DOWNLOAD_TEMP + accessToken + "&media_id=" + mediaId;

            ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
            Http.download(url, output);

            return output.toByteArray();
        }

        /**
         * 添加永久图文素材
         * @param items 图文素材列表
         * @return mediaId
         */
        public String uploadPermNews(List<NewsContentItem> items){
            return uploadPermNews(loadAccessToken(), items);
        }

        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param items 图文素材列表
         * @param cb 回调
         */
        public void uploadPermNews(final List<NewsContentItem> items, Callback<String> cb){
            uploadPermNews(loadAccessToken(), items, cb);
        }

        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param accessToken accessToken
         * @param items 图文素材列表
         * @param cb 回调
         */
        public void uploadPermNews(final String accessToken, final List<NewsContentItem> items, Callback<String> cb){
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() {
                    return uploadPermNews(accessToken, items);
                }
            });
        }

        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param accessToken accessToken
         * @param items 图文素材列表
         * @return mediaId
         */
        public String uploadPermNews(String accessToken, List<NewsContentItem> items){
            String url = ADD_NEWS + accessToken;
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("articles", items);
            Map<String, Object> resp = doPost(url, params);
            return (String)resp.get("media_id");
        }

        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param mediaId 图文mediaId
         * @param itemIndex 对应图文素材中的第几个图文项，从0开始
         * @param newItem 新的图文项
         * @param cb 回调
         */
        public void updatePermNews(final String mediaId, final Integer itemIndex, final NewsContentItem newItem, Callback<Boolean> cb){
            updatePermNews(loadAccessToken(), mediaId, itemIndex, newItem, cb);
        }

        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param accessToken accessToken
         * @param mediaId 图文mediaId
         * @param itemIndex 对应图文素材中的第几个图文项，从0开始
         * @param newItem 新的图文项
         * @param cb 回调
         */
        public void updatePermNews(final String accessToken, final String mediaId, final Integer itemIndex, final NewsContentItem newItem, Callback<Boolean> cb){
            doAsync(new AsyncFunction<Boolean>(cb) {
                @Override
                public Boolean execute() {
                    return updatePermNews(accessToken, mediaId, itemIndex, newItem);
                }
            });
        }

        /**
         * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
         * @param accessToken accessToken
         * @param mediaId 图文mediaId
         * @param itemIndex 对应图文素材中的第几个图文项，从0开始
         * @param newItem 新的图文项
         * @return 更新成功返回true，反之false
         */
        public Boolean updatePermNews(String accessToken, String mediaId, Integer itemIndex, NewsContentItem newItem){
            String url = UPDATE_NEWS + accessToken;

            Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
            params.put("media_id", mediaId);
            params.put("index", itemIndex);
            params.put("articles", newItem);

            doPost(url, params);
            return Boolean.TRUE;
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param image 图片对象
         * @return 微信内部图片链接
         */
        public String uploadPermNewsImage(String accessToken, File image) {
            try {
                return uploadPermNewsImage(accessToken, image.getName(), new FileInputStream(image));
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param image 图片对象
         * @param cb 回调
         */
        public void uploadPermNewsImage(File image, Callback<String> cb) {
            uploadPermNewsImage(loadAccessToken(), image, cb);
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param image 图片对象
         * @param cb 回调
         */
        public void uploadPermNewsImage(String accessToken, File image, Callback<String> cb) {
            try {
                uploadPermNewsImage(accessToken, image.getName(), new FileInputStream(image), cb);
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param data 文件二机制数据
         * @return 微信内部图片链接
         */
        public String uploadPermNewsImage(String accessToken, String fileName, byte[] data) {
            return uploadPermNewsImage(accessToken, fileName, new ByteArrayInputStream(data));
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param fileName 文件名
         * @param data 文件二机制数据
         * @param cb 回调
         */
        public void uploadPermNewsImage(String fileName, byte[] data, Callback<String> cb) {
            uploadPermNewsImage(loadAccessToken(), fileName, new ByteArrayInputStream(data), cb);
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param data 文件二机制数据
         * @param cb 回调
         */
        public void uploadPermNewsImage(String accessToken, String fileName, byte[] data, Callback<String> cb) {
            uploadPermNewsImage(accessToken, fileName, new ByteArrayInputStream(data), cb);
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param fileName 文件名
         * @param in 文件输入流
         * @param cb 回调
         */
        public void uploadPermNewsImage(final String fileName, final InputStream in, Callback<String> cb){
            uploadPermNewsImage(loadAccessToken(), fileName, in, cb);
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param in 文件输入流
         * @param cb 回调
         */
        public void uploadPermNewsImage(final String accessToken, final String fileName, final InputStream in, Callback<String> cb) {
            doAsync(new AsyncFunction<String>(cb) {
                @Override
                public String execute() throws FileNotFoundException {
                    return uploadPermNewsImage(accessToken, fileName, in);
                }
            });
        }

        /**
         * 上传永久图文素材内容中引用的图片
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param in 文件输入流
         * @return 微信内部图片链接
         */
        public String uploadPermNewsImage(String accessToken, String fileName, InputStream in) {
            String url = UPLOAD_NEWS_IMAGE + accessToken;
            Map<String, Object> resp = doUpload(url, "media", fileName, in, Collections.<String, String>emptyMap());
            return (String)resp.get("url");
        }

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
        public PermMaterial uploadPerm(String accessToken, MaterialUploadType type, File file) {
            try {
                return uploadPerm(accessToken, type, file.getName(), new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param accessToken accessToken
         * @param type 文件类型
         * @param fileName 文件名
         * @param data 文件二进制数据
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPerm(String accessToken, MaterialUploadType type, String fileName, byte[] data) {
            return uploadPerm(accessToken, type, fileName, new ByteArrayInputStream(data));
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param accessToken accessToken
         * @param type 文件类型
         * @param fileName 文件名
         * @param data 文件二进制数据
         * @param cb 回调
         */
        public void uploadPerm(String accessToken, MaterialUploadType type, String fileName, byte[] data, Callback<PermMaterial> cb) {
             uploadPerm(accessToken, type, fileName, new ByteArrayInputStream(data), cb);
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
         永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
         图片（image）: 1M，bmp/png/jpeg/jpg/gif
         语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
         缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param type 文件类型
         * @param fileName 文件名
         * @param data 文件二进制数据
         * @param cb 回调
         */
        public void uploadPerm(MaterialUploadType type, String fileName, byte[] data, Callback<PermMaterial> cb) {
            uploadPerm(loadAccessToken(), type, fileName, new ByteArrayInputStream(data), cb);
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
         永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
         图片（image）: 1M，bmp/png/jpeg/jpg/gif
         语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
         缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param type 文件类型
         * @param fileName 文件名
         * @param data 文件二进制数据
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPerm( MaterialUploadType type, String fileName, byte[] data) {
            return uploadPerm(loadAccessToken(), type, fileName, new ByteArrayInputStream(data));
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param type 文件类型
         * @param file 输入流
         * @param cb 回调
         */
        public void uploadPerm(final MaterialUploadType type, final File file, Callback<PermMaterial> cb) {
            uploadPerm(loadAccessToken(), type, file, cb);
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param accessToken accessToken
         * @param type 文件类型
         * @param file 输入流
         * @param cb 回调
         */
        public void uploadPerm(String accessToken, final MaterialUploadType type, final File file, Callback<PermMaterial> cb) {
            try {
                uploadPerm(accessToken, type, file.getName(), new FileInputStream(file), cb);
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param type 文件类型
         * @param input 输入流
         * @param cb 回调
         */
        public void uploadPerm(final MaterialUploadType type, final String fileName, final InputStream input, Callback<PermMaterial> cb) {
            uploadPerm(loadAccessToken(), type, fileName, input, cb);
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
             图片（image）: 1M，bmp/png/jpeg/jpg/gif
             语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
             缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param accessToken accessToken
         * @param type 文件类型
         * @param input 输入流
         * @param cb 回调
         */
        public void uploadPerm(final String accessToken, final MaterialUploadType type, final String fileName, final InputStream input, Callback<PermMaterial> cb) {
            doAsync(new AsyncFunction<PermMaterial>(cb) {
                @Override
                public PermMaterial execute() throws Exception {
                    return uploadPerm(accessToken, type, fileName, input);
                }
            });
        }

        /**
         * 上传永久(图片，语音，缩略图)素材
             永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
                 图片（image）: 1M，bmp/png/jpeg/jpg/gif
                 语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
                 缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
         * @param accessToken accessToken
         * @param type 文件类型
         * @param input 输入流
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPerm(String accessToken, MaterialUploadType type, String fileName, InputStream input) {
            if (MaterialUploadType.VIDEO == type){
                throw new IllegalArgumentException("type must be image, voice, or thumb, you should use uploadPermVideo method.");
            }
            String url = UPLOAD_PERM + accessToken;

            Map<String, String> params = Maps.newHashMapWithExpectedSize(1);
            params.put("type", type.value());

            Map<String, Object> resp = doUpload(url, "media", fileName, input, params);
            return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), PermMaterial.class);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param video 视频文件
         * @param title 标题
         * @param desc 描述
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPermVideo(String accessToken, File video, String title, String desc) throws FileNotFoundException {
            return uploadPermVideo(accessToken, video.getName(), new FileInputStream(video), title, desc);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param video 视频文件
         * @param title 标题
         * @param desc 描述
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPermVideo(File video, String title, String desc) throws FileNotFoundException {
            return uploadPermVideo(loadAccessToken(), video.getName(), new FileInputStream(video), title, desc);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param data 二进制数据
         * @param title 标题
         * @param desc 描述
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPermVideo(String accessToken, String fileName, byte[] data, String title, String desc) throws FileNotFoundException {
            return uploadPermVideo(accessToken, fileName, new ByteArrayInputStream(data), title, desc);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param fileName 文件名
         * @param data 二进制数据
         * @param title 标题
         * @param desc 描述
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPermVideo(String fileName, byte[] data, String title, String desc) throws FileNotFoundException {
            return uploadPermVideo(loadAccessToken(), fileName, new ByteArrayInputStream(data), title, desc);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param data 二进制数据
         * @param title 标题
         * @param desc 描述
         * @param cb 回调
         */
        public void uploadPermVideo(String accessToken, String fileName, byte[] data, final String title, final String desc, Callback<PermMaterial> cb) {
            uploadPermVideo(accessToken, fileName, new ByteArrayInputStream(data), title, desc, cb);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param fileName 文件名
         * @param data 二进制数据
         * @param title 标题
         * @param desc 描述
         * @param cb 回调
         */
        public void uploadPermVideo(String fileName, byte[] data, final String title, final String desc, Callback<PermMaterial> cb) {
            uploadPermVideo(loadAccessToken(), fileName, new ByteArrayInputStream(data), title, desc, cb);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param video 文件
         * @param title 标题
         * @param desc 描述
         * @param cb 回调
         */
        public void uploadPermVideo(final File video, final String title, final String desc, Callback<PermMaterial> cb) {
            uploadPermVideo(loadAccessToken(), video, title, desc, cb);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param video 文件
         * @param title 标题
         * @param desc 描述
         * @param cb 回调
         */
        public void uploadPermVideo(final String accessToken, final File video, final String title, final String desc, Callback<PermMaterial> cb) {
            try {
                uploadPermVideo(accessToken, video.getName(), new FileInputStream(video), title, desc, cb);
            } catch (FileNotFoundException e) {
                throw new WechatException(e);
            }
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param fileName 文件名
         * @param input 输入流
         * @param title 标题
         * @param desc 描述
         * @param cb 回调
         */
        public void uploadPermVideo(final String fileName, final InputStream input, final String title, final String desc, Callback<PermMaterial> cb) {
            uploadPermVideo(loadAccessToken(), fileName, input, title, desc, cb);
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param input 输入流
         * @param title 标题
         * @param desc 描述
         * @param cb 回调
         */
        public void uploadPermVideo(final String accessToken, final String fileName, final InputStream input, final String title, final String desc, Callback<PermMaterial> cb) {
            doAsync(new AsyncFunction<PermMaterial>(cb) {
                @Override
                public PermMaterial execute() throws Exception {
                    return uploadPermVideo(accessToken, fileName, input, title, desc);
                }
            });
        }

        /**
         * 上传永久视频素材(10M大小)
         * @param accessToken accessToken
         * @param fileName 文件名
         * @param input 输入流
         * @param title 标题
         * @param desc 描述
         * @return PermMaterial对象，或抛WechatException
         */
        public PermMaterial uploadPermVideo(String accessToken, String fileName, InputStream input, String title, String desc) {

            String url = UPLOAD_PERM + accessToken;

            Map<String, String> params = Maps.newHashMapWithExpectedSize(2);
            params.put("type", MaterialUploadType.VIDEO.value());

            Map<String, String> description = Maps.newHashMapWithExpectedSize(2);
            description.put("title", title);
            description.put("introduction", desc);
            params.put("description", Jsons.DEFAULT.toJson(description));

            Map<String, Object> resp = doUpload(url, "media", fileName, input, params);
            return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), PermMaterial.class);
        }
    }

    /**
     * 数据统计
     */
    public final class Datas {

        private Datas(){}

    }

    /**
     * JS SDK
     */
    public final class JsSdks {

        /**
         * 获取Ticket
         */
        private final String TICKET_GET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";

        private JsSdks(){}

        /**
         * 获取临时凭证
         * @param type 凭证类型
         *             @see me.hao0.wechat.model.js.TicketType
         * @param cb 回调
         */
        public void getTicket(final TicketType type, final Callback<Ticket> cb){
            getTicket(loadAccessToken(), type, cb);
        }

        /**
         * 获取临时凭证
         * @param type 凭证类型
         *             @see me.hao0.wechat.model.js.TicketType
         * @return Ticket对象，或抛WechatException
         */
        public Ticket getTicket(TicketType type){
            return getTicket(loadAccessToken(), type);
        }

        /**
         * 获取临时凭证
         * @param accessToken accessToken
         * @param type 凭证类型
         *             @see me.hao0.wechat.model.js.TicketType
         * @param cb 回调
         */
        public void getTicket(final String accessToken, final TicketType type, final Callback<Ticket> cb){
            doAsync(new AsyncFunction<Ticket>(cb) {
                @Override
                public Ticket execute() {
                    return getTicket(accessToken, type);
                }
            });
        }

        /**
         * 获取临时凭证
         * @param accessToken accessToken
         * @param type 凭证类型
         *             @see me.hao0.wechat.model.js.TicketType
         * @return Ticket对象，或抛WechatException
         */
        public Ticket getTicket(String accessToken, TicketType type){
            String url = TICKET_GET + accessToken + "&type=" + type.type();

            Map<String, Object> resp = doGet(url);

            Ticket t = new Ticket();
            t.setTicket((String)resp.get("ticket"));
            Integer expire = (Integer)resp.get("expires_in");
            t.setExpire(expire);
            t.setExpireAt(System.currentTimeMillis() + expire * 1000);
            t.setType(type);

            return t;
        }

        /**
         * 获取JSSDK配置信息
         * @param nonStr 随机字符串
         * @param url 调用JSSDK的页面URL全路径(去除#后的)
         * @return Config对象
         */
        public Config getConfig(String nonStr, String url){
            return getConfig(loadTicket(TicketType.JSAPI), nonStr, url);
        }

        /**
         * 获取JSSDK配置信息
         * @param jsApiTicket jsapi凭证
         * @param nonStr 随机字符串
         * @param url 调用JSSDK的页面URL全路径(去除#后的)
         * @return Config对象
         */
        public Config getConfig(String jsApiTicket, String nonStr, String url){
            return getConfig(jsApiTicket, nonStr, System.currentTimeMillis() / 1000, url);
        }

        /**
         * 获取JSSDK配置信息
         * @param nonStr 随机字符串
         * @param timestamp 时间戳(s)
         * @param url 调用JSSDK的页面URL全路径(去除#后的)
         * @return Config对象
         */
        public Config getConfig(String nonStr, Long timestamp, String url){
            return getConfig(loadTicket(TicketType.JSAPI), nonStr, timestamp, url);
        }

        /**
         * 获取JSSDK调用前的配置信息
         * @param jsApiTicket jsapi凭证
         * @param nonStr 随机字符串
         * @param timestamp 时间戳(s)
         * @param url 调用JSSDK的页面URL全路径(去除#后的)
         * @return Config对象
         */
        public Config getConfig(String jsApiTicket, String nonStr, Long timestamp, String url){
            String signStr = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";
            signStr = String.format(signStr, jsApiTicket, nonStr, timestamp, url);
            String sign = Hashing.sha1().hashString(signStr, Charsets.UTF_8).toString().toLowerCase();
            return new Config(appId, timestamp, nonStr, sign);
        }
    }

    /**
     * 关闭异步执行器(不再支持异步执行)
     */
    public void stop(){
        if (executor.isShutdown()){
            executor.shutdown();
        }
    }

    private String loadAccessToken(){
        String accessToken = tokenLoader.get();
        if (Strings.isNullOrEmpty(accessToken)){
            AccessToken token = BASE.accessToken();
            tokenLoader.refresh(token);
            accessToken = token.getAccessToken();
        }
        return accessToken;
    }

    private String loadTicket(TicketType type){
        String ticket = ticketLoader.get(type);
        if (Strings.isNullOrEmpty(ticket)){
            Ticket t = JSSDK.getTicket(type);
            ticketLoader.refresh(t);
            ticket = t.getTicket();
        }
        return ticket;
    }

    private Map<String, Object> doPost(String url, Map<String, Object> params) {
        Http http = Http.post(url);
        if (params != null && params.size() > 0){
            http.body(Jsons.DEFAULT.toJson(params));
        }
        Map<String, Object> resp = http.request(Types.MAP_STRING_OBJ_TYPE);
        Integer errcode = (Integer)resp.get(ERROR_CODE);
        if (errcode != null && errcode != 0){
            throw new WechatException(resp);
        }
        return resp;
    }

    private Map<String, Object> doGet(String url) {
        return doGet(url, null);
    }

    private Map<String, Object> doGet(String url, Map<String, Object> params) {
        Http http = Http.get(url);
        if (params != null && params.size() > 0){
            http.body(Jsons.DEFAULT.toJson(params));
        }
        Map<String, Object> resp = http.request(Types.MAP_STRING_OBJ_TYPE);
        Integer errcode = (Integer)resp.get(ERROR_CODE);
        if (errcode != null && errcode != 0){
            throw new WechatException(resp);
        }
        return resp;
    }

    private Map<String, Object> doUpload(String url, String fieldName, String fileName, InputStream input, Map<String, String> params){
        String json = Http.upload(url, fieldName, fileName, input, params);
        Map<String, Object> resp = Jsons.DEFAULT.fromJson(json, Types.MAP_STRING_OBJ_TYPE);
        Integer errcode = (Integer)resp.get(ERROR_CODE);
        if (errcode != null && errcode != 0){
            throw new WechatException(resp);
        }
        return resp;
    }

    private <T> void doAsync(final AsyncFunction<T> f){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    T res = f.execute();
                    f.cb.onSuccess(res);
                } catch (Exception e){
                    f.cb.onFailure(e);
                }
            }
        });
    }

    private void doAsync(final VoidAsyncFunction f){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    f.execute();
                } catch (Exception e){
                    f.cb.onFailure(e);
                }
            }
        });
    }

    private static abstract class AsyncFunction<T> {

        private Callback<T> cb;

        AsyncFunction(Callback<T> cb){
            this.cb = cb;
        }

        public abstract T execute() throws Exception;
    }

    private static abstract class VoidAsyncFunction {

        private VoidCallback cb;

        VoidAsyncFunction(VoidCallback cb){
            this.cb = cb;
        }

        public abstract void execute();
    }

    public static final class Types {

        static final JavaType MAP_STRING_OBJ_TYPE = Jsons.DEFAULT.createCollectionType(Map.class, String.class, Object.class);

        static final JavaType ARRAY_LIST_MENU_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Menu.class);

        static final JavaType ARRAY_LIST_GROUP_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Group.class);

        static final JavaType ARRAY_LIST_COMMON_MATERIAL_TYPE =
                Jsons.DEFAULT.createCollectionType(ArrayList.class, CommonMaterial.class);

        static final JavaType ARRAY_LIST_NEWS_MATERIAL_TYPE =
                Jsons.DEFAULT.createCollectionType(ArrayList.class, NewsMaterial.class);
    }
}
