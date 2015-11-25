package me.hao0.wechat.core;

import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.base.AccessToken;
import me.hao0.wechat.model.base.AuthType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 基础组件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class Bases extends Component {

    /**
     * 授权
     */
    private static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?";

    /**
     * 获取用户openId
     */
    private static final String OPEN_ID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    /**
     * 获取accessToken
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 获取微信服务器的IP地址列表
     */
    private static final String WX_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=";

    Bases(){}

    /**
     * 构建授权跳转URL(静默授权，仅获取用户openId，不包括个人信息)
     * @param redirectUrl 授权后的跳转URL(我方服务器URL)
     * @return 微信授权跳转URL
     */
    public String authUrl(String redirectUrl) {
        return authUrl(redirectUrl, Boolean.TRUE);
    }

    /**
     * 构建授权跳转URL
     * @param redirectUrl 授权后的跳转URL(我方服务器URL)
     * @param quiet 是否静默: true: 仅获取openId，false: 获取openId和个人信息(需用户手动确认)
     * @return 微信授权跳转URL
     */
    public String authUrl(String redirectUrl, Boolean quiet) {
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
            return AUTH_URL +
                    "appid=" + wechat.getAppId() +
                    "&redirect_uri=" + redirectUrl +
                    "&response_type=code&scope=" +
                    (quiet ? AuthType.BASE.scope() : AuthType.USER_INFO.scope())
                    + "&state=1#wechat_redirect";
        } catch (UnsupportedEncodingException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 获取用户openId
     * @param code 用户授权的code
     * @param cb 回调
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
                "appid=" + wechat.getAppId() +
                "&secret=" + wechat.getAppSecret() +
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
        String url = ACCESS_TOKEN_URL + "&appid=" + wechat.getAppId() + "&secret=" + wechat.getAppSecret();

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
     * @return 微信服务器IP列表，或抛WechatException
     */
    @SuppressWarnings("unchecked")
    public List<String> ip(String accessToken){
        String url = WX_IP_URL + accessToken;

        Map<String, Object> resp = doGet(url);

        return (List<String>)resp.get("ip_list");
    }
}
