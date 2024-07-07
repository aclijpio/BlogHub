package com.github.aclijpio.bloghub.database.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.aclijpio.bloghub.database.exceptions.AppConfigException;
import com.github.aclijpio.bloghub.database.util.source.AppConfig;

import java.io.File;
import java.io.IOException;

public enum ConfigLoader {

    LOADER;

    private static final String CONFIG_FILE = "config.yaml";

    private static AppConfig appConfig;


    public AppConfig loadConfig() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        File file = new File(classLoader.getResource("config.yaml").getFile());
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            return om.readValue(file, AppConfig.class);
        }catch (IOException e){
            throw new AppConfigException("Failed to find resource", e);
        }
    }
}
