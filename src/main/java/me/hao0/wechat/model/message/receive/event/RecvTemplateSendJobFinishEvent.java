package me.hao0.wechat.model.message.receive.event;

/**
 * 模版消息发送任务完成后, 微信服务器会将是否送达成功作为通知
 * <p>
 *     <a href="http://mp.weixin.qq.com/wiki/12/bd383158b0f8435c07b8b6bc7cdbac9c.html#.E4.BA.8B.E4.BB.B6.E6.8E.A8.E9.80.81" target="_blank">http://mp.weixin.qq.com/wiki/12/bd383158b0f8435c07b8b6bc7cdbac9c.html#.E4.BA.8B.E4.BB.B6.E6.8E.A8.E9.80.81</a>
 * </p>
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvTemplateSendJobFinishEvent extends RecvEvent {

    /**
     * 模版消息送达状态：
     * <p>
     *     1. 送达成功时：success <br/>
     *     2. 送达由于用户拒收（用户设置拒绝接收公众号消息）而失败时：failed:user block <br/>
     *     3. 送达由于其他原因失败时：failed: system failed
     * </p>
     */
    private String status;

    public RecvTemplateSendJobFinishEvent(RecvEvent e){
        super(e);
        this.eventType = e.eventType;
    }

    @Override
    public String getEventType() {
        return RecvEventType.SUBSCRIBE.value();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RecvTemplateSendJobFinishEvent{" +
                "status='" + status + '\'' +
                "} " + super.toString();
    }
}
