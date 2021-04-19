package com.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author bale
 */
@Slf4j
public final class KafkaClientUtil {
    public static final String TOPIC_ONE = "topic_single_partition_three_replication";
    public static final String TOPIC_TWO = "topic_two_partition_three_replication";
    public static final String TOPIC_THREE = "topic_three_partition_two_replication";

    private KafkaClientUtil() {
    }

    public static KafkaProducer<String, String> createProducer() {
        try (InputStream inputStream = InputStream.class.getResourceAsStream("/producer.properties")) {
            final Properties properties = new Properties();
            properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootStrapServer());
            properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.load(inputStream);
            return new KafkaProducer<>(properties);
        } catch (IOException e) {
            log.error("load kafka producer config failed.");
            return new KafkaProducer<>(new Properties());
        }
    }

    private static String getBootStrapServer() {
        return System.getenv("BOOTSTRAP_SERVER");
    }
}
