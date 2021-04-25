package com.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author bale
 */
@Slf4j
public final class KafkaClientUtil {
    private static final String DEFAULT_SERVER = "127.0.0.1:9091,127.0.0.1:9092,127.0.0.1:9093";
    public static final String TOPIC_ONE = "topic_single_partition_three_replication";
    public static final String TOPIC_TWO = "topic_two_partition_three_replication";
    public static final String TOPIC_THREE = "topic_three_partition_two_replication";

    private KafkaClientUtil() {
    }

    public static KafkaProducer<String, String> createProducer() {
        try (InputStream inputStream = KafkaClientUtil.class.getClassLoader().getResourceAsStream("/kafka/producer.properties")) {
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

    public static KafkaConsumer<String, String> createConsumer(String groupName) {
        try (InputStream inputStream = KafkaClientUtil.class.getClassLoader().getResourceAsStream("/kafka/consumer.properties")) {
            final Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootStrapServer());
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.load(inputStream);
            return new KafkaConsumer<>(properties);
        } catch (IOException e) {
            log.error("load kafka consumer config failed.");
            return new KafkaConsumer<>(new Properties());
        }
    }

    private static String getBootStrapServer() {
        return System.getProperty("BOOTSTRAP_SERVER", DEFAULT_SERVER);
    }
}
