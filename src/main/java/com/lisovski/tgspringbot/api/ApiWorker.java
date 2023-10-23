package com.lisovski.tgspringbot.api;

import com.lisovski.tgspringbot.config.ApiConfig;
import com.lisovski.tgspringbot.models.Post;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class ApiWorker {

    private final RestTemplate restTemplate;

    private final ApiConfig apiConfig;

    public Post getPost(int id) {

        String params = String.format("/%s", id);
        String url = apiConfig.getGetPostUrl() + params;

        return restTemplate.getForObject(url, Post.class);
    }

}
