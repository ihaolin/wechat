package me.hao0.wechat;

import me.hao0.wechat.core.Component;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 */
public class MyComponent extends Component {

    public String getAppId(){
        return wechat.getAppId();
    }
}
