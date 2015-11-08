package com.ishansong.wechat;

import org.haolin.wechat.utils.MD5;
import org.junit.Test;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 6/11/15
 */
public class MD5Test {

    @Test
    public void testGen(){
        System.out.println(MD5.generate("123456", false));
    }
}
