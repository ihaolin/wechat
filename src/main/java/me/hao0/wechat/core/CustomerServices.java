package me.hao0.wechat.core;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import me.hao0.wechat.model.customer.CsSession;
import me.hao0.wechat.model.customer.MsgRecord;
import me.hao0.wechat.model.customer.UserSession;
import me.hao0.wechat.model.customer.WaitingSession;
import me.hao0.wechat.utils.XmlWriters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 */
public class CustomerServices extends Component {

    /**
     * 添加客服账号
     */
    private static final String CREATE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=";

    /**
     * 更新客服账号
     */
    private static final String UPDATE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=";

    /**
     * 删除客服帐号
     */
    private static final String DELETE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=";

    /**
     * 客服聊天记录
     */
    private static final String RECORD = "https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token=";

    /**
     * 创建客服会话
     */
    private static final String CREATE_SESSION = "https://api.weixin.qq.com/customservice/kfsession/create?access_token=";

    /**
     * 关闭客服会话
     */
    private static final String CLOSE_SESSION = "https://api.weixin.qq.com/customservice/kfsession/close?access_token=";

    /**
     * 获取用户会话状态
     */
    private static final String USER_SESSION_STATUS = "https://api.weixin.qq.com/customservice/kfsession/getsession?access_token=";

    /**
     * 获取客服会话状态
     */
    private static final String CS_SESSION_STATUS = "https://api.weixin.qq.com/customservice/kfsession/getsessionlist?access_token=";

    /**
     * 未接入会话
     */
    private static final String WAITING_SESSION = "https://api.weixin.qq.com/customservice/kfsession/getwaitcase?access_token=";

    CustomerServices(){}

    /**
     * 添加客服账户
     * @param account 登录帐号(包含域名)
     * @param nickName 昵称
     * @param password 明文密码
     * @return 添加成功返回true，反之false
     */
    public Boolean createAccount(String account, String nickName, String password){
        return createAccount(wechat.loadAccessToken(), account, nickName, password);
    }

    /**
     * 添加客服账户
     * @param account 登录帐号(包含域名)
     * @param nickName 昵称
     * @param password 明文密码
     * @param cb 回调
     */
    public void createAccount(final String account, final String nickName, final String password, final Callback<Boolean> cb){
        createAccount(wechat.loadAccessToken(), account, nickName, password, cb);
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
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
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
        return updateAccount(wechat.loadAccessToken(), account, nickName, password);
    }

