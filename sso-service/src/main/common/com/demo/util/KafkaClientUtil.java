package com.demo.util;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @author bale
 */
public final class KafkaClientUtil {

    private static final String BOOTSTRAP_SERVER = "192.168.1.6:9091,192.168.1.6:9092,192.168.1.6:9093";
    
    public static final String TOPIC_ONE = "topic_single_partition_three_replication";
    public static final String TOPIC_TWO = "topic_two_partition_three_replication";
    public static final String TOPIC_THREE = "topic_three_partition_two_replication";

    private KafkaClientUtil() {
    }

    public static KafkaProducer<String, String> createProducer() {
        final Properties properties = new Properties();
        properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        return new KafkaProducer<>(properties);
    }
}
