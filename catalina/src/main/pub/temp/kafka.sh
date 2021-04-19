#!/bin/bash

if [ -z $KAFKA_HOME ]; then
    echo "there is no KAFKA_HOME"
    exit 1
fi

# 替换IP
HOST_IP_ADDR=$(hostname --all-ip-addresses | awk '{print $2}')
sed -i "s/\${HOST_IP}/${HOST_IP_ADDR}/g" $KAFKA_HOME/kafka1/config/server.properties
sed -i "s/\${HOST_IP}/${HOST_IP_ADDR}/g" $KAFKA_HOME/kafka2/config/server.properties
sed -i "s/\${HOST_IP}/${HOST_IP_ADDR}/g" $KAFKA_HOME/kafka3/config/server.properties

$KAFKA_HOME/kafka1/bin/kafka-server-start.sh -daemon $KAFKA_HOME/kafka1/config/server.properties
$KAFKA_HOME/kafka2/bin/kafka-server-start.sh -daemon $KAFKA_HOME/kafka2/config/server.properties
$KAFKA_HOME/kafka3/bin/kafka-server-start.sh -daemon $KAFKA_HOME/kafka3/config/server.properties