package com.demo.controller;

import com.demo.annotation.RestPath;
import com.demo.service.KafkaProducerService;
import com.demo.servlet.RestfulContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author bale
 */
@RestController
@RestPath("/ssoservice/producer")
public class KafkaProducerController {

    @Resource
    private KafkaProducerService kafkaProducerService;

    @PostMapping
    @RestPath("/one")
    public void one(RestfulContext context) {
        kafkaProducerService.one();
    }

    @PostMapping
    @RestPath("/two")
    public void two(RestfulContext context) {
        kafkaProducerService.two();
    }

    @PostMapping
    @RestPath("/three")
    public void three(RestfulContext context) {
        kafkaProducerService.three();
    }

}
