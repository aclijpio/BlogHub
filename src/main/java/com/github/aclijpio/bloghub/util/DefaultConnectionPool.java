package com.github.aclijpio.bloghub.util;

import com.github.aclijpio.bloghub.util.source.AppConfig;
import com.github.aclijpio.bloghub.util.source.Datasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.SQLException;
@Log
public enum DefaultConnectionPool {
    UTIL;

    private static final HikariDataSource dataSource;

    static {
        Datasource ds = null;
        try {

            final AppConfig loadedConfig =  ConfigLoader.LOADER.getConfig();
            ds = loadedConfig.getDatasource();

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(ds.getUrl());
            config.setUsername(ds.getUsername());
            config.setPassword(ds.getPassword());
            config.setDriverClassName(ds.getDriver());

            dataSource = new HikariDataSource(config);
        } finally {
            if (ds != null){
                log.info("""
                    Loaded datasource with: \
                    \turl: %s
                    \tusername: %s
                    \tpassword: %s
                    \tdriverClass: %s""".formatted(ds.getUrl(), ds.getUsername(), ds.getPassword(), ds.getDriver()));
            }

        }
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
