#!/bin/bash

if [ -z $ZK_HOME ]; then
    echo "there is no ZK_HOME"
    exit 1
fi

ZK_BIN=$ZK_HOME/bin
ZK_CONF=$ZK_HOME/conf

if [ $1 == "start" ]; then
  $ZK_BIN/zkServer.sh start $ZK_CONF/zoo1.cfg
  $ZK_BIN/zkServer.sh start $ZK_CONF/zoo2.cfg
  $ZK_BIN/zkServer.sh start $ZK_CONF/zoo3.cfg
elif [ $1 == stop ]; then
  $ZK_BIN/zkServer.sh stop $ZK_CONF/zoo1.cfg
  $ZK_BIN/zkServer.sh stop $ZK_CONF/zoo2.cfg
  $ZK_BIN/zkServer.sh stop $ZK_CONF/zoo3.cfg
else
  $ZK_BIN/zkServer.sh status $ZK_CONF/zoo1.cfg
  $ZK_BIN/zkServer.sh status $ZK_CONF/zoo2.cfg
  $ZK_BIN/zkServer.sh status $ZK_CONF/zoo3.cfg
fi

exit 0