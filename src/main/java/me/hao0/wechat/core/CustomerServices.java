package me.hao0.wechat.core;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.customer.CsSession;
import me.hao0.wechat.model.customer.MsgRecord;
import me.hao0.wechat.model.customer.UserSession;
import me.hao0.wechat.model.customer.WaitingSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import static me.hao0.common.util.Preconditions.*;

/**
 * 多客服组件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class CustomerServices extends Component {

    /**
     * 添加客服账号
     */
    private static final String CREATE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=";

    /**
     * 更新客服账号
     */
    private static final String UPDATE_ACCOUNT = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=";

    /**
     * 上传头像
     */
    private static final String UPLOAD_HEAD = "https://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token=";

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
        checkNotNullAndEmpty(account, "account");
        checkNotNullAndEmpty(nickName, "nickName");
        checkNotNullAndEmpty(password, "password");
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);

        params.put("kf_account", account);
        params.put("nickname", nickName);
        params.put("password", Hashing.md5().hashString(password, Charsets.UTF_8).toString());

        doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param data 二进制数据
     * @return 上传成功返回true，或抛WechatException
     */
    public Boolean uploadHead(String kfAccount, String fileName, byte[] data){
        return uploadHead(loadAccessToken(), kfAccount, fileName, new ByteArrayInputStream(data));
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param data 二进制数据
     * @return 上传成功返回true，或抛WechatException
     */
    public Boolean uploadHead(String accessToken, String kfAccount, String fileName, byte[] data){
        return uploadHead(accessToken, kfAccount, fileName, new ByteArrayInputStream(data));
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param data 二进制数据
     * @param cb 回调
     */
    public void uploadHead(String kfAccount, String fileName, byte[] data, Callback<Boolean> cb){
        uploadHead(loadAccessToken(), kfAccount, fileName, new ByteArrayInputStream(data), cb);
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param data 二进制数据
     * @param cb 回调
     */
    public void uploadHead(String accessToken, String kfAccount, String fileName, byte[] data, Callback<Boolean> cb){
        uploadHead(accessToken, kfAccount, fileName, new ByteArrayInputStream(data), cb);
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param image 文件
     * @return 上传成功返回true，或抛WechatException
     */
    public Boolean uploadHead(String kfAccount, File image){
        return uploadHead(loadAccessToken(), kfAccount, image);
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param image 文件
     * @return 上传成功返回true，或抛WechatException
     */
    public Boolean uploadHead(String accessToken, String kfAccount, File image){
        try {
            return uploadHead(accessToken, kfAccount, image.getName(), new FileInputStream(image));
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param image 文件
     * @param cb 回调
     */
    public void uploadHead(String accessToken, String kfAccount, File image, Callback<Boolean> cb){
        try {
            uploadHead(accessToken, kfAccount, image.getName(), new FileInputStream(image), cb);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param input 文件输入流
     * @return 上传成功返回true，或抛WechatException
     */
    public Boolean uploadHead(String kfAccount, String fileName, InputStream input){
        return uploadHead(loadAccessToken(), kfAccount, fileName, input);
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param input 文件输入流
     * @param cb 回调
     */
    public void uploadHead(final String accessToken, final String kfAccount, final String fileName, final InputStream input, Callback<Boolean> cb){
        doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() throws Exception {
                return uploadHead(accessToken, kfAccount, fileName, input);
            }
        });
    }

    /**
     * 上传客服头像(jpg/png等格式)
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param fileName 文件名
     * @param input 文件输入流
     * @return 上传成功返回true，或抛WechatException
     */
    public Boolean uploadHead(String accessToken, String kfAccount, String fileName, InputStream input){
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(kfAccount, "kfAccount");
        checkNotNullAndEmpty(fileName, "fileName");
        checkNotNull(input, "input can't be null");
        String url = UPLOAD_HEAD + accessToken + "&kf_account=" + kfAccount;
        doUpload(url, "media", fileName, input);
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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(kfAccount, "kfAccount");
        String url = DELETE_ACCOUNT + accessToken + "&kf_account=" + kfAccount;
        doGet(url);
        return Boolean.TRUE;
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
     * @param cb 回调
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
     * @param cb 回调
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
        checkNotNullAndEmpty(accessToken, "accessToken");
        String url = RECORD + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(4);
        params.put("pageindex", pageNo == null ? 1 : pageNo);
        params.put("pagesize", pageSize == null ? 10 : pageSize);
        params.put("starttime", startTime == null ? System.currentTimeMillis() : startTime.getTime());
        params.put("endtime", endTime == null ? System.currentTimeMillis() : endTime.getTime());

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
     * @param openId openId
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param text 附加文本
     * @return 创建成功返回true，或抛WechatException
     */
    public Boolean createSession(String openId, String kfAccount, String text){
        return createSession(loadAccessToken(), openId, kfAccount, text);
    }

    /**
     * 创建会话(该客服必需在线)
     * @param openId openId
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param text 附加文本
     * @param cb 回调
     */
    public void createSession(final String openId, final String kfAccount, final String text, Callback<Boolean> cb){
        createSession(loadAccessToken(), openId, kfAccount, text, cb);
    }

    /**
     * 创建会话(该客服必需在线)
     * @param accessToken accessToken
     * @param openId openId
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
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
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param text 附加文本
     * @return 创建成功返回true，或抛WechatException
     */
    public Boolean createSession(String accessToken, String openId, String kfAccount, String text){
        return createOrCloseSession(openId, kfAccount, text, CREATE_SESSION + accessToken);
    }

    /**
     * 关闭会话
     * @param openId openId
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param text 附加文本
     * @return 关闭成功返回true，或抛WechatException
     */
    public Boolean closeSession(String openId, String kfAccount, String text){
        return closeSession(loadAccessToken(), openId, kfAccount, text);
    }

    /**
     * 关闭会话
     * @param openId 用户openId
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
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
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
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
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param text 附加文本
     * @return 关闭成功返回true，或抛WechatException
     */
    public Boolean closeSession(String accessToken, String openId, String kfAccount, String text){
        return createOrCloseSession(openId, kfAccount, text, CLOSE_SESSION + accessToken);
    }

    private Boolean createOrCloseSession(String openId, String kfAccount, String text, String url){
        checkNotNullAndEmpty(openId, "openId");
        checkNotNullAndEmpty(kfAccount, "kfAccount");
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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(openId, "openId");

        String url = USER_SESSION_STATUS + accessToken + "&openid=" + openId;
        Map<String, Object> resp = doGet(url);
        UserSession status = new UserSession();
        status.setKfAccount(String.valueOf(resp.get("kf_account")));
        status.setCreateTime(new Date((Integer)resp.get("createtime") * 1000L));

        return status;
    }

    /**
     * 获取客服的会话列表
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @return 客服的会话列表，或抛WechatException
     */
    public List<CsSession> getCsSessions(String kfAccount){
        return getCsSessions(loadAccessToken(), kfAccount);
    }

    /**
     * 获取客服的会话列表
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @param cb 回调
     */
    public void getCsSessions(final String kfAccount, Callback<List<CsSession>> cb){
        getCsSessions(loadAccessToken(), kfAccount, cb);
    }


    /**
     * 获取客服的会话列表
     * @param accessToken accessToken
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
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
     * @param kfAccount 客服帐号(账号前缀@公众号微信号)
     * @return 客服的会话列表，或抛WechatException
     */
    @SuppressWarnings("unchecked")
    public List<CsSession> getCsSessions(String accessToken, String kfAccount){
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(kfAccount, "kfAccount");

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
        checkNotNullAndEmpty(accessToken, "accessToken");

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
