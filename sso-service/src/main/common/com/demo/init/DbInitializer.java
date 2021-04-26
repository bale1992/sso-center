package com.demo.init;

import com.demo.util.DataBaseInitUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

/**
 * @author bale
 */
@Slf4j
public class DbInitializer {

    public static void main(String[] args) {
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
