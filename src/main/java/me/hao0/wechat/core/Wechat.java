package me.hao0.wechat.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.base.AccessToken;
import me.hao0.wechat.model.customer.WaitingSession;
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
import me.hao0.wechat.utils.MD5;
import me.hao0.wechat.utils.Jsons;
import me.hao0.wechat.utils.XmlReaders;
import me.hao0.wechat.utils.XmlWriters;
import me.hao0.wechat.utils.Http;
import me.hao0.wechat.utils.Strings;
import me.hao0.wechat.utils.Types;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public class Wechat {

    /**
     * 微信APP ID
     */
    private String appId;

    /**
     * 微信APP 密钥
     */
    private String appSecret;

    /**
     * AccessToken加载器
     */
    private final AccessTokenLoader tokenLoader;

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

    private final String ERROR_CODE = "errcode";

    private Wechat(String appId, String appSecret, AccessTokenLoader tokenLoader){
        if (tokenLoader == null){
            throw new NullPointerException("access token loader can't be null");
        }
        this.appId = appId;
        this.appSecret = appSecret;
        this.tokenLoader = tokenLoader;
    }

    public static Wechat newWechat(String appId, String appSecret){
        return newWechat(appId, appSecret, new DefaultAccessTokenLoader());
    }

    public static Wechat newWechat(String appId, String appSecret, AccessTokenLoader tokenLoader){
        return new Wechat(appId, appSecret, tokenLoader);
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
            StringBuilder url = new StringBuilder(AUTH_URL);
            url.append("appid=").append(appId)
                    .append("&redirect_uri=").append(redirectUrl)
                    .append("&response_type=code&scope=")
                    .append(quiet ? AuthType.BASE.scope : AuthType.USER_INFO.scope)
                    .append("&state=").append("1")
                    .append("#wechat_redirect");
            return url.toString();
        }

        /**
         * 获取用户openId
         * @param code 用户授权的code
         * @return 用户的openId，或抛WechatException
         */
        public String openId(String code){
            StringBuilder url = new StringBuilder(OPEN_ID_URL);
            url.append("appid=").append(appId)
                    .append("&secret=").append(appSecret)
                    .append("&code=").append(code)
                    .append("&grant_type=").append("authorization_code");
            Map<String, Object> resp = Http.get(url.toString())
                    .ssl().request(Types.MAP_STRING_OBJ_TYPE);
            if (resp.containsKey(ERROR_CODE)){
                throw new WechatException(resp);
            } else {
                return (String)resp.get("openid");
            }
        }

        /**
         * 获取accessToken(应该尽量临时保存一个地方，每隔一段时间来获取)
         * @return accessToken，或抛WechatException
         */
        public AccessToken accessToken(){
            StringBuilder url = new StringBuilder(ACCESS_TOKEN_URL);
            url.append("&appid=").append(appId)
               .append("&secret=").append(appSecret);
            Map<String, Object> resp = Http.get(url.toString())
                    .ssl().request(Types.MAP_STRING_OBJ_TYPE);
            if (resp.containsKey(ERROR_CODE)){
                throw new WechatException(resp);
            } else {
                return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), AccessToken.class);
            }
        }

        /**
         * 获取微信服务器IP列表
         * @return 微信服务器IP列表，或抛WechatException
         */
        public List<String> ip(){
            return ip(loadToken());
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
            Map<String, Object> resp = Http.get(url)
                    .ssl().request(Types.MAP_STRING_OBJ_TYPE);
            if (resp.containsKey(ERROR_CODE)){
                throw new WechatException(resp);
            } else {
                return (List<String>)resp.get("ip_list");
            }
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
            return createAccount(loadToken(), account, nickName, password);
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
            return updateAccount(loadToken(), account, nickName, password);
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
            Map<String, Object> params = new HashMap<>();
            params.put("kf_account", account);
            params.put("nickname", nickName);
            params.put("password", MD5.generate(password, false));
            Map<String, Object> resp = Http.post(url)
                                           .body(Jsons.DEFAULT.toJson(params))
                                           .request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 删除客服账户
         * @param kfAccount 客服登录帐号(包含域名)
         * @return 添加成功返回true，或抛WechatException
         */
        public Boolean deleteAccount(String kfAccount){
            return deleteAccount(loadToken(), kfAccount);
        }

        /**
         * 删除客服账户
         * @param accessToken accessToken
         * @param kfAccount 客服登录帐号(包含域名)
         * @return 添加成功返回true，或抛WechatException
         */
        public Boolean deleteAccount(String accessToken, String kfAccount){
            StringBuilder url = new StringBuilder(DELETE_ACCOUNT);
            url.append(accessToken).append("&kf_account=").append(kfAccount);
            Map<String, Object> resp = Http.get(url.toString())
                                    .request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
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
            return getMsgRecords(loadToken(), pageNo, pageSize, startTime, endTime);
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
            Map<String, Object> params = new HashMap<>();
            params.put("pageindex", pageNo);
            params.put("pagesize", pageSize);
            params.put("starttime", startTime.getTime());
            params.put("endtime", endTime.getTime());
            Map<String, Object> resp = Http.post(url)
                                            .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
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
            return createSession(loadToken(), openId, kfAccount, text);
        }

        /**
         * 创建会话(该客服必需在线)
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
            return closeSession(loadToken(), openId, kfAccount, text);
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
            Map<String, Object> params = new HashMap<>();
            params.put("openid", openId);
            params.put("kf_account", kfAccount);
            params.put("text", text);
            Map<String, Object> resp = Http.post(url)
                                .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 获取用户的会话状态
         * @param openId 用户openId
         * @return 客户的会话状态，或抛WechatException
         */
        public UserSession getUserSession(String openId){
            return getUserSession(loadToken(), openId);
        }

        /**
         * 获取用户的会话状态
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 客户的会话状态，或抛WechatException
         */
        public UserSession getUserSession(String accessToken, String openId){
            StringBuilder url = new StringBuilder(USER_SESSION_STATUS);
            url.append(accessToken).append("&openid=").append(openId);
            Map<String, Object> resp = Http.get(url.toString()).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
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
            return getCsSessions(loadToken(), kfAccount);
        }

        /**
         * 获取客服的会话列表
         * @param accessToken accessToken
         * @param kfAccount 客服帐号(包含域名)
         * @return 客服的会话列表，或抛WechatException
         */
        @SuppressWarnings("unchecked")
        public List<CsSession> getCsSessions(String accessToken, String kfAccount){
            StringBuilder url = new StringBuilder(CS_SESSION_STATUS);
            url.append(accessToken).append("&kf_account=").append(kfAccount);
            Map<String, Object> resp = Http.get(url.toString()).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
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
            return getWaitingSessions(loadToken());
        }

        /**
         * 获取未接入的会话列表
         * @param accessToken accessToken
         * @return 未接入的会话列表，或抛WechatException
         */
        @SuppressWarnings("unchecked")
        public List<WaitingSession> getWaitingSessions(String accessToken){
            String url = WAITING_SESSION + accessToken;
            Map<String, Object> resp = Http.get(url).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
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
            return get(loadToken());
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
            return create(loadToken(), jsonMenu);
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
            return delete(loadToken());
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

        public List<Menu> getMenus() {
            return menus;
        }

        public void setMenus(List<Menu> menus) {
            this.menus = menus;
        }

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
            return createGroup(loadToken(), name);
        }

        /**
         * 创建用户分组
         * @param accessToken accessToken
         * @param name 名称
         * @return 分组ID，或抛WechatException
         */
        public Integer createGroup(String accessToken, String name){
            String url = CREATE_GROUP + accessToken;
            Map<String, Object> params = new HashMap<>();
            Group g = new Group();
            g.setName(name);
            params.put("group", g);
            Map<String, Object> resp =
                    Http.post(url).body(Jsons.EXCLUDE_EMPTY.toJson(params))
                        .request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return (Integer)((Map)resp.get("group")).get("id");
        }

        /**
         * 获取所有分组列表
         * @return 分组列表，或抛WechatException
         */
        public List<Group> getGroup(){
            return getGroup(loadToken());
        }

        /**
         * 获取所有分组列表
         * @param accessToken accessToken
         * @return 分组列表，或抛WechatException
         */
        public List<Group> getGroup(String accessToken){
            String url = GET_GROUP + accessToken;
            Map<String, Object> resp =
                    Http.get(url).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return Jsons.EXCLUDE_DEFAULT
                        .fromJson(Jsons.DEFAULT.toJson(resp.get("groups")), Types.ARRAY_LIST_GROUP_TYPE);
        }

        /**
         * 删除分组
         * @param id 分组ID
         * @return 删除成功返回true，或抛WechatException
         */
        public Boolean deleteGroup(Integer id){
            return deleteGroup(loadToken(), id);
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
            Map<String, Object> params = new HashMap<>();
            params.put("group", g);
            Map<String, Object> resp =
                    Http.post(url).body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 更新分组名称
         * @param id 分组ID
         * @param newName 分组新名称
         * @return 更新成功返回true，或抛WechatException
         */
        public Boolean updateGroup(Integer id, String newName){
            return updateGroup(loadToken(), id, newName);
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
            Map<String, Object> params = new HashMap<>();
            params.put("group", g);
            Map<String, Object> resp =
                    Http.post(url).body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 获取用户所在组
         * @param openId 用户openId
         * @return 组ID，或抛WechatException
         */
        public Integer getUserGroup(String openId){
            return getUserGroup(loadToken(), openId);
        }

        /**
         * 获取用户所在组
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 组ID，或抛WechatException
         */
        public Integer getUserGroup(String accessToken, String openId){
            String url = GROUP_OF_USER + accessToken;
            Map<String, Object> params = new HashMap<>();
            params.put("openid", openId);
            Map<String, Object> resp =
                    Http.post(url).body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return (Integer)resp.get("groupid");
        }

        /**
         * 移动用户所在组
         * @param openId 用户openId
         * @param groupId 新组ID
         * @return 移动成功返回true，或抛WechatException
         */
        public Boolean mvUserGroup(String openId, Integer groupId){
            return mvUserGroup(loadToken(), openId, groupId);
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
            Map<String, Object> params = new HashMap<>();
            params.put("openid", openId);
            params.put("to_groupid", groupId);
            Map<String, Object> resp =
                    Http.post(url).body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        public User getUser(String openId){
            return getUser(loadToken(), openId);
        }

        /**
         * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        public User getUser(String accessToken, String openId){
            StringBuilder url = new StringBuilder(GET_USER_INFO);
            url.append(accessToken).append("&openid=").append(openId);
            Map<String, Object> resp =
                    Http.get(url.toString()).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), User.class);
        }

        /**
         * 备注用户
         * @param openId 用户openId
         * @param remark 备注
         * @return 备注成功返回true，或抛WechatException
         */
        public Boolean remarkUser(String openId, String remark){
            return remarkUser(loadToken(), openId, remark);
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
            Map<String, Object> params = new HashMap<>();
            params.put("openid", openId);
            params.put("remark", remark);
            Map<String, Object> resp = Http.post(url).body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
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
            return sendTemplate(loadToken(), openId, templateId, null, fields);
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
            return sendTemplate(loadToken(), openId, templateId, link, fields);
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

            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);

            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }

            return (Integer)resp.get("msgid");
        }

        private Map<String, Object> buildTemplateParams(String openId, String templateId, String link, List<TemplateField> fields) {
            Map<String, Object> params = new HashMap<>();
            params.put("touser", openId);
            params.put("template_id", templateId);
            if (!Strings.isNullOrEmpty(link)){
                params.put("url", link);
            }
            if (fields != null && !fields.isEmpty()){
                Map<String, Map<String, String>> data = new HashMap<>();
                Map<String, String> dataItem;
                for (TemplateField field : fields){
                    dataItem = new HashMap<>();
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
            return send(loadToken(), msg);
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

            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }

            return (Long)resp.get("msg_id");
        }

        private Map<String, Object> buildSendParams(SendMessage msg) {
            Map<String, Object> params = new HashMap<>();

            if (SendMessageScope.GROUP == msg.getScope()){
                Map<String, Object> scope = new HashMap<>();
                scope.put("is_to_all", msg.getIsToAll());
                scope.put("group_id", msg.getGroupId());
                params.put("filter", scope);
            } else {
                params.put("touser", msg.getOpenIds());
            }

            // send content
            Map<String, Object> msgContent = new HashMap<>();
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
            return previewSend(loadToken(), msg);
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

            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }

            return Boolean.TRUE;
        }

        private Map<String, Object> buildPreviewParams(SendPreviewMessage msg) {
            Map<String, Object> params = new HashMap<>();

            params.put("touser", msg.getOpenId());

            // send content
            Map<String, Object> msgContent = new HashMap<>();
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
            return deleteSend(loadToken(), id);
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
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", id);
            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return Boolean.TRUE;
        }

        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param id 群发消息ID
         * @return 群发消息状态，或抛WechatException
         */
        public String getSend(Long id){
            return getSend(loadToken(), id);
        }

        /**
         * 检查群发消息状态: 订阅号与服务号认证后均可用
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @return 群发消息状态，或抛WechatException
         */
        public String getSend(String accessToken, Long id){
            String url = GET_SEND + accessToken;
            Map<String, Object> params = new HashMap<>();
            params.put("msg_id", id);
            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
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
            return getTempQrcode(loadToken(), sceneId, expire);
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
            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            Qrcode qr = Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), Qrcode.class);
            return showQrcode(qr.getTicket());
        }

        /**
         * 获取永久二维码
         * @param sceneId 业务场景ID，最大值为100000（目前参数只支持1--100000）
         * @return 永久二维码链接，或抛WechatException
         */
        public String getPermQrcode(String sceneId){
            return getPermQrcode(loadToken(), sceneId);
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
            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            Qrcode qr = Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), Qrcode.class);
            return showQrcode(qr.getTicket());
        }

        private Map<String, Object> buildQrcodeParams(String sceneId, QrcodeType type) {
            Map<String, Object> params = new HashMap<>();
            params.put("action_name", type.value());
            Map<String, Object> sceneIdMap = new HashMap<>();
            sceneIdMap.put("scene_id", sceneId);
            Map<String, Object> scene = new HashMap<>();
            scene.put("scene", sceneIdMap);
            params.put("action_info", scene);
            return params;
        }

        /**
         * 获取二维码链接
         * @param ticket 二维码的ticket
         * @return 二维码链接
         */
        private String showQrcode(String ticket){
            return SHOW_QRCODE + URLEncoder.encode(ticket);
        }

        /**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param longUrl 长链接
         * @return 短链接，或抛WechatException
         */
        public String shortUrl(String longUrl){
            return shortUrl(loadToken(), longUrl);
        }

        /**
         * 将二维码长链接转换为端链接，生成二维码将大大提升扫码速度和成功率
         * @param accessToken accessToken
         * @param longUrl 长链接
         * @return 短链接，或抛WechatException
         */
        public String shortUrl(String accessToken, String longUrl){
            String url = LONG_TO_SHORT + accessToken;
            Map<String, Object> params = new HashMap<>();
            params.put("action", "long2short");
            params.put("long_url", longUrl);
            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.DEFAULT.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }
            return (String)resp.get("short_url");
        }
    }

    /**
     * 素材API
     */
    public final class Materials {

        private Materials(){}

    }

    private String loadToken(){
        String accessToken = tokenLoader.get();
        if (Strings.isNullOrEmpty(accessToken)){
            AccessToken token = BASE.accessToken();
            tokenLoader.refresh(token);
            accessToken = token.getAccessToken();
        }
        return accessToken;
    }
}
