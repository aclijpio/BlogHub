package com.github.aclijpio.bloghub.utils;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionPool {
    Connection getConnection() throws SQLException;
}
