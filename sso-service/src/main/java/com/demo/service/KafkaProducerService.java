package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.demo.annotation.CostStatistics;
import com.demo.entity.UserEntity;
import com.demo.util.ExecutorsUtil;
import com.demo.util.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.demo.util.KafkaClientUtil.TOPIC_ONE;
import static com.demo.util.KafkaClientUtil.TOPIC_THREE;
import static com.demo.util.KafkaClientUtil.TOPIC_TWO;

/**
 * @author bale
 */
@Slf4j
@Service
public class KafkaProducerService {

    private final int RECORD_NUM = 1000000;

    private final ExecutorService singleThreadPool = ExecutorsUtil
            .getSingleExecutorService("commonSingleThreadPool");

    private final KafkaProducer<String, String> kafkaProducer = KafkaClientUtil.createProducer();

    /**
     * 向单分区topic中生产100W条数据
     */
    public void one() {
        CompletableFuture.runAsync(() -> {
            for (int i = 1; i <= RECORD_NUM; i++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_ONE,
                        JSON.toJSONString(UserEntity.builder().id((long) (i % 100)).userName("bale" + i).build()));
                kafkaProducer.send(producerRecord, ((recordMetadata, e) -> {
                    if (e != null) {
                        log.error("produce msg ex:{}", e.getMessage());
                        return;
                    }

                    log.info("produce msg===>topic:{}, partition:{}, offset:{}, timeStamp:{}", recordMetadata.topic(),
                            recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
                }));
            }
        }, singleThreadPool);
    }

    public void two() {
        CompletableFuture.runAsync(() -> produceMsgByKey(TOPIC_TWO), singleThreadPool);
    }

    public void three() {
        CompletableFuture.runAsync(() -> produceMsgByKey(TOPIC_THREE), singleThreadPool);
    }

    @CostStatistics
    private void produceMsgByKey(String topicName) {
        for (int i = 1; i <= RECORD_NUM; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, buildKey(i),
                    JSON.toJSONString(UserEntity.builder().id((long) (i % 100)).userName("bale" + i).build()));
            kafkaProducer.send(producerRecord, ((recordMetadata, e) -> {
                if (e != null) {
                    log.error("produce msg ex:{}", e.getMessage());
                    return;
                }

                log.info("produce msg===>topic:{}, partition:{}, offset:{}, timeStamp:{}", recordMetadata.topic(),
                        recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
            }));
        }
    }

    private String buildKey(int i) {
        return String.valueOf(i % 100);
    }
}
