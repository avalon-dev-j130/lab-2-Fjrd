package ru.avalon.java.j30.labs;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DBConnectionProvider {
    private static final String configsPath = "resources/config.properties";
    private final Properties configs = new Properties();

    public DBConnectionProvider() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream stream = classLoader.getResourceAsStream(configsPath)){
            configs.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getConfigs() {
        return configs;
    }
}
