package me.hao0.wechat.core;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import me.hao0.wechat.exception.WechatException;
import me.hao0.wechat.model.Page;
import me.hao0.wechat.model.material.CommonMaterial;
import me.hao0.wechat.model.material.MaterialCount;
import me.hao0.wechat.model.material.MaterialType;
import me.hao0.wechat.model.material.MaterialUploadType;
import me.hao0.wechat.model.material.NewsContentItem;
import me.hao0.wechat.model.material.NewsMaterial;
import me.hao0.wechat.model.material.PermMaterial;
import me.hao0.wechat.model.material.TempMaterial;
import me.hao0.wechat.utils.Http;
import me.hao0.wechat.utils.Jsons;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 */
public class Materials extends Component {

    /**
     * 素材总数
     */
    private static final String COUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=";

    /**
     * 素材列表
     */
    private static final String GETS = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=";

    /**
     * 删除永久素材
     */
    private static final String DELETE = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=";

    /**
     * 临时素材上传
     */
    private static final String UPLOAD_TEMP = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=";

    /**
     * 临时素材下载
     */
    private static final String DOWNLOAD_TEMP = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=";

    /**
     * 添加永久图文素材
     */
    private static final String ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=";

    /**
     * 更新永久图文素材
     */
    private static final String UPDATE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=";

    /**
     * 上传永久图文素材内容中引用的图片
     */
    private static final String UPLOAD_NEWS_IMAGE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=";

    /**
     * 上传永久素材(图片，语音，视频)
     */
    private static final String UPLOAD_PERM = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=";

