package com.demo.service;

import com.demo.entity.ConsumerGroupEntity;
import com.demo.servlet.RestfulContext;
import com.demo.util.ExecutorsUtil;
import com.demo.util.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

import static com.demo.util.KafkaClientUtil.TOPIC_ONE;

@Slf4j
@Service
public class KafkaConsumerService {

    private final ExecutorService fixedThreadPool = ExecutorsUtil
            .getFixedExecutorService(3, "consumerThreeThreadThreadPool");

    // 消费单分区topic, 由于要保序, 因此只能单线程消费
    public void consumeOne(RestfulContext context, ConsumerGroupEntity entity) {
        final KafkaConsumer<String, String> consumer = KafkaClientUtil.createConsumer(entity.getConsumerGroup());
        consumer.subscribe(Collections.singletonList(TOPIC_ONE));

        fixedThreadPool.execute(() -> {
            while (true) {
                try {
                    ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                    for (ConsumerRecord<String, String> record : consumerRecords) {
                        log.info("consumer1: offset={}, partition={}, value={}", record.offset(), record.partition(), record.value());
                    }
                } catch (Exception ex) {
                    log.error("consumer poll msg ex:{}", ex.getMessage());
                    break;
                }
            }
        });
    }

    public void consumeTwo(RestfulContext context, ConsumerGroupEntity entity) {

    }

    public void consumeThree(RestfulContext context, ConsumerGroupEntity entity) {

    }
}
