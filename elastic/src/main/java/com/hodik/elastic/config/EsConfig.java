package com.hodik.elastic.config;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Log4j2
@Configuration
@EnableElasticsearchRepositories
public class EsConfig extends ElasticsearchConfiguration {
    @Value("${elasticsearch.host}")
    private String elasticHost;

    @Value("${elasticsearch.port}")
    private Integer elasticPort;

    @Override
    public ClientConfiguration clientConfiguration() {
        log.info("[ELASTIC][CONFIG] Try to establish connection with elastic host={} port={}", elasticHost, elasticPort);
        String connection = elasticHost + ":" + elasticPort;
        return ClientConfiguration.builder()
                .connectedTo(connection)
                .withBasicAuth("elastic", "elastic")
                .build();
    }
}

