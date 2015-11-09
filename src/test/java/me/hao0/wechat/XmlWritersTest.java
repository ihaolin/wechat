package me.hao0.wechat;

import me.hao0.wechat.utils.XmlWriters;
import org.junit.Test;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 6/11/15
 */
public class XmlWritersTest {

    @Test
    public void testOneLevel(){
        XmlWriters xmlWriters = XmlWriters.create();

        xmlWriters.element("ToUserName", "123456")
            .element("FromUserName", "me")
            .element("CreateTime", System.currentTimeMillis())
            .element("MsgType", "transfer_customer_service");

        System.out.println(xmlWriters.build());
    }

    @Test
    public void testMultiLevel(){
        XmlWriters xmlWriters = XmlWriters.create();

        xmlWriters.element("ToUserName", "123456")
            .element("FromUserName", "me")
            .element("CreateTime", System.currentTimeMillis())
            .element("TransInfo", "KfAccount", "test1@test")
            .element("MsgType", "transfer_customer_service");

        System.out.println(xmlWriters.build());
    }

}
