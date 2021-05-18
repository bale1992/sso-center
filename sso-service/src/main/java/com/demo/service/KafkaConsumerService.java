package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.demo.dao.TblSinglePartitionDao;
import com.demo.dao.TblThreePartitionDao;
import com.demo.dao.TblTwoPartitionDao;
import com.demo.entity.ConsumerGroupEntity;
import com.demo.entity.ProduceInfoEntity;
import com.demo.listener.ConsumerRebalanceListenerImpl;
import com.demo.servlet.RestfulContext;
import com.demo.util.ExecutorsUtil;
import com.demo.util.KafkaClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.demo.util.KafkaClientUtil.TOPIC_ONE;
import static com.demo.util.KafkaClientUtil.TOPIC_THREE;
import static com.demo.util.KafkaClientUtil.TOPIC_TWO;

/**
 * @author bale
 */
@Slf4j
@Service
public class KafkaConsumerService {

    @Resource
    private TblSinglePartitionDao tblSinglePartitionDao;

    @Resource
    private TblTwoPartitionDao tblTwoPartitionDao;

    @Resource
    private TblThreePartitionDao tblThreePartitionDao;

    private static final Map<TopicPartition, OffsetAndMetadata> CURRENT_CONSUME_OFFSET = new ConcurrentHashMap<>();

    /**
     * 消费单分区topic, 由于要保序, 因此只能单线程消费
     */
    public void consumeOne(RestfulContext context, ConsumerGroupEntity entity) {
        final KafkaConsumer<String, String> consumer = KafkaClientUtil.createConsumer(entity.getConsumerGroup());

        consumer.subscribe(Collections.singletonList(TOPIC_ONE),
                new ConsumerRebalanceListenerImpl<>(TOPIC_ONE, consumer));

        ScheduledExecutorService scheduledExecutorService = ExecutorsUtil
                .getScheduledExecutorService(1, "singlePartitionThreadPool");

        consumeMsg(consumer, 1, scheduledExecutorService, tblSinglePartitionDao::insertOrUpdateBatch);
    }

    /**
     * 消费两个分区的topic，两个线程
     */
    public void consumeTwo(RestfulContext context, ConsumerGroupEntity entity) {
        final KafkaConsumer<String, String> consumer1 = KafkaClientUtil.createConsumer(entity.getConsumerGroup());
        final KafkaConsumer<String, String> consumer2 = KafkaClientUtil.createConsumer(entity.getConsumerGroup());

        consumer1.subscribe(Collections.singletonList(TOPIC_TWO),
                new ConsumerRebalanceListenerImpl<>(TOPIC_TWO, consumer1));
        consumer2.subscribe(Collections.singletonList(TOPIC_TWO),
                new ConsumerRebalanceListenerImpl<>(TOPIC_TWO, consumer2));

        ScheduledExecutorService scheduledExecutorService = ExecutorsUtil
                .getScheduledExecutorService(2, "twoPartitionThreadPool");

        consumeMsg(consumer1, 21, scheduledExecutorService, tblTwoPartitionDao::insertOrUpdateBatch);
        consumeMsg(consumer2, 22, scheduledExecutorService, tblTwoPartitionDao::insertOrUpdateBatch);
    }

    public void consumeThree(RestfulContext context, ConsumerGroupEntity entity) {
        final KafkaConsumer<String, String> consumer1 = KafkaClientUtil.createConsumer(entity.getConsumerGroup());
        final KafkaConsumer<String, String> consumer2 = KafkaClientUtil.createConsumer(entity.getConsumerGroup());
        final KafkaConsumer<String, String> consumer3 = KafkaClientUtil.createConsumer(entity.getConsumerGroup());

        consumer1.subscribe(Collections.singletonList(TOPIC_THREE),
                new ConsumerRebalanceListenerImpl<>(TOPIC_THREE, consumer1));
        consumer2.subscribe(Collections.singletonList(TOPIC_THREE),
                new ConsumerRebalanceListenerImpl<>(TOPIC_THREE, consumer2));
        consumer3.subscribe(Collections.singletonList(TOPIC_THREE),
                new ConsumerRebalanceListenerImpl<>(TOPIC_THREE, consumer3));

        ScheduledExecutorService scheduledExecutorService = ExecutorsUtil
                .getScheduledExecutorService(3, "threePartitionThreadPool");

        consumeMsg(consumer1, 31, scheduledExecutorService, tblThreePartitionDao::insertOrUpdateBatch);
        consumeMsg(consumer2, 32, scheduledExecutorService, tblThreePartitionDao::insertOrUpdateBatch);
        consumeMsg(consumer3, 33, scheduledExecutorService, tblThreePartitionDao::insertOrUpdateBatch);
    }

    private void consumeMsg(Consumer<String, String> consumer, int index,
                            ScheduledExecutorService scheduledExecutorService,
                            Function<List<ProduceInfoEntity>, Integer> function) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : consumerRecords) {
                    log.info("consumer{}: offset={}, partition={}, value={}",
                            index, record.offset(), record.partition(), record.value());
                    CURRENT_CONSUME_OFFSET.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset()));
                }
                List<ProduceInfoEntity> list = convertProduceInfo(consumerRecords);
                if (CollectionUtils.isNotEmpty(list)) {
                    function.apply(list);
                }

                // 手动同步提交offset; 不选异步提交的原因是：异步不支持重试
                consumer.commitSync(Duration.ofSeconds(10));
            } catch (Exception ex) {
                log.error("consumer{} poll msg ex:{}", index, ex.getMessage());
            }
        }, 0,1, TimeUnit.SECONDS);
    }

    private List<ProduceInfoEntity> convertProduceInfo(ConsumerRecords<String, String> consumerRecords) {
        final List<ProduceInfoEntity> list = new ArrayList<>();
        consumerRecords.forEach(record -> list.add(JSON.parseObject(record.value(), ProduceInfoEntity.class)));
        return list;
    }

    public static Map<TopicPartition, OffsetAndMetadata> getCurrentConsumeOffset() {
        return KafkaConsumerService.CURRENT_CONSUME_OFFSET;
    }
}
