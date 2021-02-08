package com.demo.init;

import com.demo.util.DataBaseInitUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class DBInitializer {

    public static void main(String[] args) {
        // 读取JDBC参数
        final String dataSource = System.getProperty("init.dataSource");
        if (Objects.nonNull(dataSource)) {
            try (final FileInputStream fileInputStream = new FileInputStream(dataSource);
                 final InputStreamReader reader = new InputStreamReader(fileInputStream)) {
                final Properties properties = new Properties();
                properties.load(reader);
                DataBaseInitUtil.setJdbcProperties(properties);
            } catch (IOException ex) {
                log.error("Set datasource properties exception: {}", ex.getMessage());
                return;
            }
        }

        // 创建database
        final String dbName = System.getProperty("init.dbName");
        if (Objects.nonNull(dbName)) {
            DataBaseInitUtil.createDataBase(dbName);
        }

        // 创建数据库表
        final String sqlScript = System.getProperty("init.sqlScript");
        if (Objects.nonNull(sqlScript)) {
            DataBaseInitUtil.createTable(sqlScript);
        }
    }
}
