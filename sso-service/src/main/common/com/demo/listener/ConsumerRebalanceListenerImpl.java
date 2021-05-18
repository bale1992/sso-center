package com.demo.listener;

import com.demo.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 再均衡监听器
 */
@Slf4j
public class ConsumerRebalanceListenerImpl<K, V> implements ConsumerRebalanceListener {

    private final String topicName;

    private final Consumer<K, V> consumer;

    public ConsumerRebalanceListenerImpl(String topicName, Consumer<K, V> consumer) {
        this.topicName = topicName;
        this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        // 再均衡之前执行，一般用于手动提交offset, 避免重复消费
        log.warn("Rebalanced will be happen");
        if (CollectionUtils.isEmpty(partitions)) {
            return;
        }

        Map<TopicPartition, OffsetAndMetadata> currentConsumeOffset = KafkaConsumerService.getCurrentConsumeOffset();
        for (TopicPartition topicPartition : partitions) {
            if (!this.topicName.equals(topicPartition.topic())) {
                continue;
            }
            if (currentConsumeOffset.containsKey(topicPartition)) {
                OffsetAndMetadata offsetAndMetadata = currentConsumeOffset.get(topicPartition);
                log.info("Commit offset {} before rebalanced, topic: {}, partition: {}",
                        offsetAndMetadata.offset(), topicPartition.topic(), topicPartition.partition());
                this.consumer.commitSync(Collections.singletonMap(topicPartition, offsetAndMetadata));
            }
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        // 再均衡之后执行, 一般用于指定消费起始offset, 避免重复消费
        log.warn("After rebalanced");
        if (CollectionUtils.isEmpty(partitions)) {
            return;
        }

        Set<TopicPartition> topicPartitionSet = new HashSet<>(partitions);
        Map<TopicPartition, OffsetAndMetadata> committedOffset = this.consumer.committed(topicPartitionSet);

        for (TopicPartition topicPartition : partitions) {
            if (!this.topicName.equals(topicPartition.topic())) {
                continue;
            }
            if (committedOffset.containsKey(topicPartition)) {
                OffsetAndMetadata offsetAndMetadata = committedOffset.get(topicPartition);
                log.info("Consume begin offset {} after rebalanced, topic: {}, partition: {}",
                        offsetAndMetadata.offset() + 1, topicPartition.topic(), topicPartition.partition());
                consumer.seek(topicPartition, offsetAndMetadata.offset() + 1);
            }
        }
    }
}
