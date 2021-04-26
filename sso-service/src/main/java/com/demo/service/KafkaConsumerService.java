package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.demo.dao.TblSinglePartitionDao;
import com.demo.entity.ConsumerGroupEntity;
import com.demo.entity.ProduceInfoEntity;
import com.demo.servlet.RestfulContext;
import com.demo.util.ExecutorsUtil;
import com.demo.util.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.demo.util.KafkaClientUtil.TOPIC_ONE;

/**
 * @author bale
 */
@Slf4j
@Service
public class KafkaConsumerService {

    @Resource
    private TblSinglePartitionDao tblSinglePartitionDao;

    /**
     * 消费单分区topic, 由于要保序, 因此只能单线程消费
     */
    public void consumeOne(RestfulContext context, ConsumerGroupEntity entity) {
        final KafkaConsumer<String, String> consumer = KafkaClientUtil.createConsumer(entity.getConsumerGroup());
        consumer.subscribe(Collections.singletonList(TOPIC_ONE));

        ScheduledExecutorService scheduledExecutorService = ExecutorsUtil
                .getScheduledExecutorService(1, "singlePartitionThreadPool");
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    log.info("consumer1: offset={}, partition={}, value={}", record.offset(), record.partition(), record.value());
                }
                List<ProduceInfoEntity> list = convertProduceInfo(consumerRecords);
                tblSinglePartitionDao.insertOrUpdateBatch(list);
            } catch (Exception ex) {
                log.error("consumer poll msg ex:{}", ex.getMessage());
            }
        }, 0,1, TimeUnit.SECONDS);
    }

    public void consumeTwo(RestfulContext context, ConsumerGroupEntity entity) {

    }

    public void consumeThree(RestfulContext context, ConsumerGroupEntity entity) {

    }

    private List<ProduceInfoEntity> convertProduceInfo(ConsumerRecords<String, String> consumerRecords) {
        final List<ProduceInfoEntity> list = new ArrayList<>();
        consumerRecords.forEach(record -> list.add(JSON.parseObject(record.value(), ProduceInfoEntity.class)));
        return list;
    }
}
