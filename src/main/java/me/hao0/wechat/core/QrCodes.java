package me.hao0.wechat.core;

import com.google.common.collect.Maps;

import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.qrcode.Qrcode;
import me.hao0.wechat.model.qrcode.QrcodeType;
import me.hao0.common.json.Jsons;
import me.hao0.common.util.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import static me.hao0.common.util.Preconditions.*;

/**
 * 二维码组件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class QrCodes extends Component {

    /**
     * 获取Ticket
     */
    private static final String TICKET_GET = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";

    /**
     * 显示二维码链接
     */
    private static final String SHOW_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

    /**
     * 将原长链接通过此接口转成短链接
     */
    private static final String LONG_TO_SHORT = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";

    QrCodes(){}

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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(sceneId, "sceneId");
        checkArgument(expire != null && expire > 0, "expire must > 0");

        String url = TICKET_GET + accessToken;
        Map<String, Object> params = buildQrcodeParams(sceneId, null, QrcodeType.QR_SCENE);
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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(sceneId, "sceneId");

        String url = TICKET_GET + accessToken;
        Map<String, Object> params = buildQrcodeParams(sceneId, null, QrcodeType.QR_LIMIT_SCENE);

        Map<String, Object> resp = doPost(url, params);
        Qrcode qr = Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), Qrcode.class);

        return showQrcode(qr.getTicket());
    }
    
    
    /**
     * 获取永久二维码
     * @param sceneStr	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段   
     * @return 永久二维码链接，或抛WechatException
     */
    public String getPermQrcodeBySceneStr(String sceneStr){
        return getPermQrcodeBySceneStr(loadAccessToken(), sceneStr);
    }
    /**
     * 获取永久二维码
     * @param sceneStr	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段   
     * @param cb 回调
     */
    public void getPermQrcodeBySceneStr(final String sceneStr, Callback<String> cb){
        getPermQrcodeBySceneStr(loadAccessToken(), sceneStr, cb);
    }

    /**
     * 获取永久二维码
     * @param accessToken accessToken
     * @param sceneStr	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段   
     * @param cb 回调
     */
    public void getPermQrcodeBySceneStr(final String accessToken, final String sceneId, Callback<String> cb){
        doAsync(new AsyncFunction<String>(cb) {
            @Override
            public String execute() {
                return getPermQrcodeBySceneStr(accessToken, sceneId);
            }
        });
    }
    
    /**
     * 获取永久二维码
     * @param accessToken accessToken
     * @param sceneStr	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段   
     * @return 永久二维码链接，或抛WechatException
     */
    public String getPermQrcodeBySceneStr(String accessToken, String sceneStr){
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(sceneStr, "sceneStr");

        String url = TICKET_GET + accessToken;
        Map<String, Object> params = buildQrcodeParams(null, sceneStr, QrcodeType.QR_LIMIT_STR_SCENE);

        Map<String, Object> resp = doPost(url, params);
        Qrcode qr = Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), Qrcode.class);

        return showQrcode(qr.getTicket());
    }

    /**
     * 生成二维码参数，首先尝试使用 sceneId，再使用sceneStr
     * @param sceneId	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
     * @param sceneStr	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64，仅永久二维码支持此字段   
     * @param type	二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
     * @return	二维码参数
     */
    private Map<String, Object> buildQrcodeParams(String sceneId, String sceneStr, QrcodeType type) {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("action_name", type.value());

        Map<String, Object> sceneMap = Maps.newHashMapWithExpectedSize(1);
        if (!Strings.isNullOrEmpty(sceneId)) {
        	sceneMap.put("scene_id", sceneId);
		}else if (!Strings.isNullOrEmpty(sceneStr)) {
        	sceneMap.put("scene_str", sceneStr);
		}
        
        Map<String, Object> scene = Maps.newHashMapWithExpectedSize(1);
        scene.put("scene", sceneMap);

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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(longUrl, "longUrl");

        String url = LONG_TO_SHORT + accessToken;
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("action", "long2short");
        params.put("long_url", longUrl);

        Map<String, Object> resp = doPost(url, params);
        return (String)resp.get("short_url");
    }
}
