package me.hao0.wechat.core;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.loader.AccessTokenLoader;
import me.hao0.wechat.loader.DefaultAccessTokenLoader;
import me.hao0.wechat.loader.DefaultTicketLoader;
import me.hao0.wechat.loader.TicketLoader;
import me.hao0.wechat.model.base.AccessToken;
import me.hao0.wechat.model.js.Ticket;
import me.hao0.wechat.model.js.TicketType;
import me.hao0.wechat.utils.Fields;
import me.hao0.wechat.utils.Jsons;
import me.hao0.wechat.utils.Http;
import java.io.InputStream;
import java.lang.reflect.Field;
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

    private static final String BASES = "me.hao0.wechat.core.Bases";

    private static final String USERS = "me.hao0.wechat.core.Users";

    private static final String MENUS = "me.hao0.wechat.core.Menus";

    private static final String CUSTOMER_SERVICES = "me.hao0.wechat.core.CustomerServices";

    private static final String MESSAGES = "me.hao0.wechat.core.Messages";

    private static final String QRCODES = "me.hao0.wechat.core.QrCodes";

    private static final String MATERIALS = "me.hao0.wechat.core.Materials";

    private static final String DATAS = "me.hao0.wechat.core.Datas";

    private static final String JSSDKS = "me.hao0.wechat.core.JsSdks";

    private static final AccessTokenLoader DEFAULT_ACCESS_TOKEN_LOADER = new DefaultAccessTokenLoader();

    private static final DefaultTicketLoader DEFAULT_TICKET_LOADER = new DefaultTicketLoader();

    private static final JavaType MAP_STRING_OBJ_TYPE = Jsons.DEFAULT.createCollectionType(Map.class, String.class, Object.class);

    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() + 1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("wechat");
                    return t;
                }
            });

    private LoadingCache<String, Component> components =
            CacheBuilder.newBuilder().build(new CacheLoader<String, Component>() {
                @Override
                public Component load(String classFullName) throws Exception {
                    Class clazz = Class.forName(classFullName);
                    Object comp = clazz.newInstance();
                    injectWechat(clazz, comp);
                    return (Component)comp;
                }
            });

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

    public Bases base(){
        return (Bases)components.getUnchecked(BASES);
    }

    public CustomerServices cs(){
        return (CustomerServices)components.getUnchecked(CUSTOMER_SERVICES);
    }

    public Menus menu(){
        return (Menus)components.getUnchecked(MENUS);
    }

    public Users user(){
        return (Users)components.getUnchecked(USERS);
    }

    public Messages msg(){
        return (Messages)components.getUnchecked(MESSAGES);
    }

    public QrCodes qr(){
        return (QrCodes)components.getUnchecked(QRCODES);
    }

    public Materials material(){
        return (Materials)components.getUnchecked(MATERIALS);
    }

    public Datas data(){
        return (Datas)components.getUnchecked(DATAS);
    }

    public JsSdks js(){
        return (JsSdks)components.getUnchecked(JSSDKS);
    }

    private void injectWechat(Class clazz, Object comp) throws NoSuchFieldException {
        Field wechat = clazz.getSuperclass().getDeclaredField("wechat");
        Fields.put(comp, wechat, this);
    }

    /**
     * 注册组件
     * @param component 组件对象
     * @param <T> 范型
     */
    public <T extends Component> void register(T component){
        try {
            injectWechat(component.getClass(), component);
        } catch (NoSuchFieldException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 关闭异步执行器(不再支持异步执行)
     */
    public void destroy(){
        if (executor.isShutdown()){
            executor.shutdown();
        }
    }

    String loadAccessToken(){
        String accessToken = tokenLoader.get();
        if (Strings.isNullOrEmpty(accessToken)){
            AccessToken token = base().accessToken();
            tokenLoader.refresh(token);
            accessToken = token.getAccessToken();
        }
        return accessToken;
    }

    String loadTicket(TicketType type){
        String ticket = ticketLoader.get(type);
        if (Strings.isNullOrEmpty(ticket)){
            Ticket t = js().getTicket(type);
            ticketLoader.refresh(t);
            ticket = t.getTicket();
        }
        return ticket;
    }

    Map<String, Object> doPost(String url, Map<String, Object> params) {
        String body = null;
        if (params != null && !params.isEmpty()){
            body = Jsons.DEFAULT.toJson(params);
        }
        return doPost(url, body);
    }

    Map<String, Object> doPost(String url, String body) {
        Http http = Http.post(url);
        if (!Strings.isNullOrEmpty(body)){
            http.body(body);
        }
        Map<String, Object> resp = http.request(MAP_STRING_OBJ_TYPE);
        Integer errcode = (Integer)resp.get(ERROR_CODE);
        if (errcode != null && errcode != 0){
            throw new WechatException(resp);
        }
        return resp;
    }

    Map<String, Object> doGet(String url) {
        return doGet(url, null);
    }

    Map<String, Object> doGet(String url, Map<String, Object> params) {
        Http http = Http.get(url);
        if (params != null && params.size() > 0){
            http.body(Jsons.DEFAULT.toJson(params));
        }
        Map<String, Object> resp = http.request(MAP_STRING_OBJ_TYPE);
        Integer errcode = (Integer)resp.get(ERROR_CODE);
        if (errcode != null && errcode != 0){
            throw new WechatException(resp);
        }
        return resp;
    }

    Map<String, Object> doUpload(String url, String fieldName, String fileName, InputStream input, Map<String, String> params){
        String json = Http.upload(url, fieldName, fileName, input, params);
        Map<String, Object> resp = Jsons.DEFAULT.fromJson(json, MAP_STRING_OBJ_TYPE);
        Integer errcode = (Integer)resp.get(ERROR_CODE);
        if (errcode != null && errcode != 0){
            throw new WechatException(resp);
        }
        return resp;
    }

    <T> void doAsync(final AsyncFunction<T> f){
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
}
