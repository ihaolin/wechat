package com.ishansong.wechat;

import org.haolin.wechat.core.Wechat;
import org.haolin.wechat.exception.WechatException;
import org.haolin.wechat.model.customer.MsgRecord;
import org.haolin.wechat.model.menu.Menu;
import org.haolin.wechat.model.message.Article;
import org.haolin.wechat.model.message.TemplateField;
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

    private String accessToken = "M0imM0RdVRiFC_6n-jKXjj2iXJvT0fPMf3LkDfk51qKeN4dnQWH8dBLoLCeOqDTOU9pnh2EDGt8R3yQpF_6uukIL_XNzMfpBGYBwhcG7BMEQASeACAFPH";

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
        System.out.println(wechat.MESSAGE.responseText("bcsdafafasdf", "你好"));
        System.out.println(wechat.MESSAGE.responseImage("bcsdafafasdf", "oijwefoiioefjiwfiwf"));
        System.out.println(wechat.MESSAGE.responseVoice("bcsdafafasdf", "9823r8f9h8f239hf293fh"));
        System.out.println(wechat.MESSAGE.responseVideo("bcsdafafasdf", "nowefwifjwopefjiwe", "视频标题", "视频描述"));
        System.out.println(wechat.MESSAGE.responseMusic("bcsdafafasdf", "joiwefjoiwejf", "音乐标题", "音乐描述", "http://jofwieofj.com", "http://jofwieofj.com?hq"));
        System.out.println(wechat.MESSAGE.responseNews("bcsdafafasdf",
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
}
