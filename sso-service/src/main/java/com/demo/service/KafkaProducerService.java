package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.demo.entity.UserEntity;
import com.demo.util.ExecutorsUtil;
import com.demo.util.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.demo.util.KafkaClientUtil.TOPIC_ONE;
import static com.demo.util.KafkaClientUtil.TOPIC_THREE;
import static com.demo.util.KafkaClientUtil.TOPIC_TWO;

/**
 * @author bale
 */
@Slf4j
@Service
public class KafkaProducerService {

    private final ExecutorService singleThreadPool = ExecutorsUtil
            .getSingleExecutorService("commonSingleThreadPool");

    /**
     * 向单分区topic中生产100W条数据
     */
    public void one() {
        CompletableFuture.runAsync(() -> produceMsg(TOPIC_ONE, (k) -> null), singleThreadPool);
    }

    public void two() {
        CompletableFuture.runAsync(() -> produceMsg(TOPIC_TWO, this::buildKey), singleThreadPool);
    }

    public void three() {
        CompletableFuture.runAsync(() -> produceMsg(TOPIC_THREE, this::buildKey), singleThreadPool);
    }

    private void produceMsg(String topicName, Function<Integer, String> function) {
        final int recordNum = 1000000;
        final KafkaProducer<String, String> kafkaProducer = KafkaClientUtil.createProducer();
        kafkaProducer.initTransactions(); // 初始化事务
        kafkaProducer.beginTransaction(); // 开始事务
        try {
            for (int i = 1; i <= recordNum; i++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, function.apply(i),
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
            kafkaProducer.commitTransaction(); // 提交事务
        } catch (Exception ex) {
            kafkaProducer.abortTransaction(); // 终止事务
            log.error("produce msg ex:{}", ex.getMessage());
        }
    }

    private String buildKey(Integer i) {
        return String.valueOf(i % 100);
    }
}
