package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.demo.entity.ProduceInfoEntity;
import com.demo.util.ExecutorsUtil;
import com.demo.util.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
     *
     * 家里环境测试时间间隔为: 22:06:25:993 ~ 22:08:26:137
     * offset范围是: 1141815 ~ 2141814
     */
    public void one() {
        CompletableFuture.runAsync(() -> produceMsg(TOPIC_ONE, (k) -> null), singleThreadPool);
    }

    /**
     * 向两个分区topic中生产100W条数据
     *
     * 家里环境测试时间间隔为: 22:14:01:888 ~ 22:16:13:877
     * offset范围是: 0 ~ ...
     */
    public void two() {
        CompletableFuture.runAsync(() -> produceMsg(TOPIC_TWO, this::buildKey), singleThreadPool);
    }

    /**
     * 向三个分区topic中生产100W条数据
     *
     * 家里环境测试时间间隔为: 22:17:45:316 ~ 22:19:29:910
     * offset范围是: 0 ~ ...
     */
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
                ProducerRecord<String, String> producerRecord = buildProducerRecord(i, topicName, function);
                kafkaProducer.send(producerRecord, ((recordMetadata, e) -> {
                    if (e != null) {
                        log.error("produce msg ex: {}", e.getMessage());
                        return;
                    }

                    log.info("produce msg===>topic:{}, partition:{}, offset:{}, timeStamp:{}", recordMetadata.topic(),
                            recordMetadata.partition(), recordMetadata.offset(), recordMetadata.timestamp());
                }));
            }
            kafkaProducer.commitTransaction(); // 提交事务
        } catch (Exception ex) {
            kafkaProducer.abortTransaction(); // 终止事务
            log.error("produce msg ex: {}", ex.getMessage());
        }
    }

    private String buildKey(Integer i) {
        return String.valueOf(i % 100);
    }

    private ProducerRecord<String, String> buildProducerRecord(int index, String topicName, Function<Integer, String> func) {
        ProduceInfoEntity entity = ProduceInfoEntity.builder()
                .id(index % 100)
                .name("bale" + index)
                .age(index)
                .createTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .build();
        return new ProducerRecord<>(topicName, func.apply(index), JSON.toJSONString(entity));
    }
}
