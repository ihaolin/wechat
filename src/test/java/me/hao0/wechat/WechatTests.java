package me.hao0.wechat;

import me.hao0.wechat.core.Callback;
import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.core.WechatBuilder;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.Page;
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
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public class WechatTests {

    private Wechat wechat = WechatBuilder.newBuilder("wxff66ea2204a7ad1c58", "5c9d2dc5a3c9209eadfa76cbx4893956f0").build();

    private String accessToken = "MMo1RBVEJNSssyYf49QxlBkGxeA20MTsk5RTS5N-CeXIny1iqqSQYjalr_VXx7-GJdf3W-zLmnB_UVU0Ntio-jhbHRCqpmTas4pxyaK0U-UACCjAEAPRR";

    private String testDomain = "xxx";

    private String openId = "onN_8trIW7PSoXLMzMSWySb5jfdY";

    private String jsApiTicket = "sM4AOVdWfPE4DxkXGEs8VCGATNbUB_Oae4oi2hRmdEb_g8HSkXz8ZFP6vY96wdPqIgHpx9HYoIePo-tRG7pt1g";

    @Test
    public void testAccessToken(){
        assertNotNull(wechat.BASE.accessToken());
    }

    @Test
    public void testGetIps(){
        assertNotNull(wechat.BASE.ip(accessToken));
    }

    @Test(expected = WechatException.class)
    public void testGetIpsErr(){
        wechat.BASE.ip(accessToken + "xxx");
    }

    @Test
    public void testCreateCsAccount(){
        assertTrue(wechat.CS.createAccount(accessToken, "haolin007@" + testDomain, "haolin007", "123456"));
    }

    @Test
    public void testUpdateCsAccount(){
        assertTrue(wechat.CS.updateAccount(accessToken, "haolin007@" + testDomain, "haolin007", "1234567"));
    }

    @Test
    public void testDeleteCsAccount(){
        assertTrue(wechat.CS.deleteAccount(accessToken, "haolin007@" + testDomain));
    }

    @Test
    public void testGetCsRecords(){
        Date startTime = new Date(1439434683);
        Date endTime = new Date(1439481483);
        assertNotNull(wechat.CS.getMsgRecords("ZL0gV5PVZy_fuTRRdMlJFFYebGpNHJNAWk71KlR0LA_sxGRp5wSHfRopva2TGAGnFyiDlL-Dr-FYjZ7eMg9nXkmNVyx-TPnC-vA4OVX2chgCAPjAIATRG", 1, 10, startTime, endTime));
    }

    @Test
    public void testCreateCsSession(){
        assertTrue(wechat.CS.createSession(accessToken, "onN_8trIW7PSoXLMzMSWySb5jfdY", "haolin007@" + testDomain, "你好"));
    }

    @Test
    public void testGetUserSession(){
        assertNotNull(wechat.CS.getUserSession(accessToken, "onN_8trIW7PSoXLMzMSWySb5jfdY"));
    }

    @Test
    public void testGetCsSession(){
        assertNotNull(wechat.CS.getCsSessions(accessToken, "haolin007@" + testDomain));
    }

    @Test
    public void testGetWaitingSession(){
        assertNotNull(wechat.CS.getWaitingSessions(accessToken));
    }

    @Test
    public void testMenuBuild(){
        Wechat.MenuBuilder menuBuilder = Wechat.MenuBuilder.newBuilder();

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
        assertNotNull(wechat.MENU.get(accessToken));
    }

    @Test
    public void testGroupCreate(){
        assertTrue(wechat.USER.createGroup(accessToken, "测试分组2") > 0);
    }

    @Test
    public void testGroupGet(){
        assertNotNull(wechat.USER.getGroup(accessToken));
    }

    @Test
    public void testGroupDelete(){
        assertTrue(wechat.USER.deleteGroup(accessToken, 102));
    }

    @Test
    public void testUpdateGroup(){
        assertTrue(wechat.USER.updateGroup(accessToken, 100, "测试分组哟"));
    }

    @Test
    public void testGetUserGroup(){
        assertTrue(wechat.USER.getUserGroup(accessToken, openId) > 0);
    }

    @Test
    public void testMoveUserGroup(){
        assertTrue(wechat.USER.mvUserGroup(accessToken, openId, 100));
    }

    @Test
    public void testGetUserInfo(){
        assertNotNull(wechat.USER.getUser(accessToken, openId));
    }

    @Test
    public void testUserRemark(){
        assertTrue(wechat.USER.remarkUser(accessToken, openId, "林浩"));
    }

    @Test
    public void testMessageResp(){
        assertNotNull(wechat.MESSAGE.respText("bcsdafafasdf", "你好"));
        assertNotNull(wechat.MESSAGE.respImage("bcsdafafasdf", "oijwefoiioefjiwfiwf"));
        assertNotNull(wechat.MESSAGE.respVoice("bcsdafafasdf", "9823r8f9h8f239hf293fh"));
        assertNotNull(wechat.MESSAGE.respVideo("bcsdafafasdf", "nowefwifjwopefjiwe", "视频标题", "视频描述"));
        assertNotNull(wechat.MESSAGE.respMusic("bcsdafafasdf", "joiwefjoiwejf", "音乐标题", "音乐描述", "http://jofwieofj.com", "http://jofwieofj.com?hq"));
        assertNotNull(wechat.MESSAGE.respNews("bcsdafafasdf",
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
        assertTrue(
                wechat.MESSAGE.sendTemplate(accessToken,
                        openId, "vxjR2DrT-I5-Edzc3vgWV-CgMoG0cAWISAxQIMLmYk4", "http://www.hao0.me",
                        Arrays.asList(
                                new TemplateField("first", "下单成功"),
                                new TemplateField("remark", "感谢您的使用。"),
                                new TemplateField("keyword1", "TD113123123"),
                                new TemplateField("keyword2", "2015-11-11 11:11:11"),
                                new TemplateField("keyword3", "123456")
                        )) > 0
        );
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
        RecvMessage message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
        assertTrue(message instanceof RecvMsg && message instanceof RecvLinkMessage);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";
        message = wechat.MESSAGE.receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvSubscribeEvent);

        xml = "<xml><ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "<EventKey><![CDATA[qrscene_123123]]></EventKey>\n" +
                "<Ticket><![CDATA[TICKET]]></Ticket>\n" +
                "</xml>";
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
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
        message = wechat.MESSAGE.receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvLocationEvent);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[CLICK]]></Event>\n" +
                "<EventKey><![CDATA[EVENTKEY]]></EventKey>\n" +
                "</xml>";
        message = wechat.MESSAGE.receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvMenuEvent);

        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[VIEW]]></Event>\n" +
                "<EventKey><![CDATA[www.qq.com]]></EventKey>\n" +
                "</xml>";
        message = wechat.MESSAGE.receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvMenuEvent);
    }

    @Test
    public void testMessagePreview(){
        SendPreviewMessage msg = new SendPreviewMessage();
        msg.setOpenId(openId);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("你好吗");
        assertTrue(wechat.MESSAGE.previewSend(accessToken, msg));
    }

    @Test
    public void testMessageSendByOpenIds(){
        SendMessage msg = new SendMessage();
        msg.setScope(SendMessageScope.OPEN_ID);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("这是测试，openId列表群发");
        msg.setOpenIds(Arrays.asList(openId, openId));
        assertTrue(wechat.MESSAGE.send(accessToken, msg) > 0L);
    }

    @Test
    public void testMessageSendByGroupId(){
        SendMessage msg = new SendMessage();
        msg.setScope(SendMessageScope.GROUP);
        msg.setGroupId(100);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("这是测试，groupId群发");
        assertTrue(wechat.MESSAGE.send(accessToken, msg) > 0L);
    }

    @Test
    public void testMessageStatus(){
        assertNotNull(wechat.MESSAGE.getSend(accessToken, 2547931045L));
    }

    @Test
    public void testMessageDelete(){
        assertTrue(wechat.MESSAGE.deleteSend(accessToken, 2547931045L));
    }

    @Test
    public void testGetTempQrcode(){
        assertNotNull(wechat.QRCODE.getTempQrcode(accessToken, "1234", 3600));
    }

    @Test
    public void testGetPermQrcode(){
        assertNotNull(wechat.QRCODE.getPermQrcode(accessToken, "12345"));
    }

    @Test
    public void testShortUrl(){
        assertNotNull(wechat.QRCODE.shortUrl(accessToken, "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=xxxx%3D%3D"));
    }

    @Test
    public void testMaterialCount(){
        assertNotNull(wechat.MATERIAL.count(accessToken));
    }

    @Test
    public void testMaterialGets(){

        Page<CommonMaterial> images = wechat.MATERIAL.gets(accessToken, MaterialType.IMAGE, 0, 10);
        assertTrue(images.getTotal() == 2);

        Page<CommonMaterial> videos = wechat.MATERIAL.gets(accessToken, MaterialType.VIDEO, 0, 10);
        assertTrue(videos.getTotal() == 0);

        Page<CommonMaterial> voices = wechat.MATERIAL.gets(accessToken, MaterialType.VOICE, 0, 10);
        assertTrue(voices.getTotal() == 0);

        Page<NewsMaterial> news = wechat.MATERIAL.gets(accessToken, MaterialType.NEWS, 0, 10);
        assertTrue(news.getTotal() == 1);
    }

    @Test
    public void testMediaUpload() throws FileNotFoundException {
        wechat.MATERIAL.uploadTemp(accessToken, MaterialUploadType.IMAGE, new File("/Users/haolin/temp/user.png"));
    }

    @Test
    public void testMediaDownload() throws IOException {
        File output = new File("/Users/haolin/temp/user_2.png");
        byte[] data = wechat.MATERIAL.downloadTemp(accessToken, "IT1E7HIXw69AWFeEXOLj0si5ufyTbCBwF8PlAkvK3Nj765RAZLkOPJS0zfKwItzG");
        FileOutputStream out = new FileOutputStream(output);
        out.write(data);
    }

    @Test
    public void testUploadPerm(){
        PermMaterial material = wechat.MATERIAL.uploadPerm(accessToken, MaterialUploadType.THUMB, new File("/Users/haolin/temp/user.png"));
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
        String mediaId = wechat.MATERIAL.uploadPermNews(accessToken, items);
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
        assertTrue(wechat.MATERIAL.updatePermNews(accessToken, "K74X6mIzSjUcRNfP5rjI8nT8utSktYKev5n56pR7L8k", 2, item));
    }

    @Test
    public void testUploadPermVideo() throws FileNotFoundException {
        PermMaterial material = wechat.MATERIAL.uploadPermVideo(accessToken, new File("/Users/haolin/temp/sport.mp4"), "运动", "动起来");
        assertNotNull(material);
        // K74X6mIzSjUcRNfP5rjI8uW0GM_jk0O_vauMXP9JWMg
    }

    @Test
    public void testGetTicket(){
        Ticket t = wechat.JSSDK.getTicket(accessToken, TicketType.JSAPI);
        assertNotNull(t);
        System.out.println(t);

        t = wechat.JSSDK.getTicket(accessToken, TicketType.CARD);
        assertNotNull(t);
        System.out.println(t);
    }

    @Test
    public void testJsConfig(){
        Config config = wechat.JSSDK.getConfig(jsApiTicket, "abcde", 12345678L, "http://m.bingex.com/demo");
        assertNotNull(config);
        assertEquals("ea570c3af8aa160441782ceaf7aecdfb55887b48", config.getSignature());
    }

    @Test
    public void testLoadWithoutToken(){
        List<Group> groups = wechat.USER.getGroup();
        assertNotNull(groups);
        System.out.println(groups);

        Integer groupId = wechat.USER.getUserGroup(openId);
        assertNotNull(groupId);
        System.out.println(groupId);

        Ticket ticket = wechat.JSSDK.getTicket(TicketType.JSAPI);
        assertNotNull(ticket);
        System.out.println(ticket);
    }

    @Test
    public void testTicketCallback() throws InterruptedException {
        wechat.JSSDK.getTicket(accessToken, TicketType.CARD, new Callback<Ticket>() {
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
}
