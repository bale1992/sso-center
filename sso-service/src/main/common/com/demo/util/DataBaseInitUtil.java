package com.demo.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.cj.jdbc.Driver;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * @author bale
 */
@Slf4j
public final class DataBaseInitUtil {

    private static final String CREATE_DATABASE_SQL = "create database if not exists ";

    private DataBaseInitUtil() {
    }

    public static void createDataBase(String dbName) {
        loadProperties().ifPresent((properties) -> {
            // todo 这里用占位符抛异常, 不知道为啥
            try (Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties);
                 PreparedStatement statement = connection.prepareStatement(CREATE_DATABASE_SQL + dbName)) {
                statement.executeUpdate();
            } catch (SQLException ex) {
                log.error("Create database exception: {}", ex.getMessage());
            }
        });
    }

    public static void createTable(String sqlScriptPath) {
        loadProperties().ifPresent((properties -> {
            try (final Connection connection = DriverManager.getConnection(properties.getProperty("url"), properties);
                 final FileReader fileReader = new FileReader(sqlScriptPath)) {
                final ScriptRunner scriptRunner = new ScriptRunner(connection);
                scriptRunner.setLogWriter(null);
                scriptRunner.runScript(fileReader);
            } catch (SQLException sqlException) {
                log.error("Create database sqlException: {}", sqlException.getMessage());
            } catch (IOException ioException) {
                log.error("Create database ioException: {}", ioException.getMessage());
            }
        }));
    }

    public static DataSource createDataSource() {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        loadProperties().ifPresent((properties -> {
            dataSource.setUser(properties.getProperty("user"));
            dataSource.setPassword(properties.getProperty("password"));
            dataSource.setJdbcUrl(properties.getProperty("url"));
            try {
                dataSource.setDriverClass(Driver.class.getName());
            } catch (PropertyVetoException e) {
                log.error("Set dataSource driver class exception, className: {}", Driver.class.getName());
            }
        }));
        return dataSource;
    }

    private static Optional<Properties> loadProperties() {
        Properties properties = null;
        final String dataSource = System.getProperty("init.dataSource");
        if (Objects.nonNull(dataSource)) {
            try (final FileInputStream fileInputStream = new FileInputStream(dataSource);
                 final InputStreamReader reader = new InputStreamReader(fileInputStream)) {
                properties = new Properties();
                properties.load(reader);
            } catch (IOException ex) {
                log.error("Set datasource properties exception: {}", ex.getMessage());
            }
        }
        return Optional.ofNullable(properties);
    }
}
