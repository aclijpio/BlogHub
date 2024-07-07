package com.github.aclijpio.bloghub.database.util;

import com.github.aclijpio.bloghub.database.util.source.AppConfig;
import com.github.aclijpio.bloghub.database.util.source.Datasource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public enum DefaultConnectionPool {
    UTIL;

    private static final HikariDataSource dataSource;

    static {
        final AppConfig loadedConfig = ConfigLoader.LOADER.loadConfig();
        final Datasource datasource = loadedConfig.getDatasource();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(datasource.getUrl());
        config.setUsername(datasource.getUsername());
        config.setPassword(datasource.getPassword());
        config.setDriverClassName(datasource.getDriver());

        dataSource = new HikariDataSource(config);
    }
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
