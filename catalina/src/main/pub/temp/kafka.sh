#!/bin/bash

if [ -z $KAFKA_HOME ]; then
    echo "there is no KAFKA_HOME"
    exit 1
fi

$KAFKA_HOME/kafka1/bin/kafka-server-start.sh -daemon $KAFKA_HOME/kafka1/config/server.properties
$KAFKA_HOME/kafka2/bin/kafka-server-start.sh -daemon $KAFKA_HOME/kafka2/config/server.properties
$KAFKA_HOME/kafka3/bin/kafka-server-start.sh -daemon $KAFKA_HOME/kafka3/config/server.properties