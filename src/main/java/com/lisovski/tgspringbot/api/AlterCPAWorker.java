package com.lisovski.tgspringbot.api;

import com.lisovski.tgspringbot.config.ApiConfig;
import com.lisovski.tgspringbot.dtos.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@AllArgsConstructor
public class AlterCPAWorker {

    private final RestTemplate restTemplate;

    private final ApiConfig apiConfig;

    public CreateFlowAnswer createFlow(String token, int offerId){

        String requestURL = apiConfig.getCreateFlowUrl()+"?id="+token+"&offer="+offerId;

        return restTemplate.getForObject(requestURL, CreateFlowAnswer.class);
    }

    public String getFlows(String token, int offerId){

        String requestURL = apiConfig.getGetFlowsUrl()+"?id="+token+"&offer="+offerId;

        return restTemplate.getForObject(requestURL, String.class);

    }

    public String getOffers(String token, int offerId){

        String requestURL;

        if(offerId != 0){
             requestURL = apiConfig.getGetOffersUrl()+"?id="+token+"&offer="+offerId;
        }else{
             requestURL = apiConfig.getGetOffersUrl()+"?id="+token;
        }

        return restTemplate.getForObject(requestURL, String.class);
    }

    public AuthAnswer auth(AuthRequest authRequest){

        String requestURL = apiConfig.getAuthUrl();
        System.out.println(requestURL);

        return restTemplate.postForObject(requestURL, authRequest, AuthAnswer.class);

    }


}
