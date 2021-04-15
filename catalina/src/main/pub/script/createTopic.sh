#!/bin/bash

TOPIC_CONF=$APP_ROOT/etc/topic/topics.cfg
if [ -f $TOPIC_CONF ]; then
    echo "topic conf file not exists"
    exit 1
fi

KAFKA_BIN=$KAFKA_HOME/kafka1/bin
if [ -d $KAFKA_BIN ]; then
    echo "kafka bin not exists"
    exit 1
fi

HOST_IP=""
BOOTSTRAP_SERVER=$HOST_IP:9091,$HOST_IP:9092,$HOST_IP:9093
for line in $(cat $TOPIC_CONF)
do
    TEMP_CONF_FILE="$line.xml"
    echo "$TEMP_CONF_FILE"

    TOPIC_NAME=`grep -E -o -e '<name>.+</name>' $TEMP_CONF_FILE | sed 's/<name>//g'|sed 's/<\/name>//g'`
    PARTITION=`grep -E -o -e '<partition>.+</partition>' $TEMP_CONF_FILE | sed 's/<partition>//g'|sed 's/<\/partition>//g'`
    REPLICATION=`grep -E -o -e '<replication>.+</replication>' $TEMP_CONF_FILE | sed 's/<replication>//g'|sed 's/<\/replication>//g'`

    echo "topicName $TOPIC_NAME"
    echo "topicPartition $PARTITION"
    echo "topicReplication $REPLICATION"

    $KAFKA_BIN/kafka-topics.sh --create --bootstrap-server $BOOTSTRAP_SERVER --partitions $PARTITION --replication-factor $REPLICATION --topic $TOPIC_NAME
done