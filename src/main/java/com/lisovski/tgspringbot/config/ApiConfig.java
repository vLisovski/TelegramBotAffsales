package com.lisovski.tgspringbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class ApiConfig {

    @Value("${api.post.getpost}")
    String getPostUrl;

    @Value("${alter.createflow}")
    String createFlowUrl;

    @Value("${alter.flows}")
    String getFlowsUrl;

    @Value("${alter.offers}")
    String getOffersUrl;

    @Value("${alter.auth}")
    String authUrl;

    @Value("${alter.deleteflow}")
    String deleteFlowUrl;

    @Value("${alter.updateflow}")
    String updateFlowUrl;

    @Value("${alter.stat}")
    String statUrl;
}
