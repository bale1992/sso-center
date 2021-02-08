package com.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public final class DataBaseInitUtil {

    private static Properties jdbcProperties;

    private static final String CREATE_DATABASE_SQL = "create database if not exists ";

    private DataBaseInitUtil() {
    }

    public static void setJdbcProperties(Properties properties) {
        DataBaseInitUtil.jdbcProperties = properties;
    }

    public static void createDataBase(String dbName) {
        // todo 这里用占位符抛异常, 不知道为啥
        try (Connection connection = DriverManager.getConnection(jdbcProperties.getProperty("url"), jdbcProperties);
             PreparedStatement statement = connection.prepareStatement(CREATE_DATABASE_SQL + dbName)) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            log.error("Create database exception: {}", ex.getMessage());
        }
    }

    public static void createTable(String sqlScriptPath) {
        try (final Connection connection = DriverManager.getConnection(jdbcProperties.getProperty("url"), jdbcProperties);
             final FileReader fileReader = new FileReader(sqlScriptPath)) {
            final ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setLogWriter(null);
            scriptRunner.runScript(fileReader);
        } catch (SQLException sqlException) {
            log.error("Create database sqlException: {}", sqlException.getMessage());
        } catch (IOException ioException) {
            log.error("Create database ioException: {}", ioException.getMessage());
        }
    }
}
