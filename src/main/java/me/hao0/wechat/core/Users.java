package me.hao0.wechat.core;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Maps;
import me.hao0.wechat.model.user.Group;
import me.hao0.wechat.model.user.User;
import me.hao0.wechat.utils.Jsons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 */
public class Users extends Component {

    /**
     * 创建用户分组
     */
    private static final String CREATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";

    /**
     * 获取用户分组列表
     */
    private static final String GET_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";

    /**
     * 删除分组
     */
    private static final String DELETE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=";

    /**
     * 更新分组名称
     */
    private static final String UPDATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";

    /**
     * 获取用户所在分组
     */
    private static final String GROUP_OF_USER = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=";

    /**
     * 移动用户所在组
     */
    private static final String MOVE_USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";

    /**
     * 拉取用户信息
     */
    private static final String GET_USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&access_token=";

    /**
     * 备注用户
     */
    private static final String REMARK_USER = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=";

    private static final JavaType ARRAY_LIST_GROUP_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Group.class);

    Users(){}

    /**
     * 创建用户分组
     * @param name 名称
     * @return 分组ID，或抛WechatException
     */
    public Integer createGroup(String name){
        return createGroup(wechat.loadAccessToken(), name);
    }

    /**
     * 创建用户分组
     * @param name 名称
     * @param cb 回调
     */
    public void createGroup(final String name, Callback<Integer> cb){
        createGroup(wechat.loadAccessToken(), name, cb);
    }

    /**
     * 创建用户分组
     * @param accessToken accessToken
     * @param name 名称
     * @param cb 回调
     */
    public void createGroup(final String accessToken, final String name, Callback<Integer> cb){
        wechat.doAsync(new AsyncFunction<Integer>(cb) {
            @Override
            public Integer execute() {
                return createGroup(accessToken, name);
            }
        });
    }

    /**
     * 创建用户分组
     * @param accessToken accessToken
     * @param name 名称
     * @return 分组ID，或抛WechatException
     */
    public Integer createGroup(String accessToken, String name){
        String url = CREATE_GROUP + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        Group g = new Group();
        g.setName(name);
        params.put("group", g);

        Map<String, Object> resp = wechat.doPost(url, params);
        return (Integer)((Map)resp.get("group")).get("id");
    }

    /**
     * 获取所有分组列表
     * @return 分组列表，或抛WechatException
     */
    public List<Group> getGroup(){
        return getGroup(wechat.loadAccessToken());
    }

    /**
     * 获取所有分组列表
     * @param accessToken accessToken
     * @param cb 回调
     */
    public void getGroup(final String accessToken, Callback<List<Group>> cb){
        wechat.doAsync(new AsyncFunction<List<Group>>(cb) {
            @Override
            public List<Group> execute() {
                return getGroup(accessToken);
            }
        });
    }

    /**
     * 获取所有分组列表
     * @param accessToken accessToken
     * @return 分组列表，或抛WechatException
     */
    public List<Group> getGroup(String accessToken){
        String url = GET_GROUP + accessToken;

        Map<String, Object> resp = wechat.doGet(url);

        return Jsons.EXCLUDE_DEFAULT
                .fromJson(Jsons.DEFAULT.toJson(resp.get("groups")), ARRAY_LIST_GROUP_TYPE);
    }

    /**
     * 删除分组
     * @param id 分组ID
     * @return 删除成功返回true，或抛WechatException
     */
    public Boolean deleteGroup(Integer id){
        return deleteGroup(wechat.loadAccessToken(), id);
    }

    /**
     * 删除分组
     * @param id 分组ID
     * @param cb 回调
     */
    public void deleteGroup(final Integer id, Callback<Boolean> cb){
        deleteGroup(wechat.loadAccessToken(), id, cb);
    }

    /**
     * 删除分组
     * @param accessToken accessToken
     * @param id 分组ID
     * @param cb 回调
     */
    public void deleteGroup(final String accessToken, final Integer id, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return deleteGroup(accessToken, id);
            }
        });
    }

    /**
     * 删除分组
     * @param accessToken accessToken
     * @param id 分组ID
     * @return 删除成功返回true，或抛WechatException
     */
    public Boolean deleteGroup(String accessToken, Integer id){
        String url = DELETE_GROUP + accessToken;

        Group g = new Group();
        g.setId(id);
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("group", g);

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 更新分组名称
     * @param id 分组ID
     * @param newName 分组新名称
     * @return 更新成功返回true，或抛WechatException
     */
    public Boolean updateGroup(Integer id, String newName){
        return updateGroup(wechat.loadAccessToken(), id, newName);
    }

    /**
     * 更新分组名称
     * @param id 分组ID
     * @param newName 分组新名称
     * @param cb 回调
     */
    public void updateGroup(final Integer id, final String newName, Callback<Boolean> cb){
        updateGroup(wechat.loadAccessToken(), id, newName, cb);
    }

    /**
     * 更新分组名称
     * @param accessToken accessToken
     * @param id 分组ID
     * @param newName 分组新名称
     * @param cb 回调
     */
    public void updateGroup(final String accessToken, final Integer id, final String newName, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return updateGroup(accessToken, id, newName);
            }
        });
    }

    /**
     * 更新分组名称
     * @param accessToken accessToken
     * @param id 分组ID
     * @param newName 分组新名称
     * @return 更新成功返回true，或抛WechatException
     */
    public Boolean updateGroup(String accessToken, Integer id, String newName){
        String url = UPDATE_GROUP + accessToken;

        Group g = new Group();
        g.setId(id);
        g.setName(newName);
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("group", g);

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 获取用户所在组
     * @param openId 用户openId
     * @return 组ID，或抛WechatException
     */
    public Integer getUserGroup(String openId){
        return getUserGroup(wechat.loadAccessToken(), openId);
    }

    /**
     * 获取用户所在组
     * @param openId 用户openId
     * @param cb 回调
     */
    public void getUserGroup(final String openId, Callback<Integer> cb){
        getUserGroup(wechat.loadAccessToken(), openId, cb);
    }

    /**
     * 获取用户所在组
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param cb 回调
     */
    public void getUserGroup(final String accessToken, final String openId, Callback<Integer> cb){
        wechat.doAsync(new AsyncFunction<Integer>(cb) {
            @Override
            public Integer execute() {
                return getUserGroup(accessToken, openId);
            }
        });
    }

    /**
     * 获取用户所在组
     * @param accessToken accessToken
     * @param openId 用户openId
     * @return 组ID，或抛WechatException
     */
    public Integer getUserGroup(String accessToken, String openId){
        String url = GROUP_OF_USER + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
        params.put("openid", openId);

        Map<String, Object> resp = wechat.doPost(url, params);
        return (Integer)resp.get("groupid");
    }

    /**
     * 移动用户所在组
     * @param openId 用户openId
     * @param groupId 新组ID
     * @return 移动成功返回true，或抛WechatException
     */
    public Boolean mvUserGroup(String openId, Integer groupId){
        return mvUserGroup(wechat.loadAccessToken(), openId, groupId);
    }

    /**
     * 移动用户所在组
     * @param openId 用户openId
     * @param groupId 新组ID
     * @param cb 回调
     */
    public void mvUserGroup(final String openId, final Integer groupId, Callback<Boolean> cb){
        mvUserGroup(wechat.loadAccessToken(), openId, groupId, cb);
    }

    /**
     * 移动用户所在组
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param groupId 新组ID
     * @param cb 回调
     */
    public void mvUserGroup(final String accessToken, final String openId, final Integer groupId, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return mvUserGroup(accessToken, openId, groupId);
            }
        });
    }

    /**
     * 移动用户所在组
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param groupId 新组ID
     * @return 移动成功返回true，或抛WechatException
     */
    public Boolean mvUserGroup(String accessToken, String openId, Integer groupId){
        String url = MOVE_USER_GROUP + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("openid", openId);
        params.put("to_groupid", groupId);

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }

    /**
     * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
     * @param openId 用户openId
     * @return 用户信息，或抛WechatException
     */
    public User getUser(String openId){
        return getUser(wechat.loadAccessToken(), openId);
    }

    /**
     * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
     * @param openId 用户openId
     * @param cb 回调
     */
    public void getUser(final String openId, Callback<User> cb){
        getUser(wechat.loadAccessToken(), openId, cb);
    }

    /**
     * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param cb 回调
     */
    public void getUser(final String accessToken, final String openId, Callback<User> cb){
        wechat.doAsync(new AsyncFunction<User>(cb) {
            @Override
            public User execute() {
                return getUser(accessToken, openId);
            }
        });
    }

    /**
     * 拉取用户信息(若用户未关注，且未授权，将拉取不了信息)
     * @param accessToken accessToken
     * @param openId 用户openId
     * @return 用户信息，或抛WechatException
     */
    public User getUser(String accessToken, String openId){
        String url = GET_USER_INFO + accessToken + "&openid=" + openId;

        Map<String, Object> resp = wechat.doGet(url);

        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(resp), User.class);
    }

    /**
     * 备注用户
     * @param openId 用户openId
     * @param remark 备注
     * @return 备注成功返回true，或抛WechatException
     */
    public Boolean remarkUser(String openId, String remark){
        return remarkUser(wechat.loadAccessToken(), openId, remark);
    }

    /**
     * 备注用户
     * @param openId 用户openId
     * @param remark 备注
     * @param cb 回调
     */
    public void remarkUser(final String openId, final String remark, Callback<Boolean> cb){
        remarkUser(wechat.loadAccessToken(), openId, remark, cb);
    }

    /**
     * 备注用户
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param remark 备注
     * @param cb 回调
     */
    public void remarkUser(final String accessToken, final String openId, final String remark, Callback<Boolean> cb){
        wechat.doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return remarkUser(accessToken, openId, remark);
            }
        });
    }

    /**
     * 备注用户
     * @param accessToken accessToken
     * @param openId 用户openId
     * @param remark 备注
     * @return 备注成功返回true，或抛WechatException
     */
    public Boolean remarkUser(String accessToken, String openId, String remark){
        String url = REMARK_USER + accessToken;

        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("openid", openId);
        params.put("remark", remark);

        wechat.doPost(url, params);
        return Boolean.TRUE;
    }
}
