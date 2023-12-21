package com.hnp.simplerestclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Configuration
public class RestClientConfig {

    @Value("${application.api.url}")
    private String url;
    @Value("${application.api.username}")
    private String username;
    @Value("${application.api.password}")
    private String password;

    @Bean
    public RestClient restClient() {
        String auth = "Basic " + Base64
                .getEncoder()
                .encodeToString((username + ":" + password).getBytes());
        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        auth
                ).build();
    }
}
