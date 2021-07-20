import kong.unirest.Unirest;
import webserver.GsonManager;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Config config = readConfig();
        Unirest.config().defaultBaseUrl(config.getUrl());
        ApplicationManager.run(config.getToken());
    }

    private static Config readConfig() {
        try {
            FileReader reader = new FileReader("config.json");
            return GsonManager.getGson().fromJson(reader, Config.class);
        } catch (IOException ex) {
            throw new RuntimeException("cannot read config file");
        }
    }
}