    /**
     * 更新客服账户
     * @param account 登录帐号(包含域名)
     * @param nickName 昵称
     * @param password 明文密码
     * @param cb 回调
     */
    public void updateAccount(final String account, final String nickName, final String password, Callback<Boolean> cb){
        updateAccount(wechat.loadAccessToken(), account, nickName, password, cb);
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
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
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

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 删除客服账户
     * @param kfAccount 客服登录帐号(包含域名)
     * @param cb 回调
     */
    public void deleteAccount(String kfAccount, Callback<Boolean> cb){
        deleteAccount(wechat.loadAccessToken(), kfAccount, cb);
    }

    /**
     * 删除客服账户
     * @param kfAccount 客服登录帐号(包含域名)
     * @return 添加成功返回true，或抛WechatException
     */
    public Boolean deleteAccount(String kfAccount){
        return deleteAccount(wechat.loadAccessToken(), kfAccount);
    }

    /**
     * 删除客服账户
     * @param accessToken accessToken
     * @param kfAccount 客服登录帐号(包含域名)
     * @param cb 回调
     */
    public void deleteAccount(final String accessToken, final String kfAccount, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
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

        wechat.doGet(url);

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
                .element("FromUserName", wechat.getAppId())
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
        return getMsgRecords(wechat.loadAccessToken(), pageNo, pageSize, startTime, endTime);
    }

    /**
     * 查询客服聊天记录
     * @param pageNo 页码
     * @param pageSize 分页大小
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param cb 回调
     */
    public void getMsgRecords(final Integer pageNo, final Integer pageSize, final Date startTime, final Date endTime, Callback<List<MsgRecord>> cb){
        getMsgRecords(wechat.loadAccessToken(), pageNo, pageSize, startTime, endTime, cb);
    }

    /**
     * 查询客服聊天记录
     * @param accessToken accessToken
     * @param pageNo 页码
     * @param pageSize 分页大小
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param cb 回调
     */
    public void getMsgRecords(final String accessToken, final Integer pageNo, final Integer pageSize, final Date startTime, final Date endTime, Callback<List<MsgRecord>> cb){
        wechat.doAsync(new AsyncFunction<List<MsgRecord>>(cb) {
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

        Map<String, Object> resp = wechat.doPost(url, params);
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
     * @param openId openId
     * @param kfAccount 客服帐号(包含域名)
     * @param text 附加文本
     * @return 创建成功返回true，或抛WechatException
     */
    public Boolean createSession(String openId, String kfAccount, String text){
        return createSession(wechat.loadAccessToken(), openId, kfAccount, text);
    }

    /**
     * 创建会话(该客服必需在线)
     * @param openId openId
     * @param kfAccount 客服帐号(包含域名)
     * @param text 附加文本
     * @param cb 回调
     */
    public void createSession(final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
        createSession(wechat.loadAccessToken(), openId, kfAccount, text, cb);
    }

    /**
     * 创建会话(该客服必需在线)
     * @param accessToken accessToken
     * @param openId openId
     * @param kfAccount 客服帐号(包含域名)
     * @param text 附加文本
     * @param cb 回调
     */
    public void createSession(final String accessToken, final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
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
     * @param openId openId
     * @param kfAccount 客服帐号(包含域名)
     * @param text 附加文本
     * @return 关闭成功返回true，或抛WechatException
     */
    public Boolean closeSession(String openId, String kfAccount, String text){
        return closeSession(wechat.loadAccessToken(), openId, kfAccount, text);
    }

    /**
     * 关闭会话
     * @param openId 用户openId
     * @param kfAccount 客服帐号(包含域名)
     * @param text 附加文本
     * @param cb 回调
     */
    public void closeSession(final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
        closeSession(wechat.loadAccessToken(), openId, kfAccount, text, cb);
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
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return closeSession(accessToken, openId, kfAccount, text);
            }
        });
    }

    /**
     * 关闭会话
     * @param accessToken accessToken
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

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 获取用户的会话状态
     * @param openId 用户openId
     * @return 客户的会话状态，或抛WechatException
     */
    public UserSession getUserSession(String openId){
        return getUserSession(wechat.loadAccessToken(), openId);
    }

    /**
     * 获取用户的会话状态
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param cb 回调
     */
    public void getUserSession(final String accessToken, final String openId, Callback<UserSession> cb){
        wechat.doAsync(new AsyncFunction<UserSession>(cb) {
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
        getUserSession(wechat.loadAccessToken(), openId, cb);
    }

    /**
     * 获取用户的会话状态
     * @param accessToken accessToken
     * @param openId 用户openId
     * @return 客户的会话状态，或抛WechatException
     */
    public UserSession getUserSession(String accessToken, String openId){
        String url = USER_SESSION_STATUS + accessToken + "&openid=" + openId;

        Map<String, Object> resp = wechat.doGet(url);
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
        return getCsSessions(wechat.loadAccessToken(), kfAccount);
    }

    /**
     * 获取客服的会话列表
     * @param kfAccount 客服帐号(包含域名)
     * @param cb 回调
     */
    public void getCsSessions(final String kfAccount, Callback<List<CsSession>> cb){
        getCsSessions(wechat.loadAccessToken(), kfAccount, cb);
    }


    /**
     * 获取客服的会话列表
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(包含域名)
     * @param cb 回调
     */
    public void getCsSessions(final String accessToken, final String kfAccount, Callback<List<CsSession>> cb){
        wechat.doAsync(new AsyncFunction<List<CsSession>>(cb) {
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

        Map<String, Object> resp = wechat.doGet(url);
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
        return getWaitingSessions(wechat.loadAccessToken());
    }

    /**
     * 获取未接入的会话列表
     * @param cb 回调
     */
    public void getWaitingSessions(Callback<List<WaitingSession>> cb){
        getWaitingSessions(wechat.loadAccessToken(), cb);
    }

    /**
     * 获取未接入的会话列表
     * @param accessToken accessToken
     * @param cb 回调
     */
    public void getWaitingSessions(final String accessToken, Callback<List<WaitingSession>> cb){
        wechat.doAsync(new AsyncFunction<List<WaitingSession>>(cb) {
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

        Map<String, Object> resp = wechat.doGet(url);
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
