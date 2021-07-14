package controller.webserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GsonHelper {
    private static final Gson gson = new Gson();

    private static Type getMapType(Class<?> keyType, Class<?> valueType) {
        return TypeToken.getParameterized(HashMap.class, keyType, valueType).getType();
    }

    public static <K, V> HashMap<K, V> fromMap(String json, Class<K> keyType, Class<V> valueType) {
        return gson.fromJson(json, getMapType(keyType, valueType));
    }
}
