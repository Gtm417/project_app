package com.hodik.elastic.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class EsConfig {

    @Bean
    public RestClient getRestClient(CredentialsProvider credentialsProvider) {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    return httpClientBuilder
                            .setDefaultCredentialsProvider(credentialsProvider);
                })
                .build();
        return restClient;
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport(CredentialsProvider credentialsProvider) {
        return new RestClientTransport(
                getRestClient(credentialsProvider), new JacksonJsonpMapper());
    }


    @Bean
    public ElasticsearchClient getElasticsearchClient(CredentialsProvider credentialsProvider){
        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport(credentialsProvider));
        return client;
    }

    @Bean
    public CredentialsProvider credentialsProvider() {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "elastic"));
        return credentialsProvider;
    }

}

