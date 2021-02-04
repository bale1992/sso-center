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

JAVA_OPTS="-Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -DNFW=$APP_PROCESS_NAME -Dprocname=$APP_PROCESS_NAME "
export JAVA_OPTS="$JAVA_OPTS"

#开启远程调试
CATALINA_OPTS="-server -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=39999"
export CATALINA_OPTS="$CATALINA_OPTS"

bash $CATALINA_HOME/bin/catalina.sh start
