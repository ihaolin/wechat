package me.hao0.wechat;

import me.hao0.common.model.Page;
import me.hao0.wechat.core.Callback;
import me.hao0.wechat.core.MenuBuilder;
import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.core.WechatBuilder;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.base.AccessToken;
import me.hao0.wechat.model.data.article.ArticleDailySummary;
import me.hao0.wechat.model.data.article.ArticleShare;
import me.hao0.wechat.model.data.article.ArticleShareHour;
import me.hao0.wechat.model.data.article.ArticleSummary;
import me.hao0.wechat.model.data.article.ArticleSummaryHour;
import me.hao0.wechat.model.data.article.ArticleTotal;
import me.hao0.wechat.model.data.interfaces.InterfaceSummary;
import me.hao0.wechat.model.data.interfaces.InterfaceSummaryHour;
import me.hao0.wechat.model.data.msg.MsgSendDist;
import me.hao0.wechat.model.data.msg.MsgSendSummary;
import me.hao0.wechat.model.data.msg.MsgSendSummaryHour;
import me.hao0.wechat.model.data.user.UserCumulate;
import me.hao0.wechat.model.data.user.UserSummary;
import me.hao0.wechat.model.js.Ticket;
import me.hao0.wechat.model.js.TicketType;
import me.hao0.wechat.model.js.Config;
import me.hao0.wechat.model.material.CommonMaterial;
import me.hao0.wechat.model.material.MaterialType;
import me.hao0.wechat.model.material.MaterialUploadType;
import me.hao0.wechat.model.material.NewsContentItem;
import me.hao0.wechat.model.material.NewsMaterial;
import me.hao0.wechat.model.material.PermMaterial;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.event.RecvEvent;
import me.hao0.wechat.model.message.receive.event.RecvLocationEvent;
import me.hao0.wechat.model.message.receive.event.RecvMenuEvent;
import me.hao0.wechat.model.message.receive.event.RecvScanEvent;
import me.hao0.wechat.model.message.receive.event.RecvSubscribeEvent;
import me.hao0.wechat.model.message.receive.msg.RecvImageMessage;
import me.hao0.wechat.model.message.receive.msg.RecvLinkMessage;
import me.hao0.wechat.model.message.receive.msg.RecvLocationMessage;
import me.hao0.wechat.model.message.receive.msg.RecvMsg;
import me.hao0.wechat.model.message.receive.msg.RecvShortVideoMessage;
import me.hao0.wechat.model.message.receive.msg.RecvTextMessage;
import me.hao0.wechat.model.message.receive.msg.RecvVideoMessage;
import me.hao0.wechat.model.message.receive.msg.RecvVoiceMessage;
import me.hao0.wechat.model.message.resp.Article;
import me.hao0.wechat.model.message.send.SendMessage;
import me.hao0.wechat.model.message.send.SendMessageScope;
import me.hao0.wechat.model.message.send.SendMessageType;
import me.hao0.wechat.model.message.send.SendPreviewMessage;
import me.hao0.wechat.model.message.send.TemplateField;
import me.hao0.wechat.model.user.Group;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import static org.junit.Assert.*;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public class WechatTests {

    private Wechat wechat;

    private String accessToken = "a9l23BUNe5v3qvZeJs3N5nzRid9GbmXSjwW5XNMI0iUrZVkWPZFO-KLQvFRSa1GYY6CDAVNW0jXn5uGA6v1MGRsJgLSgVpeKR0HuJWB2LKdkFSDfACAUHS";

    private String testDomain = "xxx";

    private String openId = "onN_8trIW7PSoXLMzMSWySb5jfdY";

    @Before
    public void init() throws IOException {
        Properties props = new Properties();
        InputStream in = Object.class.getResourceAsStream("/dev.properties");
        props.load(in);

        wechat = WechatBuilder.newBuilder(props.getProperty("appId"), props.getProperty("appSecret"))
                .build();
    }

    @Test
    public void testAccessToken(){
        AccessToken token = wechat.base().accessToken();
        assertNotNull(token);
        System.out.println(token);
    }

    @Test
    public void testGetIps(){
        assertNotNull(wechat.base().ip(accessToken));
    }

    @Test(expected = WechatException.class)
    public void testGetIpsErr(){
        wechat.base().ip(accessToken + "xxx");
    }

    @Test
    public void testCreateCsAccount(){
        assertTrue(wechat.cs().createAccount(accessToken, "haolin007@" + testDomain, "haolin007", "123456"));
    }

    @Test
    public void testUpdateCsAccount(){
        assertTrue(wechat.cs().updateAccount(accessToken, "haolin007@" + testDomain, "haolin007", "1234567"));
    }

    @Test
    public void testUploadHead(){
        assertTrue(wechat.cs().uploadHead(accessToken, "haolin007@bingex01", new File("/Users/haolin/temp/user.png")));
    }

    @Test
    public void testDeleteCsAccount(){
        assertTrue(wechat.cs().deleteAccount(accessToken, "haolin007@" + testDomain));
    }

    @Test
    public void testGetCsRecords(){
        Date startTime = new Date(1439434683);
        Date endTime = new Date(1439481483);
        assertNotNull(wechat.cs().getMsgRecords("ZL0gV5PVZy_fuTRRdMlJFFYebGpNHJNAWk71KlR0LA_sxGRp5wSHfRopva2TGAGnFyiDlL-Dr-FYjZ7eMg9nXkmNVyx-TPnC-vA4OVX2chgCAPjAIATRG", 1, 10, startTime, endTime));
    }

    @Test
    public void testCreateCsSession(){
        assertTrue(wechat.cs().createSession(accessToken, "onN_8trIW7PSoXLMzMSWySb5jfdY", "haolin007@" + testDomain, "你好"));
    }

    @Test
    public void testGetUserSession(){
        assertNotNull(wechat.cs().getUserSession(accessToken, "onN_8trIW7PSoXLMzMSWySb5jfdY"));
    }

    @Test
    public void testGetCsSession(){
        assertNotNull(wechat.cs().getCsSessions(accessToken, "haolin007@" + testDomain));
    }

    @Test
    public void testGetWaitingSession(){
        assertNotNull(wechat.cs().getWaitingSessions(accessToken));
    }

    @Test
    public void testMenuBuild(){
        MenuBuilder menuBuilder = MenuBuilder.newBuilder();

        menuBuilder.view("我要下单", "http://www.hao0.me");

        menuBuilder.click("优惠活动", "ACTIVITIES");

        Menu more = menuBuilder.newParentMenu("更多");
        menuBuilder.click(more, "关于我们", "http://www.hao0.me");
        menuBuilder.view(more, "我的订单", "http://www.hao0.me");

        menuBuilder.menu(more);

        String jsonMenu = menuBuilder.build();
        assertEquals("{\"button\":[{\"name\":\"我要下单\",\"type\":\"view\",\"url\":\"http://www.hao0.me\"},{\"name\":\"优惠活动\",\"type\":\"click\",\"key\":\"ACTIVITIES\"},{\"name\":\"更多\",\"sub_button\":[{\"name\":\"关于我们\",\"type\":\"click\",\"key\":\"http://www.hao0.me\"},{\"name\":\"我的订单\",\"type\":\"view\",\"url\":\"http://www.hao0.me\"}]}]}", jsonMenu);
    }

    @Test
    public void testMenuGet(){
        assertNotNull(wechat.menu().get(accessToken));
    }

    @Test
    public void testGroupCreate(){
        assertTrue(wechat.user().createGroup(accessToken, "测试分组2") > 0);
    }

    @Test
    public void testGroupGet(){
        List<Group> groups = wechat.user().getGroup(accessToken);
        assertNotNull(groups);
        System.out.println(groups);
    }

    @Test
    public void testGroupDelete(){
        assertTrue(wechat.user().deleteGroup(accessToken, 102));
    }

    @Test
    public void testUpdateGroup(){
        assertTrue(wechat.user().updateGroup(accessToken, 100, "测试分组哟"));
    }

    @Test
    public void testGetUserGroup(){
        assertTrue(wechat.user().getUserGroup(accessToken, openId) > 0);
    }

    @Test
    public void testMoveUserGroup(){
        assertTrue(wechat.user().mvUserGroup(accessToken, openId, 100));
    }

    @Test
    public void testGetUserInfo(){
        assertNotNull(wechat.user().getUser(accessToken, openId));
    }

    @Test
    public void testGetUsersInfo() throws Exception {
        assertNotNull( wechat.user().getUsers("").getData().getOpenId());
    }

    @Test
    public void testUserRemark(){
        assertTrue(wechat.user().remarkUser(accessToken, openId, "林浩"));
    }

    @Test
    public void testMessageResp(){
        RecvMessage msg = new RecvMessage();
        msg.setFromUserName("xxx");
        msg.setToUserName("yyy");
        msg.setMsgType("text");
        msg.setCreateTime(123);

        assertNotNull(wechat.msg().respText(msg, "你好"));
        assertNotNull(wechat.msg().respImage(msg, "oijwefoiioefjiwfiwf"));
        assertNotNull(wechat.msg().respVoice(msg, "9823r8f9h8f239hf293fh"));
        assertNotNull(wechat.msg().respVideo(msg, "nowefwifjwopefjiwe", "视频标题", "视频描述"));
        assertNotNull(wechat.msg().respMusic(msg, "joiwefjoiwejf", "音乐标题", "音乐描述", "http://jofwieofj.com", "http://jofwieofj.com?hq"));
        assertNotNull(wechat.msg().respNews(msg,
                        Arrays.asList(
                                new Article("图文标题", "图文描述", "图片链接", "链接"),
                                new Article("图文标题", "图文描述", "图片链接", "链接"),
                                new Article("图文标题", "图文描述", "图片链接", "链接"),
                                new Article("图文标题", "图文描述", "图片链接", "链接")
                        ))
        );
    }

    @Test
    public void testSendTemplate(){
        Long msgId = wechat.msg().sendTemplate(accessToken,
                openId, "vxjR2DrT-I5-Edzc3vgWV-CgMoG0cAWISAxQIMLmYk4", "http://www.hao0.me",
                Arrays.asList(
                    new TemplateField("first", "下单成功"),
                    new TemplateField("remark", "感谢您的使用。"),
                    new TemplateField("keyword1", "TD113123123"),
                    new TemplateField("keyword2", "2015-11-11 11:11:11"),
                    new TemplateField("keyword3", "123456")
                ));
        assertTrue(msgId > 0L);
    }

    @Test
    public void testMessageReceive(){
        String xml = "<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName> \n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[this is a test]]></Content>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>";
        RecvMessage message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvTextMessage);

        xml = "<xml>\n" +
                " <ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                " <FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                " <CreateTime>1348831860</CreateTime>\n" +
                " <MsgType><![CDATA[image]]></MsgType>\n" +
                " <PicUrl><![CDATA[this is a url]]></PicUrl>\n" +
                " <MediaId><![CDATA[media_id]]></MediaId>\n" +
                " <MsgId>1234567890123456</MsgId>\n" +
                " </xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvImageMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1357290913</CreateTime>\n" +
                "<MsgType><![CDATA[voice]]></MsgType>\n" +
                "<MediaId><![CDATA[media_id]]></MediaId>\n" +
                "<Format><![CDATA[Format]]></Format>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvVoiceMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1357290913</CreateTime>\n" +
                "<MsgType><![CDATA[video]]></MsgType>\n" +
                "<MediaId><![CDATA[media_id]]></MediaId>\n" +
                "<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvVideoMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1357290913</CreateTime>\n" +
                "<MsgType><![CDATA[shortvideo]]></MsgType>\n" +
                "<MediaId><![CDATA[media_id]]></MediaId>\n" +
                "<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvShortVideoMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1351776360</CreateTime>\n" +
                "<MsgType><![CDATA[location]]></MsgType>\n" +
                "<Location_X>23.134521</Location_X>\n" +
                "<Location_Y>113.358803</Location_Y>\n" +
                "<Scale>20</Scale>\n" +
                "<Label><![CDATA[位置信息]]></Label>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml> ";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvLocationMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>1351776360</CreateTime>\n" +
                "<MsgType><![CDATA[link]]></MsgType>\n" +
                "<Title><![CDATA[公众平台官网链接]]></Title>\n" +
                "<Description><![CDATA[公众平台官网链接]]></Description>\n" +
                "<Url><![CDATA[url]]></Url>\n" +
                "<MsgId>1234567890123456</MsgId>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvLinkMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvSubscribeEvent);

        xml = "<xml><ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "<EventKey><![CDATA[qrscene_123123]]></EventKey>\n" +
                "<Ticket><![CDATA[TICKET]]></Ticket>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvSubscribeEvent);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[SCAN]]></Event>\n" +
                "<EventKey><![CDATA[SCENE_VALUE]]></EventKey>\n" +
                "<Ticket><![CDATA[TICKET]]></Ticket>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvScanEvent);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[fromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[LOCATION]]></Event>\n" +
                "<Latitude>23.137466</Latitude>\n" +
                "<Longitude>113.352425</Longitude>\n" +
                "<Precision>119.385040</Precision>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvLocationEvent);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[CLICK]]></Event>\n" +
                "<EventKey><![CDATA[EVENTKEY]]></EventKey>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvMenuEvent);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[VIEW]]></Event>\n" +
                "<EventKey><![CDATA[www.qq.com]]></EventKey>\n" +
                "</xml>";
        message = wechat.msg().receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvMenuEvent);
    }

    @Test
    public void testMessagePreview(){
        SendPreviewMessage msg = new SendPreviewMessage();
        msg.setOpenId(openId);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("你好吗");
        assertTrue(wechat.msg().previewSend(accessToken, msg));
    }

    @Test
    public void testMessageSendByOpenIds(){
        SendMessage msg = new SendMessage();
        msg.setScope(SendMessageScope.OPEN_ID);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("这是测试，openId列表群发");
        msg.setOpenIds(Arrays.asList(openId, openId));
        Long msgId = wechat.msg().send(accessToken, msg);
        assertTrue(msgId > 0L);
        System.out.println(msgId);
    }

    @Test
    public void testMessageSendByGroupId(){
        SendMessage msg = new SendMessage();
        msg.setScope(SendMessageScope.GROUP);
        msg.setGroupId(100);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("这是测试，groupId群发");
        Long msgId = wechat.msg().send(accessToken, msg);
        assertTrue(msgId > 0L);
        System.out.println(msgId);
    }

    @Test
    public void testMessageStatus(){
        assertNotNull(wechat.msg().getSend(accessToken, 2547931045L));
    }

    @Test
    public void testMessageDelete(){
        assertTrue(wechat.msg().deleteSend(accessToken, 2547931045L));
    }

    @Test
    public void testGetTempQrcode(){
        assertNotNull(wechat.qr().getTempQrcode(accessToken, "1234", 3600));
    }

    @Test
    public void testGetPermQrcode(){
        assertNotNull(wechat.qr().getPermQrcode(accessToken, "12345"));
    }
    
    @Test
    public void testGetPermQrcodeBySceneStr(){
        assertNotNull(wechat.qr().getPermQrcodeBySceneStr(accessToken, "abcdefg"));
    }

    @Test
    public void testShortUrl(){
        assertNotNull(wechat.qr().shortUrl(accessToken, "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=xxxx%3D%3D"));
    }

    @Test
    public void testMaterialCount(){
        assertNotNull(wechat.material().count(accessToken));
    }

    @Test
    public void testMaterialGets(){

        Page<CommonMaterial> images = wechat.material().gets(accessToken, MaterialType.IMAGE, 0, 10);
        assertNotNull(images);
        System.out.println(images);

        Page<CommonMaterial> videos = wechat.material().gets(accessToken, MaterialType.VIDEO, 0, 10);
        assertNotNull(videos);
        System.out.println(videos);

        Page<CommonMaterial> voices = wechat.material().gets(accessToken, MaterialType.VOICE, 0, 10);
        assertNotNull(voices);
        System.out.println(voices);

        Page<NewsMaterial> news = wechat.material().gets(accessToken, MaterialType.NEWS, 0, 10);
        assertNotNull(news);
        System.out.println(news);
    }

    @Test
    public void testMediaUpload() throws FileNotFoundException {
        wechat.material().uploadTemp(accessToken, MaterialUploadType.IMAGE, new File("/Users/haolin/temp/user.png"));
    }

    @Test
    public void testMediaDownload() throws IOException {
        File output = new File("/Users/haolin/temp/user_2.png");
        byte[] data = wechat.material().downloadTemp(accessToken, "IT1E7HIXw69AWFeEXOLj0si5ufyTbCBwF8PlAkvK3Nj765RAZLkOPJS0zfKwItzG");
        FileOutputStream out = new FileOutputStream(output);
        out.write(data);
    }

    @Test
    public void testUploadPerm(){
        PermMaterial material = wechat.material().uploadPerm(accessToken, MaterialUploadType.THUMB, new File("/Users/haolin/temp/user.png"));
        assertNotNull(material);
        // K74X6mIzSjUcRNfP5rjI8oERKkI_0_X8u16ZiY14ut4
        // https://mp.weixin.qq.com/cgi-bin/filepage?type=2&begin=0&count=12&t=media/img_list&token=649963139&lang=zh_CN
    }

    @Test
    public void testUploadPermNews(){
        List<NewsContentItem> items = new ArrayList<>();
        NewsContentItem item;
        for (int i=0; i<3; i++){
            item = new NewsContentItem();
            item.setTitle("测试标题" + i);
            item.setAuthor("测试作者" + i);
            item.setContent("测试作者" + i);
            item.setDigest("这是测试啊" + i);
            item.setShowCoverPic(1);
            item.setThumbMediaId("K74X6mIzSjUcRNfP5rjI8oERKkI_0_X8u16ZiY14ut4");
            item.setUrl("https://github.com/ihaolin/wechat");
            item.setContentSourceUrl("https://github.com/ihaolin/wechat");
            items.add(item);
        }
        String mediaId = wechat.material().uploadPermNews(accessToken, items);
        assertNotNull(mediaId);
        // K74X6mIzSjUcRNfP5rjI8nT8utSktYKev5n56pR7L8k
    }

    @Test
    public void testUpdatePermNews(){
        NewsContentItem item = new NewsContentItem();
        item.setTitle("测试标题");
        item.setAuthor("测试作者");
        item.setContent("测试作者");
        item.setDigest("这是测试更新啊");
        item.setShowCoverPic(1);
        item.setThumbMediaId("K74X6mIzSjUcRNfP5rjI8oERKkI_0_X8u16ZiY14ut4");
        item.setUrl("https://github.com/ihaolin/wechat");
        item.setContentSourceUrl("https://github.com/ihaolin/wechat");
        assertTrue(wechat.material().updatePermNews(accessToken, "K74X6mIzSjUcRNfP5rjI8nT8utSktYKev5n56pR7L8k", 2, item));
    }

    @Test
    public void testUploadPermVideo() throws FileNotFoundException {
        PermMaterial material = wechat.material().uploadPermVideo(accessToken, new File("/Users/haolin/temp/sport.mp4"), "运动", "动起来");
        assertNotNull(material);
        // K74X6mIzSjUcRNfP5rjI8uW0GM_jk0O_vauMXP9JWMg
    }

    @Test
    public void testGetTicket(){
        Ticket t = wechat.js().getTicket(accessToken, TicketType.JSAPI);
        assertNotNull(t);
        System.out.println(t);

        t = wechat.js().getTicket(accessToken, TicketType.CARD);
        assertNotNull(t);
        System.out.println(t);
    }

    @Test
    public void testJsConfig(){
        String jsApiTicket = "sM4AOVdWfPE4DxkXGEs8VCGATNbUB_Oae4oi2hRmdEb_g8HSkXz8ZFP6vY96wdPqIgHpx9HYoIePo-tRG7pt1g";
        Config config = wechat.js().getConfig(jsApiTicket, "abcde", 12345678L, "http://m.bingex.com/demo");
        assertNotNull(config);
        assertEquals("ea570c3af8aa160441782ceaf7aecdfb55887b48", config.getSignature());
    }

    @Test
    public void testLoadWithoutToken(){
        List<Group> groups = wechat.user().getGroup();
        assertNotNull(groups);
        System.out.println(groups);

        Integer groupId = wechat.user().getUserGroup(openId);
        assertNotNull(groupId);
        System.out.println(groupId);

        Ticket ticket = wechat.js().getTicket(TicketType.JSAPI);
        assertNotNull(ticket);
        System.out.println(ticket);
    }

    @Test
    public void testTicketCallback() throws InterruptedException {
        wechat.js().getTicket(accessToken, TicketType.CARD, new Callback<Ticket>() {
            @Override
            public void onSuccess(Ticket ticket) {
                assertNotNull(ticket);
                System.out.println(ticket);
            }

            @Override
            public void onFailure(Exception e) {
                System.err.println(e);
            }
        });
        System.out.println("main");

        Thread.sleep(1000000L);
    }

    @Test
    public void testClazzName(){
        System.out.println(Wechat.class.getSimpleName());
        System.out.println(Wechat.class.getCanonicalName());
        System.out.println(Wechat.class.getName());
    }

    @Test
    public void testRegisterComponent(){
        MyComponent myComp = new MyComponent();
        wechat.register(myComp);
        assertNotNull(myComp.getAppId());
    }

    @Test
    public void testUserSummary(){
        List<UserSummary> summaries = wechat.data().userSummary(accessToken, "2015-09-01", "2015-09-06");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testUserCumulate(){
        List<UserCumulate> cumulates = wechat.data().userCumulate(accessToken, "2015-09-01", "2015-09-06");
        assertNotNull(cumulates);
        System.out.println(cumulates);
    }

    @Test
    public void testarticleSummaryDaily(){
        List<ArticleDailySummary> summaries = wechat.data().articleDailySummary(accessToken, "2015-11-11");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testArticleTotal(){
        List<ArticleTotal> totals = wechat.data().articleTotal(accessToken, "2015-11-11");
        assertNotNull(totals);
        System.out.println(totals);
    }

    @Test
    public void testArticleSummary(){
        List<ArticleSummary> summaries = wechat.data().articleSummary(accessToken, "2015-06-23", "2015-06-25");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testArticleSummaryByHour(){
        List<ArticleSummaryHour> summaries = wechat.data().articleSummaryHourly(accessToken, "2015-06-23");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testArticleShare(){
        List<ArticleShare> shares = wechat.data().articleShare(accessToken, "2015-11-11", "2015-11-17");
        assertNotNull(shares);
        System.out.println(shares);
    }

    @Test
    public void testArticleShareByHour(){
        List<ArticleShareHour> shares = wechat.data().articleShareByHourly(accessToken, "2015-11-11");
        assertNotNull(shares);
        System.out.println(shares);
    }

    @Test
    public void testInterfaceSummary(){
        List<InterfaceSummary> summaries = wechat.data().interfaceSummary(accessToken, "2015-11-12", "2015-11-19");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testInterfaceSummaryByHour(){
        List<InterfaceSummaryHour> summaries = wechat.data().interfaceSummaryHourly(accessToken, "2015-11-19");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testMsgSendSummary(){
        List<MsgSendSummary> summaries = wechat.data().msgSendSummary(accessToken, "2015-11-12", "2015-11-18");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testMsgSendSummaryByHour(){
        List<MsgSendSummaryHour> summaries = wechat.data().msgSendSummaryHourly(accessToken, "2015-11-19");
        assertNotNull(summaries);
        System.out.println(summaries);
    }

    @Test
    public void testMsgSendSummaryWeekly(){
        List<MsgSendSummary> summaries = wechat.data().msgSendSummaryWeekly(accessToken, "2015-10-01", "2015-10-30");
        assertNotNull(summaries);
        for (MsgSendSummary summary : summaries){
            System.out.println(summary);
        }
    }

    @Test
    public void testMsgSendSummaryMonthly(){
        List<MsgSendSummary> summaries = wechat.data().msgSendSummaryMonthly(accessToken, "2015-10-01", "2015-10-30");
        assertNotNull(summaries);
        for (MsgSendSummary summary : summaries){
            System.out.println(summary);
        }
    }

    @Test
    public void testMsgSendDist(){
        List<MsgSendDist> dists = wechat.data().msgSendDist(accessToken, "2015-11-11", "2015-11-18");
        assertNotNull(dists);
        for (MsgSendDist dist : dists){
            System.out.println(dist);
        }
    }

    @Test
    public void testMsgSendDistWeekly(){
        List<MsgSendDist> dists = wechat.data().msgSendDistWeekly(accessToken, "2015-09-01", "2015-09-30");
        assertNotNull(dists);
        for (MsgSendDist dist : dists){
            System.out.println(dist);
        }
    }

    @Test
    public void testMsgSendDistMonthly(){
        List<MsgSendDist> dists = wechat.data().msgSendDistMonthly(accessToken, "2015-10-01", "2015-10-30");
        assertNotNull(dists);
        for (MsgSendDist dist : dists){
            System.out.println(dist);
        }
    }

    @Test
    public void testBuildWechat(){
        Wechat w = WechatBuilder.newBuilder(null, "").build();
    }
}
