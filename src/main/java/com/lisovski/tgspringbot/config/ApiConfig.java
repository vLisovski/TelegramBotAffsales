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
}