    private static final JavaType ARRAY_LIST_COMMON_MATERIAL_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, CommonMaterial.class);

    private static final JavaType ARRAY_LIST_NEWS_MATERIAL_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, NewsMaterial.class);

    Materials(){}

    /**
     * 获取素材总数统计
     * @return 素材总数统计
     */
    public MaterialCount count(){
        return count(wechat.loadAccessToken());
    }

    /**
     * 获取素材总数统计
     * @param cb 回调
     */
    public void count(Callback<MaterialCount> cb){
        count(wechat.loadAccessToken(), cb);
    }

    /**
     * 获取素材总数统计
     * @param accessToken accessToken
     * @param cb 回调
     */
    public void count(final String accessToken, Callback<MaterialCount> cb){
        wechat.doAsync(new AsyncFunction<MaterialCount>(cb) {
            @Override
            public MaterialCount execute() {
                return count(accessToken);
            }
        });
    }

    /**
     * 获取素材总数统计
     * @param accessToken accessToken
     * @return 素材总数统计，或抛WechatException
     */
    public MaterialCount count(String accessToken){
        String url = COUNT + accessToken;
        Map<String, Object> resp = wechat.doGet(url);
        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), MaterialCount.class);
    }

    /**
     * 获取素材列表
     * @param type 素材类型
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     * @param count 返回素材的数量，取值在1到20之间
     * @param <T> Material范型
     * @return 素材分页对象，或抛WechatException
     */
    public <T> Page<T> gets(MaterialType type, Integer offset, Integer count){
        return gets(wechat.loadAccessToken(), type, offset, count);
    }

    /**
     * 获取素材列表
     * @param type 素材类型
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     * @param count 返回素材的数量，取值在1到20之间
     * @param <T> Material范型
     * @param cb 回调
     */
    public <T> void gets(final MaterialType type, final Integer offset, final Integer count, Callback<Page<T>> cb){
        gets(wechat.loadAccessToken(), type, offset, count, cb);
    }

    /**
     * 获取素材列表
     * @param accessToken accessToken
     * @param type 素材类型
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     * @param count 返回素材的数量，取值在1到20之间
     * @param <T> Material范型
     * @param cb 回调
     */
    public <T> void gets(final String accessToken, final MaterialType type, final Integer offset, final Integer count, Callback<Page<T>> cb){
        wechat.doAsync(new AsyncFunction<Page<T>>(cb) {
            @Override
            public Page<T> execute() {
                return gets(accessToken, type, offset, count);
            }
        });
    }

    /**
     * 获取素材列表
     * @param accessToken accessToken
     * @param type 素材类型
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材返回
     * @param count 返回素材的数量，取值在1到20之间
     * @param <T> Material范型
     * @return 素材分页对象，或抛WechatException
     */
    public <T> Page<T> gets(String accessToken, MaterialType type, Integer offset, Integer count){
        String url = GETS + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("type", type.value());
        params.put("offset", offset);
        params.put("count", count);

        Map<String, Object> resp = wechat.doPost(url, params);
        return renderMaterialPage(type, resp);
    }

    private <T> Page<T> renderMaterialPage(MaterialType type, Map<String, Object> resp) {
        Integer itemCount = (Integer)resp.get("item_count");
        if (itemCount == null || itemCount <= 0){
            return Page.empty();
        }

        Integer itemTotal = (Integer)resp.get("total_count");

        JavaType materialType = MaterialType.NEWS == type ?
                ARRAY_LIST_NEWS_MATERIAL_TYPE :ARRAY_LIST_COMMON_MATERIAL_TYPE ;
        List<T> materials = Jsons.DEFAULT.fromJson(
                Jsons.DEFAULT.toJson(resp.get("item")), materialType);

        return new Page<>(itemTotal, materials);
    }

    /**
     * 删除永久素材
     * @param mediaId 永久素材mediaId
     * @return 删除成功返回true，或抛WechatException
     */
    public Boolean delete(String mediaId){
        return delete(wechat.loadAccessToken(), mediaId);
    }

    /**
     * 删除永久素材
     * @param mediaId 永久素材mediaId
     * @param cb 回调
     */
    public void delete(final String mediaId, Callback<Boolean> cb){
        delete(wechat.loadAccessToken(), mediaId, cb);
    }

    /**
     * 删除永久素材
     * @param accessToken accessToken
     * @param mediaId 永久素材mediaId
     * @param cb 回调
     */
    public void delete(final String accessToken, final String mediaId, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return delete(accessToken, mediaId);
            }
        });
    }

    /**
     * 删除永久素材
     * @param accessToken accessToken
     * @param mediaId 永久素材mediaId
     * @return 删除成功返回true，或抛WechatException
     */
    public Boolean delete(String accessToken, String mediaId){
        String url = DELETE + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("media_id", mediaId);
        wechat.doPost(url, params);

        return Boolean.TRUE;
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param type 文件类型
     * @param fileName 文件名
     * @param fileData 文件数据
     * @return TempMaterial对象，或抛WechatException
     */
    public TempMaterial uploadTemp(MaterialUploadType type, String fileName, byte[] fileData) {
        return uploadTemp(wechat.loadAccessToken(), type, fileName, new ByteArrayInputStream(fileData));
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param fileData 文件数据
     * @return TempMaterial对象，或抛WechatException
     */
    public TempMaterial uploadTemp(String accessToken, MaterialUploadType type, String fileName, byte[] fileData) {
        return uploadTemp(accessToken, type, fileName, new ByteArrayInputStream(fileData));
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param type 文件类型
     * @param media 媒体文件输入流
     * @return TempMaterial对象，或抛WechatException
     */
    public TempMaterial uploadTemp(MaterialUploadType type, File media) {
        return uploadTemp(wechat.loadAccessToken(), type, media);
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param type 文件类型
     * @param media 媒体文件输入流
     * @return TempMaterial对象，或抛WechatException
     */
    public TempMaterial uploadTemp(String accessToken, MaterialUploadType type, File media) {
        try {
            return uploadTemp(accessToken, type, media.getName(), new FileInputStream(media));
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param type 文件类型
     * @param media 媒体文件输入流
     * @param cb 回调
     */
    public void uploadTemp( MaterialUploadType type, File media, Callback<TempMaterial> cb) {
        try {
            uploadTemp(wechat.loadAccessToken(), type, media.getName(), new FileInputStream(media), cb);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param type 文件类型
     * @param media 媒体文件输入流
     * @param cb 回调
     */
    public void uploadTemp(String accessToken, MaterialUploadType type, File media, Callback<TempMaterial> cb) {
        try {
            uploadTemp(accessToken, type, media.getName(), new FileInputStream(media), cb);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @return TempMaterial对象，或抛WechatException
     */
    public TempMaterial uploadTemp(MaterialUploadType type, String fileName, InputStream input) {
        return uploadTemp(wechat.loadAccessToken(), type, fileName, input);
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @param cb 回调
     */
    public void uploadTemp(final MaterialUploadType type, final String fileName, final InputStream input, Callback<TempMaterial> cb) {
        uploadTemp(wechat.loadAccessToken(), type, fileName, input, cb);
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @param cb 回调
     */
    public void uploadTemp(final String accessToken, final MaterialUploadType type, final String fileName, final InputStream input, Callback<TempMaterial> cb) {
        wechat.doAsync(new AsyncFunction<TempMaterial>(cb) {
            @Override
            public TempMaterial execute() {
                return uploadTemp(accessToken, type, fileName, input);
            }
        });
    }

    /**
     * 上传临时素材:
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     视频（video）：10MB，支持MP4格式
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @return TempMaterial对象，或抛WechatException
     */
    public TempMaterial uploadTemp(String accessToken, MaterialUploadType type, String fileName, InputStream input) {
        String url = UPLOAD_TEMP + accessToken;

        Map<String, String> params = Maps.newHashMapWithExpectedSize(1);
        params.put("type", type.value());

        Map<String, Object> resp = wechat.doUpload(url, "media", fileName, input, params);
        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), TempMaterial.class);
    }

    /**
     * 下载临时素材
     * @param mediaId mediaId
     * @return 文件二进制数据
     */
    public byte[] downloadTemp(String mediaId){
        return downloadTemp(wechat.loadAccessToken(), mediaId);
    }

    /**
     * 下载临时素材
     * @param mediaId mediaId
     * @param cb 回调
     */
    public void downloadTemp(final String mediaId, Callback<byte[]> cb){
        downloadTemp(wechat.loadAccessToken(), mediaId, cb);
    }

    /**
     * 下载临时素材
     * @param accessToken accessToken
     * @param mediaId mediaId
     * @param cb 回调
     */
    public void downloadTemp(final String accessToken, final String mediaId, Callback<byte[]> cb){
        wechat.doAsync(new AsyncFunction<byte[]>(cb) {
            @Override
            public byte[] execute() {
                return downloadTemp(accessToken, mediaId);
            }
        });
    }

    /**
     * 下载临时素材
     * @param accessToken accessToken
     * @param mediaId mediaId
     * @return 文件二进制数据
     */
    public byte[] downloadTemp(String accessToken, String mediaId){
        String url = DOWNLOAD_TEMP + accessToken + "&media_id=" + mediaId;

        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
        Http.download(url, output);

        return output.toByteArray();
    }

    /**
     * 添加永久图文素材
     * @param items 图文素材列表
     * @return mediaId
     */
    public String uploadPermNews(List<NewsContentItem> items){
        return uploadPermNews(wechat.loadAccessToken(), items);
    }

    /**
     * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
     * @param items 图文素材列表
     * @param cb 回调
     */
    public void uploadPermNews(final List<NewsContentItem> items, Callback<String> cb){
        uploadPermNews(wechat.loadAccessToken(), items, cb);
    }

    /**
     * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
     * @param accessToken accessToken
     * @param items 图文素材列表
     * @param cb 回调
     */
    public void uploadPermNews(final String accessToken, final List<NewsContentItem> items, Callback<String> cb){
        wechat.doAsync(new AsyncFunction<String>(cb) {
            @Override
            public String execute() {
                return uploadPermNews(accessToken, items);
            }
        });
    }

    /**
     * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
     * @param accessToken accessToken
     * @param items 图文素材列表
     * @return mediaId
     */
    public String uploadPermNews(String accessToken, List<NewsContentItem> items){
        String url = ADD_NEWS + accessToken;
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("articles", items);
        Map<String, Object> resp = wechat.doPost(url, params);
        return (String)resp.get("media_id");
    }

    /**
     * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
     * @param mediaId 图文mediaId
     * @param itemIndex 对应图文素材中的第几个图文项，从0开始
     * @param newItem 新的图文项
     * @param cb 回调
     */
    public void updatePermNews(final String mediaId, final Integer itemIndex, final NewsContentItem newItem, Callback<Boolean> cb){
        updatePermNews(wechat.loadAccessToken(), mediaId, itemIndex, newItem, cb);
    }

    /**
     * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
     * @param accessToken accessToken
     * @param mediaId 图文mediaId
     * @param itemIndex 对应图文素材中的第几个图文项，从0开始
     * @param newItem 新的图文项
     * @param cb 回调
     */
    public void updatePermNews(final String accessToken, final String mediaId, final Integer itemIndex, final NewsContentItem newItem, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return updatePermNews(accessToken, mediaId, itemIndex, newItem);
            }
        });
    }

    /**
     * 添加永久图文素材(其中内容中的外部图片链接会被过滤，所以需先用uploadPermNewsImage转换为微信内部图片)
     * @param accessToken accessToken
     * @param mediaId 图文mediaId
     * @param itemIndex 对应图文素材中的第几个图文项，从0开始
     * @param newItem 新的图文项
     * @return 更新成功返回true，反之false
     */
    public Boolean updatePermNews(String accessToken, String mediaId, Integer itemIndex, NewsContentItem newItem){
        String url = UPDATE_NEWS + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(3);
        params.put("media_id", mediaId);
        params.put("index", itemIndex);
        params.put("articles", newItem);

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param accessToken accessToken
     * @param image 图片对象
     * @return 微信内部图片链接
     */
    public String uploadPermNewsImage(String accessToken, File image) {
        try {
            return uploadPermNewsImage(accessToken, image.getName(), new FileInputStream(image));
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param image 图片对象
     * @param cb 回调
     */
    public void uploadPermNewsImage(File image, Callback<String> cb) {
        uploadPermNewsImage(wechat.loadAccessToken(), image, cb);
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param accessToken accessToken
     * @param image 图片对象
     * @param cb 回调
     */
    public void uploadPermNewsImage(String accessToken, File image, Callback<String> cb) {
        try {
            uploadPermNewsImage(accessToken, image.getName(), new FileInputStream(image), cb);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param data 文件二机制数据
     * @return 微信内部图片链接
     */
    public String uploadPermNewsImage(String accessToken, String fileName, byte[] data) {
        return uploadPermNewsImage(accessToken, fileName, new ByteArrayInputStream(data));
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param fileName 文件名
     * @param data 文件二机制数据
     * @param cb 回调
     */
    public void uploadPermNewsImage(String fileName, byte[] data, Callback<String> cb) {
        uploadPermNewsImage(wechat.loadAccessToken(), fileName, new ByteArrayInputStream(data), cb);
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param data 文件二机制数据
     * @param cb 回调
     */
    public void uploadPermNewsImage(String accessToken, String fileName, byte[] data, Callback<String> cb) {
        uploadPermNewsImage(accessToken, fileName, new ByteArrayInputStream(data), cb);
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param fileName 文件名
     * @param in 文件输入流
     * @param cb 回调
     */
    public void uploadPermNewsImage(final String fileName, final InputStream in, Callback<String> cb){
        uploadPermNewsImage(wechat.loadAccessToken(), fileName, in, cb);
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param in 文件输入流
     * @param cb 回调
     */
    public void uploadPermNewsImage(final String accessToken, final String fileName, final InputStream in, Callback<String> cb) {
        wechat.doAsync(new AsyncFunction<String>(cb) {
            @Override
            public String execute() throws FileNotFoundException {
                return uploadPermNewsImage(accessToken, fileName, in);
            }
        });
    }

    /**
     * 上传永久图文素材内容中引用的图片
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param in 文件输入流
     * @return 微信内部图片链接
     */
    public String uploadPermNewsImage(String accessToken, String fileName, InputStream in) {
        String url = UPLOAD_NEWS_IMAGE + accessToken;
        Map<String, Object> resp = wechat.doUpload(url, "media", fileName, in, Collections.<String, String>emptyMap());
        return (String)resp.get("url");
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param accessToken accessToken
     * @param type 文件类型
     * @param file 文件
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPerm(String accessToken, MaterialUploadType type, File file) {
        try {
            return uploadPerm(accessToken, type, file.getName(), new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param data 文件二进制数据
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPerm(String accessToken, MaterialUploadType type, String fileName, byte[] data) {
        return uploadPerm(accessToken, type, fileName, new ByteArrayInputStream(data));
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param data 文件二进制数据
     * @param cb 回调
     */
    public void uploadPerm(String accessToken, MaterialUploadType type, String fileName, byte[] data, Callback<PermMaterial> cb) {
        uploadPerm(accessToken, type, fileName, new ByteArrayInputStream(data), cb);
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param type 文件类型
     * @param fileName 文件名
     * @param data 文件二进制数据
     * @param cb 回调
     */
    public void uploadPerm(MaterialUploadType type, String fileName, byte[] data, Callback<PermMaterial> cb) {
        uploadPerm(wechat.loadAccessToken(), type, fileName, new ByteArrayInputStream(data), cb);
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param type 文件类型
     * @param fileName 文件名
     * @param data 文件二进制数据
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPerm( MaterialUploadType type, String fileName, byte[] data) {
        return uploadPerm(wechat.loadAccessToken(), type, fileName, new ByteArrayInputStream(data));
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param type 文件类型
     * @param file 输入流
     * @param cb 回调
     */
    public void uploadPerm(final MaterialUploadType type, final File file, Callback<PermMaterial> cb) {
        uploadPerm(wechat.loadAccessToken(), type, file, cb);
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param accessToken accessToken
     * @param type 文件类型
     * @param file 输入流
     * @param cb 回调
     */
    public void uploadPerm(String accessToken, final MaterialUploadType type, final File file, Callback<PermMaterial> cb) {
        try {
            uploadPerm(accessToken, type, file.getName(), new FileInputStream(file), cb);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @param cb 回调
     */
    public void uploadPerm(final MaterialUploadType type, final String fileName, final InputStream input, Callback<PermMaterial> cb) {
        uploadPerm(wechat.loadAccessToken(), type, fileName, input, cb);
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @param cb 回调
     */
    public void uploadPerm(final String accessToken, final MaterialUploadType type, final String fileName, final InputStream input, Callback<PermMaterial> cb) {
        wechat.doAsync(new AsyncFunction<PermMaterial>(cb) {
            @Override
            public PermMaterial execute() throws Exception {
                return uploadPerm(accessToken, type, fileName, input);
            }
        });
    }

    /**
     * 上传永久(图片，语音，缩略图)素材
     永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     图片（image）: 1M，bmp/png/jpeg/jpg/gif
     语音（voice）：2M，播放长度不超过60s，mp3/wma/wav/amr
     缩略图（thumb）：64KB，bmp/png/jpeg/jpg/gif
     * @param accessToken accessToken
     * @param type 文件类型
     * @param fileName 文件名
     * @param input 输入流
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPerm(String accessToken, MaterialUploadType type, String fileName, InputStream input) {
        if (MaterialUploadType.VIDEO == type){
            throw new IllegalArgumentException("type must be image, voice, or thumb, you should use uploadPermVideo method.");
        }
        String url = UPLOAD_PERM + accessToken;

        Map<String, String> params = Maps.newHashMapWithExpectedSize(1);
        params.put("type", type.value());

        Map<String, Object> resp = wechat.doUpload(url, "media", fileName, input, params);
        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), PermMaterial.class);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param accessToken accessToken
     * @param video 视频文件
     * @param title 标题
     * @param desc 描述
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPermVideo(String accessToken, File video, String title, String desc) {
        try {
            return uploadPermVideo(accessToken, video.getName(), new FileInputStream(video), title, desc);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param video 视频文件
     * @param title 标题
     * @param desc 描述
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPermVideo(File video, String title, String desc) {
        return uploadPermVideo(wechat.loadAccessToken(), video, title, desc);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param data 二进制数据
     * @param title 标题
     * @param desc 描述
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPermVideo(String accessToken, String fileName, byte[] data, String title, String desc) {
        return uploadPermVideo(accessToken, fileName, new ByteArrayInputStream(data), title, desc);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param fileName 文件名
     * @param data 二进制数据
     * @param title 标题
     * @param desc 描述
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPermVideo(String fileName, byte[] data, String title, String desc) {
        return uploadPermVideo(wechat.loadAccessToken(), fileName, new ByteArrayInputStream(data), title, desc);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param data 二进制数据
     * @param title 标题
     * @param desc 描述
     * @param cb 回调
     */
    public void uploadPermVideo(String accessToken, String fileName, byte[] data, final String title, final String desc, Callback<PermMaterial> cb) {
        uploadPermVideo(accessToken, fileName, new ByteArrayInputStream(data), title, desc, cb);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param fileName 文件名
     * @param data 二进制数据
     * @param title 标题
     * @param desc 描述
     * @param cb 回调
     */
    public void uploadPermVideo(String fileName, byte[] data, final String title, final String desc, Callback<PermMaterial> cb) {
        uploadPermVideo(wechat.loadAccessToken(), fileName, new ByteArrayInputStream(data), title, desc, cb);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param video 文件
     * @param title 标题
     * @param desc 描述
     * @param cb 回调
     */
    public void uploadPermVideo(final File video, final String title, final String desc, Callback<PermMaterial> cb) {
        uploadPermVideo(wechat.loadAccessToken(), video, title, desc, cb);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param accessToken accessToken
     * @param video 文件
     * @param title 标题
     * @param desc 描述
     * @param cb 回调
     */
    public void uploadPermVideo(final String accessToken, final File video, final String title, final String desc, Callback<PermMaterial> cb) {
        try {
            uploadPermVideo(accessToken, video.getName(), new FileInputStream(video), title, desc, cb);
        } catch (FileNotFoundException e) {
            throw new WechatException(e);
        }
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param fileName 文件名
     * @param input 输入流
     * @param title 标题
     * @param desc 描述
     * @param cb 回调
     */
    public void uploadPermVideo(final String fileName, final InputStream input, final String title, final String desc, Callback<PermMaterial> cb) {
        uploadPermVideo(wechat.loadAccessToken(), fileName, input, title, desc, cb);
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param input 输入流
     * @param title 标题
     * @param desc 描述
     * @param cb 回调
     */
    public void uploadPermVideo(final String accessToken, final String fileName, final InputStream input, final String title, final String desc, Callback<PermMaterial> cb) {
        wechat.doAsync(new AsyncFunction<PermMaterial>(cb) {
            @Override
            public PermMaterial execute() throws Exception {
                return uploadPermVideo(accessToken, fileName, input, title, desc);
            }
        });
    }

    /**
     * 上传永久视频素材(10M大小)
     * @param accessToken accessToken
     * @param fileName 文件名
     * @param input 输入流
     * @param title 标题
     * @param desc 描述
     * @return PermMaterial对象，或抛WechatException
     */
    public PermMaterial uploadPermVideo(String accessToken, String fileName, InputStream input, String title, String desc) {

        String url = UPLOAD_PERM + accessToken;

        Map<String, String> params = Maps.newHashMapWithExpectedSize(2);
        params.put("type", MaterialUploadType.VIDEO.value());

        Map<String, String> description = Maps.newHashMapWithExpectedSize(2);
        description.put("title", title);
        description.put("introduction", desc);
        params.put("description", Jsons.DEFAULT.toJson(description));

        Map<String, Object> resp = wechat.doUpload(url, "media", fileName, input, params);
        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), PermMaterial.class);
    }
}
