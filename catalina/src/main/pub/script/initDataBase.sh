#!/bin/bash

DB_NAME="ssoservicedb"
SQL_SCRIPT=$APP_ROOT/etc/database/ssoservice.sql
DATASOURCE=$APP_ROOT/etc/datasource/datasource.properties
CLASS_PATH_TEMP="$APP_ROOT/webapps/ROOT/WEB-INF/classes"
EXT_DIR="$APP_ROOT/webapps/ROOT/WEB-INF/lib:$JAVA_HOME/jre/lib/ext"
DB_INIT_OPTS="-Dinit.sqlScript=$SQL_SCRIPT"
DB_INIT_OPTS="$DB_INIT_OPTS -Dinit.dataSource=$DATASOURCE"
DB_INIT_OPTS="$DB_INIT_OPTS -Dinit.dbName=$DB_NAME"
java $DB_INIT_OPTS -Djava.ext.dirs=$EXT_DIR -classpath $CLASS_PATH_TEMP com.demo.init.DBInitializer