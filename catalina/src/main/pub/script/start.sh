#!/bin/bash

#APP名称
export APP_NAME=SSOCenter

#APP根目录/usr/bale/deploy/SSOCenter
export APP_ROOT=/usr/bale/deploy/$APP_NAME

#APP进程名
export APP_PROCESS_NAME=ssocenter

#tomcat工作目录/usr/bale/deploy/SSOCenter
export CATALINA_BASE=$APP_ROOT

#tomcat日志目录
CATALINA_LOG_DIR=$CATALINA_BASE/logs
if [ ! -d $CATALINA_LOG_DIR ];then
  mkdir -p $CATALINA_LOG_DIR
fi
export CATALINA_OUT=$CATALINA_LOG_DIR/catalina.out

#初始化数据库
bash $APP_ROOT/script/initDataBase.sh

#创建topic
bash $APP_ROOT/script/createTopic.sh

#JAVA启动参数
HOST_IP=$(hostname --all-ip-addresses | awk '{print $1}')
DATASOURCE=$APP_ROOT/etc/datasource/datasource.properties
BOOTSTRAP_SERVER=$HOST_IP:9091,$HOST_IP:9092,$HOST_IP:9093
JAVA_OPTS="-Dfile.encoding=UTF-8 -DBOOTSTRAP_SERVER=${BOOTSTRAP_SERVER} -Dinit.dataSource=$DATASOURCE"
JAVA_OPTS="$JAVA_OPTS -DNFW=$APP_PROCESS_NAME -Dprocname=$APP_PROCESS_NAME "
export JAVA_OPTS="$JAVA_OPTS"

#开启远程调试
CATALINA_OPTS="-server -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=39999"
export CATALINA_OPTS="$CATALINA_OPTS"

#查询进程占用端口号 ss -tnlp | grep pid
#keytool -genkeypair -alias "tomcat" -keyalg "RSA" -keystore tomcat.keystore

bash $CATALINA_HOME/bin/catalina.sh start
