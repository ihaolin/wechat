package me.hao0.wechat.core;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import me.hao0.common.xml.XmlReaders;
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
import me.hao0.wechat.model.message.resp.Article;
import me.hao0.wechat.model.message.resp.RespMessageType;
import me.hao0.wechat.model.message.send.SendMessage;
import me.hao0.wechat.model.message.send.SendMessageScope;
import me.hao0.wechat.model.message.send.SendMessageType;
import me.hao0.wechat.model.message.send.SendPreviewMessage;
import me.hao0.wechat.model.message.send.TemplateField;
import me.hao0.wechat.utils.XmlWriters;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static me.hao0.common.util.Preconditions.*;

/**
 * 消息组件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class Messages extends Component {

    /**
     * 发送模板消息
     */
    private static final String TEMPLATE_SEND = "http://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * 分组群发消息
     */
    private static final String SEND_ALL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=";

    /**
     * 按openId列表群发消息
     */
    private static final String SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=";

    /**
     * 删除群发消息
     */
    private static final String DELETE_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=";

    /**
     * 预览群发消息
     */
    private static final String PREVIEW_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=";

    /**
     * 查询群发消息状态
     */
    private static final String GET_SEND = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=";

    Messages(){}

    /**
     * 被动回复微信服务器文本消息
     * @param openId 用户openId
     * @param content 文本内容
     * @return XML文本消息
     */
    public String respText(String openId, String content){
        checkNotNullAndEmpty(openId, "openId");
        checkNotNullAndEmpty(content, "content");
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
        checkNotNullAndEmpty(openId, "opendId");
        checkNotNullAndEmpty(mediaId, "mediaId");

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
        checkNotNullAndEmpty(openId, "opendId");
        checkNotNullAndEmpty(mediaId, "mediaId");

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
        checkNotNullAndEmpty(openId, "opendId");
        checkNotNullAndEmpty(mediaId, "mediaId");

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
        checkNotNullAndEmpty(openId, "opendId");
        checkNotNullAndEmpty(mediaId, "mediaId");

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
        checkNotNullAndEmpty(openId, "openId");
        checkNotNullAndEmpty(articles, "articles");
        checkArgument(articles.size() > 10, "articles length must < 10");

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
                .element("FromUserName", wechat.getAppId())
                .element("CreateTime", System.currentTimeMillis() / 1000)
                .element("MsgType", type.value());
        return xmlWriters;
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
        checkNotNullAndEmpty(openId, "openId");
        checkNotNullAndEmpty(kfAccount, "kfAccount");

        XmlWriters xmlWriters = XmlWriters.create();
        xmlWriters.element("ToUserName", openId)
                .element("FromUserName", wechat.getAppId())
                .element("CreateTime", System.currentTimeMillis() / 1000);

        if (!Strings.isNullOrEmpty(kfAccount)){
            xmlWriters.element("TransInfo", "KfAccount", kfAccount);
        }
        xmlWriters.element("MsgType", RespMessageType.CS.value());

        return xmlWriters.build();
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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNullAndEmpty(openId, "openId");
        checkNotNullAndEmpty(templateId, "templateId");

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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNull(msg, "msg can't be null");

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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkNotNull(msg, "msg can't be null");

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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkArgument(id != null && id > 0, "id must be > 0");

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
        checkNotNullAndEmpty(accessToken, "accessToken");
        checkArgument(id != null && id > 0, "id must be > 0");

        String url = GET_SEND + accessToken;
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("msg_id", id);

        Map<String, Object> resp = doPost(url, params);
        return (String)resp.get("msg_status");
    }
}
