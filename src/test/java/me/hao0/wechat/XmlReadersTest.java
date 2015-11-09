package me.hao0.wechat;

import me.hao0.wechat.utils.XmlReaders;
import org.junit.Test;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class XmlReadersTest {

    @Test
    public void testGetNode(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<work-contact-info>" +
                "<Location>Shanghai-shuion-333</Location>" +
                "<Postal>200020</Postal>" +
                "<Tel><fix>63262299</fix><mobile>" +
                "1581344454</mobile></Tel>" +
                "<Appellation>Mr. Wang</Appellation>" +
                "</work-contact-info>";
        XmlReaders readers = XmlReaders.create(xml);
        System.out.println(readers.getNodeStr("Tel"));
    }
}
