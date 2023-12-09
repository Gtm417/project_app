package com.hodik.elastic.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("users/test")
@Log4j2
public class LoadBalancerTest {
    private static int random = new Random().nextInt(100000);

    @Autowired
    DiscoveryClient client;

    @Value("${eureka.instance.metadataMap.instanceId}")
    String instanceId;
    @Value("${eureka.instance.instanceId}")
    String id;

    @GetMapping
    public String hello() {
//        List<ServiceInstance> instances = client.getInstances("elastic_app");
//        ServiceInstance selectedInstance = instances
//                .get(new Random().nextInt(instances.size()));
        log.info("Load balancer test " + random);
//        return "Hello World: " + instanceId + " : " + random;
        return id;
    }
}
