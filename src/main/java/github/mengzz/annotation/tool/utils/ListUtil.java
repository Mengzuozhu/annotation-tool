package github.mengzz.annotation.tool.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mengzz
 **/
public class ListUtil {
    /**
     * Copy.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @param cls  the cls
     * @return the list
     */
    public static <T> List<T> copy(List<T> data, Class<T> cls) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        List<JsonObject> jsonObjects = gson.fromJson(json, type);
        return jsonObjects.stream()
                .map(jsonObject -> gson.fromJson(jsonObject, cls))
                .collect(Collectors.toList());
    }
}
