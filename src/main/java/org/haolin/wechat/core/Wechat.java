package org.haolin.wechat.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.haolin.wechat.exception.WechatException;
import org.haolin.wechat.model.base.AuthType;
import org.haolin.wechat.model.customer.CsSession;
import org.haolin.wechat.model.customer.MsgRecord;
import org.haolin.wechat.model.customer.UserSession;
import org.haolin.wechat.model.customer.WaitingSession;
import org.haolin.wechat.model.menu.Menu;
import org.haolin.wechat.model.menu.MenuType;
import org.haolin.wechat.model.message.Article;
import org.haolin.wechat.model.message.SendMessage;
import org.haolin.wechat.model.message.SendMessageScope;
import org.haolin.wechat.model.message.SendMessageType;
import org.haolin.wechat.model.message.SendPreviewMessage;
import org.haolin.wechat.model.message.TemplateField;
import org.haolin.wechat.model.message.RespMessageType;
import org.haolin.wechat.model.user.Group;
import org.haolin.wechat.model.user.User;
import org.haolin.wechat.utils.MD5;
import org.haolin.wechat.utils.Jsons;
import org.haolin.wechat.utils.Xmls;
import org.haolin.wechat.utils.Http;
import org.haolin.wechat.utils.Strings;
import org.haolin.wechat.utils.Types;
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
     * 素材API
     */
    public final Materials MATERIAL = new Materials();

    private final String ERROR_CODE = "errcode";

    private Wechat(){}

    public static Wechat newWechat(String appId, String appSecret){
        Wechat w = new Wechat();
        w.appId = appId;
        w.appSecret = appSecret;
        return w;
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
         * @param quiet 是否静默: true: 仅获取openId，false: 获取openId和个人信息
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
         * 获取accessToken(失效为2小时，你因该尽量临时保存一个地方，每隔一段时间来获取)
         * @return accessToken，或抛WechatException
         */
        public String accessToken(){
            StringBuilder url = new StringBuilder(ACCESS_TOKEN_URL);
            url.append("&appid=").append(appId)
               .append("&secret=").append(appSecret);
            Map<String, Object> resp = Http.get(url.toString())
                    .ssl().request(Types.MAP_STRING_OBJ_TYPE);
            if (resp.containsKey(ERROR_CODE)){
                throw new WechatException(resp);
            } else {
                return (String)resp.get("access_token");
            }
        }

        /**
         * 获取微信服务器IP列表
         * @param accessToken accessToken
         *                  @see org.haolin.wechat.core.Wechat.Bases#accessToken()
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
            Xmls xmls = Xmls.create();

            xmls.element("ToUserName", openId)
                .element("FromUserName", appId)
                .element("CreateTime", System.currentTimeMillis() / 1000);

            if (!Strings.isNullOrEmpty(kfAccount)){
                xmls.element("TransInfo", "KfAccount", kfAccount);
            }
            xmls.element("MsgType", "transfer_customer_service");

            return xmls.build();
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
         * @param accessToken accessToken
         * @param openId 用户openId
         * @param groupId 组ID
         * @return 移动成功返回true，或抛WechatException
         */
        public Boolean moveUserGroup(String accessToken, String openId, Integer groupId){
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
         * @param accessToken accessToken
         * @param openId 用户openId
         * @return 用户信息，或抛WechatException
         */
        public User getUserInfo(String accessToken, String openId){
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
         * 构建XML文本消息，以响应微信服务器
         * @param openId 用户openId
         * @param content 消息内容
         * @return XML文本消息
         */
        public String responseText(String openId, String content){
            Xmls msg = responseCommonElements(openId, RespMessageType.TEXT);
            msg.element("Content", content);
            return msg.build();
        }

        /**
         * 构建XML图片消息，以响应微信服务器
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @return XML图片消息
         */
        public String responseImage(String openId, String mediaId){
            Xmls msg = responseCommonElements(openId, RespMessageType.IMAGE);
            msg.element("Image", "MediaId", mediaId);
            return msg.build();
        }

        /**
         * 构建XML语音消息，以响应微信服务器
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @return XML语音消息
         */
        public String responseVoice(String openId, String mediaId){
            Xmls msg = responseCommonElements(openId, RespMessageType.VOICE);
            msg.element("Voice", "MediaId", mediaId);
            return msg.build();
        }

        /**
         * 构建XML视频消息，以响应微信服务器
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @param title 标题
         * @param desc 描述
         * @return XML视频消息
         */
        public String responseVideo(String openId, String mediaId, String title, String desc){
            Xmls msg = responseCommonElements(openId, RespMessageType.VIDEO);
            msg.element("Video", "MediaId", mediaId, "Title", title, "Description", desc);
            return msg.build();
        }

        /**
         * 构建XML音乐消息，以响应微信服务器
         * @param openId 用户openId
         * @param mediaId 通过素材管理接口上传多媒体文件，得到的id
         * @param title 标题
         * @param desc 描述
         * @param url 音乐链接
         * @param hqUrl 高质量音乐链接，WIFI环境优先使用该链接播放音乐
         * @return XML音乐消息
         */
        public String responseMusic(String openId, String mediaId,
                                    String title, String desc, String url, String hqUrl){
            Xmls msg = responseCommonElements(openId, RespMessageType.MUSIC);
            msg.element("Music",
                    "Title", title,
                    "Description", desc,
                    "MusicURL", url,
                    "HQMusicUrl", hqUrl,
                    "ThumbMediaId", mediaId);
            return msg.build();
        }

        /**
         * 构建XML图文消息，以响应微信服务器
         * @param openId 用户openId
         * @param articles 图片消息对象列表，长度 <= 10
         * @return XML图文消息
         */
        public String responseNews(String openId, List<Article> articles){
            if (articles.size() > 10){
                articles = articles.subList(0, 10);
            }
            Xmls xmls = responseCommonElements(openId, RespMessageType.NEWS);
            xmls.element("ArticleCount", articles.size());
            List<Xmls.E> items = new ArrayList<>();
            Xmls.E item;
            for (Article article : articles){
                item = xmls.newElement("item",
                                        "Title", article.getTitle(),
                                        "Description", article.getDesc(),
                                        "PicUrl", article.getPicUrl(),
                                        "Url", article.getUrl());
                items.add(item);
            }
            xmls.element("Articles", items);
            return xmls.build();
        }

        private Xmls responseCommonElements(String openId, RespMessageType type) {
            Xmls xmls = Xmls.create();
            xmls.element("ToUserName", openId)
                .element("FromUserName", appId)
                .element("CreateTime", System.currentTimeMillis() / 1000)
                .element("MsgType", type.value());
            return xmls;
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
         *  @see org.haolin.wechat.model.message.SendMessageScope
         * @param accessToken accessToken
         * @param msg 消息
         * @return 消息ID，或抛WechatException
         */
        public Integer send(String accessToken, SendMessage msg){

            String url = (SendMessageScope.GROUP == msg.getScope() ? SEND_ALL : SEND) + accessToken;
            Map<String, Object> params = buildSendParams(msg);

            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }

            return (Integer)resp.get("msg_id");
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
         * @param accessToken accessToken
         * @param msg 预览消息
         * @return 消息ID，或抛WechatException
         */
        public Integer previewSend(String accessToken, SendPreviewMessage msg){
            String url = PREVIEW_SEND + accessToken;

            Map<String, Object> params = buildPreviewParams(msg);

            Map<String, Object> resp = Http.post(url)
                    .body(Jsons.EXCLUDE_EMPTY.toJson(params)).request(Types.MAP_STRING_OBJ_TYPE);
            Integer errcode = (Integer)resp.get(ERROR_CODE);
            if (errcode != null && errcode != 0){
                throw new WechatException(resp);
            }

            return (Integer)resp.get("msg_id");
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
         * 删除群发消息: 订阅号与服务号认证后均可用
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @return 删除成功，或抛WechatException
         */
        public Boolean deleteSend(String accessToken, Integer id){
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
         * @param accessToken acessToken
         * @param id 群发消息ID
         * @return 群发消息状态，或抛WechatException
         */
        public String getSend(String accessToken, Integer id){
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
     * 素材API
     */
    public final class Materials {

        private Materials(){}

    }
}
