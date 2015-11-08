package org.haolin.wechat.utils;

import com.fasterxml.jackson.databind.JavaType;
import org.haolin.wechat.model.menu.Menu;
import org.haolin.wechat.model.user.Group;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public final class Types {

    /**
     * Map<String, Object>
     */
    public static final JavaType MAP_STRING_OBJ_TYPE = Jsons.DEFAULT.createCollectionType(Map.class, String.class, Object.class);

    /**
     * Map<String, ArrayList<String>>
     */
    public static final JavaType MAP_STRING_ARRAY_LIST_TYPE = Jsons.DEFAULT.createCollectionType(Map.class, String.class, ArrayList.class);

    /**
     * Map<String, String>
     */
    public static final JavaType MAP_STRING_STRING_TYPE = Jsons.DEFAULT.createCollectionType(Map.class, String.class, String.class);

    /**
     * ArrayList<String>
     */
    public static final JavaType ARRAY_LIST_STRING_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, String.class);

    /**
     * ArrayList<Menu>
     */
    public static final JavaType ARRAY_LIST_MENU_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Menu.class);

    /**
     * ArrayList<Group>
     */
    public static final JavaType ARRAY_LIST_GROUP_TYPE = Jsons.DEFAULT.createCollectionType(ArrayList.class, Group.class);

}
