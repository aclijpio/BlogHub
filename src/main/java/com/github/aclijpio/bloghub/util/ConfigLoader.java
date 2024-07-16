package com.github.aclijpio.bloghub.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.aclijpio.bloghub.util.exceptions.AppConfigException;
import com.github.aclijpio.bloghub.util.source.AppConfig;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
/**
 * Класс ConfigLoader используется для загрузки конфигурационного файла в формате YAML.
 */
@Log
public enum ConfigLoader {

    LOADER;

    //Название конфигурационного файла
    private static final String CONFIG_FILE = "config.yaml";

    // Экземпляр класса AppConfig, загруженного из конфигурационного файла.
    private static final AppConfig appConfig = loadConfig();

    /**
     * @return экземпляр класса AppConfig, загруженного из конфигурационного файла.
     */
    public AppConfig getConfig(){
        return appConfig;
    }

    /**
     * Загружает конфигурационный файл и преобразует его в объект AppConfig.
     * @return объект AppConfig, содержащий настройки из конфигурационного файла
     * @throws AppConfigException если конфигурационный файл не найден или произошла ошибка при чтении файла
     */
    private static AppConfig loadConfig() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        URL resource = classLoader.getResource(CONFIG_FILE);
        if (resource == null) {
            throw new AppConfigException("Failed to find resource: " + CONFIG_FILE);
        }

        File file = new File(resource.getFile());
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        om.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return om.readValue(file, AppConfig.class);
        } catch (IOException e){
            throw new AppConfigException("Failed to find resource", e);
        } finally {
            log.info("Loaded config: " + file);
        }
    }
}
