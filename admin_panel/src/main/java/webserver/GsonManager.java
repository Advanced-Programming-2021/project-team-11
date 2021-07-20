package webserver;

import com.google.gson.Gson;

public class GsonManager {
    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
