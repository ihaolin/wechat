package me.hao0.wechat.utils;

import com.fasterxml.jackson.databind.JavaType;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.user.Group;
import java.util.ArrayList;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public final class Types {

    public static final JavaType MAP_STRING_OBJ_TYPE = Jsons.DEFAULT.createCollectionType(Map.class, String.class, Object.class);

    public static final JavaType ARRAY_LIST_MENU_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Menu.class);

    public static final JavaType ARRAY_LIST_GROUP_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Group.class);

}
