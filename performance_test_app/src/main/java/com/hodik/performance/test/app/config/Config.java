package com.hodik.performance.test.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Getter
@Setter
@Configuration
public class Config {

    public static int REQUEST_AMOUNT = 1;

    @Value("${request.per.second}")
    int requestPerSecond;
    @Value("${request.amount}")
    int requestAmount;

    @Value("${users.url}")
    String usersUrl;
    @Value("${projects.url}")
    String projectsUrl;
    @Value("${vacancies.url}")
    String vacanciesUrl;


    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYWluX2FwcCIsInJvbGUiOiJST0xFX0FQUCIsImlhdCI6MTcwMjM3NTI2NSwiZXhwIjoyMDE3OTk0NDY1fQ.0kZ538LL82eZ37iLjjaszieWoTvnXEnf7pxitjlwGOk");
        return execution.execute(request, body);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(this::intercept));
        return restTemplate;
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(1000000);
    }
}
