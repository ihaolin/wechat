package me.hao0.wechat.model.material;

/**
 * 普通素材类(图片，视频，语音)
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 12/11/15
 */
public class CommonMaterial extends Material {

    private static final long serialVersionUID = -6397218526074423143L;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 图文页的URL，或者，当获取的列表是图片素材列表时，该字段是图片的URL
     */
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CommonMaterial{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                "} " + super.toString();
    }
}
