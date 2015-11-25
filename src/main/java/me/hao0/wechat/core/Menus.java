package me.hao0.wechat.core;

import com.fasterxml.jackson.databind.JavaType;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.common.json.Jsons;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单组件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class Menus extends Component {

    /**
     * 查询菜单
     */
    private static final String GET = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=";

    /**
     * 创建菜单
     */
    private static final String CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

    /**
     * 删除菜单
     */
    private static final String DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";

    private static final JavaType ARRAY_LIST_MENU_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Menu.class);

    Menus(){}

    /**
     * 查询菜单
     * @return 菜单列表
     */
    public List<Menu> get(){
        return get(loadAccessToken());
    }

    /**
     * 查询菜单
     * @param cb 回调
     */
    public void get(Callback<List<Menu>> cb){
        get(loadAccessToken(), cb);
    }

    /**
     * 查询菜单
     * @param accessToken accessToken
     * @param cb 回调
     */
    public void get(final String accessToken, Callback<List<Menu>> cb){
        doAsync(new AsyncFunction<List<Menu>>(cb) {
            @Override
            public List<Menu> execute() {
                return get(accessToken);
            }
        });
    }

    /**
     * 查询菜单
     * @param accessToken accessToken
     * @return 菜单列表
     */
    public List<Menu> get(String accessToken){
        String url = GET + accessToken;
        Map<String, Object> resp =  doGet(url);
        String jsonMenu = Jsons.DEFAULT.toJson(((Map) resp.get("menu")).get("button"));
        return Jsons.EXCLUDE_DEFAULT.fromJson(jsonMenu, ARRAY_LIST_MENU_TYPE);
    }

    /**
     * 创建菜单
     * @param jsonMenu 菜单json
     * @return 创建成功返回true，或抛WechatException
     */
    public Boolean create(String jsonMenu){
        return create(loadAccessToken(), jsonMenu);
    }

    /**
     * 创建菜单
     * @param accessToken 访问token
     * @param jsonMenu 菜单json
     * @param cb 回调
     */
    public void create(final String accessToken, final String jsonMenu, Callback<Boolean> cb){
        doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return create(accessToken, jsonMenu);
            }
        });
    }

    /**
     * 创建菜单
     * @param jsonMenu 菜单json
     * @param cb 回调
     */
    public void create(final String jsonMenu, Callback<Boolean> cb){
        create(loadAccessToken(), jsonMenu, cb);
    }

    /**
     * 创建菜单
     * @param accessToken 访问token
     * @param jsonMenu 菜单json
     * @return 创建成功返回true，或抛WechatException
     */
    public Boolean create(String accessToken, String jsonMenu){
        String url = CREATE + accessToken;
        doPost(url, jsonMenu);
        return Boolean.TRUE;
    }

    /**
     * 删除菜单
     * @return 删除成功返回true，或抛WechatException
     */
    public Boolean delete(){
        return delete(loadAccessToken());
    }

    /**
     * 删除菜单
     * @param cb 回调
     */
    public void delete(Callback<Boolean> cb){
        delete(loadAccessToken(), cb);
    }

    /**
     * 删除菜单
     * @param accessToken accessToken
     * @param cb 回调
     */
    public void delete(final String accessToken, Callback<Boolean> cb){
        doAsync(new AsyncFunction<Boolean>(cb) {
            @Override
            public Boolean execute() {
                return delete(accessToken);
            }
        });
    }

    /**
     * 删除菜单
     * @param accessToken accessToken
     * @return 删除成功返回true，或抛WechatException
     */
    public Boolean delete(String accessToken){
        String url = DELETE + accessToken;
        doGet(url);
        return Boolean.TRUE;
    }
}
