package com.demo.controller;

import com.demo.annotation.RestPath;
import com.demo.entity.ConsumerGroupEntity;
import com.demo.service.KafkaConsumerService;
import com.demo.servlet.RestfulContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RestPath("/ssoservice/consumer")
public class KafkaConsumerController {

    @Resource
    private KafkaConsumerService kafkaConsumerService;

    @PostMapping
    @RestPath("/one")
    public void consumeOne(RestfulContext context, @RequestBody ConsumerGroupEntity consumerGroup) {
        kafkaConsumerService.consumeOne(context, consumerGroup);
    }

    @PostMapping
    @RestPath("/two")
    public void consumeTwo(RestfulContext context, @RequestBody ConsumerGroupEntity consumerGroup) {
        kafkaConsumerService.consumeTwo(context, consumerGroup);
    }

    @PostMapping
    @RestPath("/three")
    public void consumeThree(RestfulContext context, @RequestBody ConsumerGroupEntity consumerGroup) {
        kafkaConsumerService.consumeThree(context, consumerGroup);
    }

}
