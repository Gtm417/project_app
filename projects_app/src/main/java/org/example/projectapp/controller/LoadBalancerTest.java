package org.example.projectapp.controller;

import org.example.projectapp.restclient.ElasticUsersServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class LoadBalancerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerTest.class);
    @Autowired
    private ElasticUsersServiceClient client;

    @GetMapping("/{num}")
    public void test(@PathVariable("num") Long num) {
        for (int i = 0; i < num; i++) {
            String response = client.test();
            logger.info("[LOAD-BALANCER] " + response);
        }
    }
}
