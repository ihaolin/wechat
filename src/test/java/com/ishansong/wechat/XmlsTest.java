package com.ishansong.wechat;

import org.haolin.wechat.utils.Xmls;
import org.junit.Test;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 6/11/15
 */
public class XmlsTest {

    @Test
    public void testOneLevel(){
        Xmls xmls = Xmls.create();

        xmls.element("ToUserName", "123456")
            .element("FromUserName", "me")
            .element("CreateTime", System.currentTimeMillis())
            .element("MsgType", "transfer_customer_service");

        System.out.println(xmls.build());
    }

    @Test
    public void testMultiLevel(){
        Xmls xmls = Xmls.create();

        xmls.element("ToUserName", "123456")
            .element("FromUserName", "me")
            .element("CreateTime", System.currentTimeMillis())
            .element("TransInfo", "KfAccount", "test1@test")
            .element("MsgType", "transfer_customer_service");

        System.out.println(xmls.build());
    }

}
