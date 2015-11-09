package me.hao0.wechat;

import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.customer.MsgRecord;
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
import org.junit.Test;
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

    private Wechat wechat = Wechat.newWechat("appId", "appSecret");

    private String accessToken = "CDg5ENPqbkMndmz8yMddwxGuQtJANKOmuksygsR-wwqgMhjGF8HUzojfJb2b2fWqp4-6xIXAq_V8oBNckpM20eVxdIG3xGtJI9SJGnwD10gUMQfAAAJBF";

    private String testDomain = "xxx";

    private String openId = "onN_8trIW7PSoXLMzMSWySb5jfdY";

    @Test
    public void testAccessToken(){
        System.out.println(wechat.BASE.accessToken());
    }

    @Test
    public void testGetIps(){
        System.out.println(wechat.BASE.ip(accessToken));
    }

    @Test(expected = WechatException.class)
    public void testGetIpsErr(){
        System.out.println(wechat.BASE.ip(accessToken + "xxx"));
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
        List<MsgRecord> records = wechat.CS.getMsgRecords("ZL0gV5PVZy_fuTRRdMlJFFYebGpNHJNAWk71KlR0LA_sxGRp5wSHfRopva2TGAGnFyiDlL-Dr-FYjZ7eMg9nXkmNVyx-TPnC-vA4OVX2chgCAPjAIATRG", 1, 10, startTime, endTime);
        for (MsgRecord record : records){
            System.out.println(record);
        }
    }

    @Test
    public void testCreateCsSession(){
        assertTrue(wechat.CS.createSession(accessToken, "onN_8trIW7PSoXLMzMSWySb5jfdY", "haolin007@" + testDomain, "你好"));
    }

    @Test
    public void testGetUserSession(){
        System.out.println(wechat.CS.getUserSession(accessToken, "onN_8trIW7PSoXLMzMSWySb5jfdY"));
    }

    @Test
    public void testGetCsSession(){
        System.out.println(wechat.CS.getCsSessions(accessToken, "haolin007@" + testDomain));
    }

    @Test
    public void testGetWaitingSession(){
        System.out.println(wechat.CS.getWaitingSessions(accessToken));
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
        System.out.println(jsonMenu);
    }

    @Test
    public void testMenuGet(){
        System.out.println(wechat.MENU.get("QNmbpGXSMvZbNFg0fbLeHhIdxf2O-hl9F5H9QiEYTF6c1uUznVEFVoV9zIoomTAVS6gDCwEI8euJo8BggUiQYBoUFP4iV2bYr27JtII-0_sODTiAJAKZP"));
    }

    @Test
    public void testGroupCreate(){
        System.out.println(wechat.USER.createGroup(accessToken, "测试分组2"));
    }

    @Test
    public void testGroupGet(){
        System.out.println(wechat.USER.getGroup(accessToken));
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
        System.out.println(wechat.USER.getUserGroup(accessToken, openId));
    }

    @Test
    public void testMoveUserGroup(){
        assertTrue(wechat.USER.moveUserGroup(accessToken, openId, 100));
    }

    @Test
    public void testGetUserInfo(){
        System.out.println(wechat.USER.getUserInfo(accessToken, openId));
    }

    @Test
    public void testUserRemark(){
        assertTrue(wechat.USER.remarkUser(accessToken, openId, "林浩"));
    }

    @Test
    public void testMessageResp(){
        System.out.println(wechat.MESSAGE.respText("bcsdafafasdf", "你好"));
        System.out.println(wechat.MESSAGE.respImage("bcsdafafasdf", "oijwefoiioefjiwfiwf"));
        System.out.println(wechat.MESSAGE.respVoice("bcsdafafasdf", "9823r8f9h8f239hf293fh"));
        System.out.println(wechat.MESSAGE.respVideo("bcsdafafasdf", "nowefwifjwopefjiwe", "视频标题", "视频描述"));
        System.out.println(wechat.MESSAGE.respMusic("bcsdafafasdf", "joiwefjoiwejf", "音乐标题", "音乐描述", "http://jofwieofj.com", "http://jofwieofj.com?hq"));
        System.out.println(wechat.MESSAGE.respNews("bcsdafafasdf",
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
        System.out.println(
            wechat.MESSAGE.sendTemplate(accessToken,
                    openId, "vxjR2DrT-I5-Edzc3vgWV-CgMoG0cAWISAxQIMLmYk4", "http://www.hao0.me",
                    Arrays.asList(
                        new TemplateField("first", "下单成功"),
                        new TemplateField("remark", "感谢您的使用。"),
                        new TemplateField("keyword1", "TD113123123"),
                        new TemplateField("keyword2", "2015-11-11 11:11:11"),
                        new TemplateField("keyword3", "123456")
                    ))
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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


        xml = "<xml>\n" +
                "<ToUserName><![CDATA[toUser]]></ToUserName>\n" +
                "<FromUserName><![CDATA[FromUser]]></FromUserName>\n" +
                "<CreateTime>123456789</CreateTime>\n" +
                "<MsgType><![CDATA[event]]></MsgType>\n" +
                "<Event><![CDATA[subscribe]]></Event>\n" +
                "</xml>";
        message = wechat.MESSAGE.receive(xml);
        assertTrue(message instanceof RecvEvent && message instanceof RecvSubscribeEvent);
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);


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
        System.out.println(message);
    }

    @Test
    public void testMessagePreview(){
        SendPreviewMessage msg = new SendPreviewMessage();
        msg.setOpenId(openId);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("你好吗");
        System.out.println(wechat.MESSAGE.previewSend(accessToken, msg));
    }

    @Test
    public void testMessageSendByOpenIds(){
        SendMessage msg = new SendMessage();
        msg.setScope(SendMessageScope.OPEN_ID);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("这是测试，openId列表群发");
        msg.setOpenIds(Arrays.asList(openId, openId));
        System.out.println(wechat.MESSAGE.send(accessToken, msg));
    }

    @Test
    public void testMessageSendByGroupId(){
        SendMessage msg = new SendMessage();
        msg.setScope(SendMessageScope.GROUP);
        msg.setGroupId(100);
        msg.setType(SendMessageType.TEXT);
        msg.setContent("这是测试，groupId群发");
        System.out.println(wechat.MESSAGE.send(accessToken, msg));
    }

    @Test
    public void testMessageStatus(){
        System.out.println(wechat.MESSAGE.getSend(accessToken, 2547931045L));
    }

    @Test
    public void testMessageDelete(){
        assertTrue(wechat.MESSAGE.deleteSend(accessToken, 2547931045L));
    }

    @Test
    public void testGetTempQrcode(){
        System.out.println(wechat.QRCODE.getTempQrcode(accessToken, "1234", 3600));
    }

    @Test
    public void testGetPermQrcode(){
        System.out.println(wechat.QRCODE.getPermQrcode(accessToken, "12345"));
    }

    @Test
    public void testShortUrl(){
        System.out.println(wechat.QRCODE.shortUrl(accessToken, "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=xxxx%3D%3D"));
    }
}
