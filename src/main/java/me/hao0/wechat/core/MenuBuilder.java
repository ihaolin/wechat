package me.hao0.wechat.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.menu.MenuType;
import me.hao0.common.json.Jsons;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单构建器
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public final class MenuBuilder {

    @JsonProperty("button")
    private List<Menu> menus = new ArrayList<>();

    private MenuBuilder() {
    }

    public static MenuBuilder newBuilder() {
        return new MenuBuilder();
    }

    /**
     * 创建一个一级菜单
     *
     * @param m 菜单对象
     * @return this
     */
    public MenuBuilder menu(Menu m) {
        menus.add(m);
        return this;
    }

    /**
     * 创建一个CLICK一级菜单
     *
     * @param name 名称
     * @param key  键
     * @return this
     */
    public MenuBuilder click(String name, String key) {
        Menu m = newClickMenu(name, key);
        menus.add(m);
        return this;
    }

    /**
     * 创建一个CLICK二级菜单
     *
     * @param parent 父级菜单
     * @param name   名称
     * @param key    键
     * @return this
     */
    public MenuBuilder click(Menu parent, String name, String key) {
        Menu m = newClickMenu(name, key);
        parent.getChildren().add(m);
        return this;
    }

    /**
     * 创建一个VIEW一级菜单
     *
     * @param name 名称
     * @param url  链接
     * @return this
     */
    public MenuBuilder view(String name, String url) {
        Menu m = newViewMenu(name, url);
        menus.add(m);
        return this;
    }

    /**
     * 创建一个VIEW二级菜单
     *
     * @param parent 父级菜单
     * @param name   名称
     * @param url    链接
     * @return this
     */
    public MenuBuilder view(Menu parent, String name, String url) {
        Menu m = newViewMenu(name, url);
        parent.getChildren().add(m);
        return this;
    }

    /**
     * 创建一个VIEW菜单
     *
     * @param name 名称
     * @param url  链接
     * @return Menu对象
     */
    public Menu newViewMenu(String name, String url) {
        Menu m = new Menu();
        m.setName(name);
        m.setType(MenuType.VIEW.value());
        m.setUrl(url);
        return m;
    }

    /**
     * 创建一个CLICK菜单
     *
     * @param name 名称
     * @param key  键
     * @return Menu对象
     */
    public Menu newClickMenu(String name, String key) {
        Menu m = new Menu();
        m.setName(name);
        m.setType(MenuType.CLICK.value());
        m.setKey(key);
        return m;
    }

    /**
     * 创建一个一级菜单
     *
     * @param name 名称
     * @return Menu对象
     */
    public Menu newParentMenu(String name) {
        Menu m = new Menu();
        m.setName(name);
        return m;
    }

    /**
     * 返回菜单的json数据
     *
     * @return 菜单json数据
     */
    public String build() {
        return Jsons.EXCLUDE_EMPTY.toJson(this);
    }
}