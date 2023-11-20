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

    public DeleteFlowAnswer deleteFlow(String token, int flowId){

        String requestURL = apiConfig.getDeleteFlowUrl()+"?id="+token+"&flow="+flowId;

        return restTemplate.getForObject(requestURL, DeleteFlowAnswer.class);
    }

    public UpdateFlowAnswer updateFlow(String token, UpdateFlowRequest updateFlowRequest){

        String requestURL = apiConfig.getUpdateFlowUrl()
                +"?id="+token
                +"&flow="+updateFlowRequest.getFlow();

        if(!updateFlowRequest.getName().equals(" ")){
            requestURL = requestURL + "&name=" + updateFlowRequest.getName();
        }
        if(!updateFlowRequest.getUtms().equals(" ")){
            requestURL = requestURL + "&utms=" + updateFlowRequest.getUtms();
        }
        if(!updateFlowRequest.getUtmc().equals(" ")){
            requestURL = requestURL + "&utmc=" + updateFlowRequest.getUtmc();
        }
        if(!updateFlowRequest.getUtmm().equals(" ")){
            requestURL = requestURL + "&utmm=" + updateFlowRequest.getUtmm();
        }
        if(!updateFlowRequest.getUtmn().equals(" ")){
            requestURL = requestURL + "&utmn=" + updateFlowRequest.getUtmn();
        }
        if(!updateFlowRequest.getUtmt().equals(" ")){
            requestURL = requestURL + "&utmt=" + updateFlowRequest.getUtmt();
        }

        return restTemplate.getForObject(requestURL, UpdateFlowAnswer.class);
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

        return restTemplate.postForObject(requestURL, authRequest, AuthAnswer.class);

    }

    public String getStatistic(String token, StatisticRequest statisticRequest){

        String requestURL = apiConfig.getStatUrl() +"?id="+token +"&item="+statisticRequest.getItem();
        if(!statisticRequest.getFrom().equals(" ")){
            requestURL = requestURL+"&from="+statisticRequest.getFrom();
        }
        if(!statisticRequest.getTo().equals(" ")){
            requestURL = requestURL+"&to="+statisticRequest.getTo();
        }
        if(statisticRequest.getOffer()!=0){
            requestURL = requestURL+"&offer="+statisticRequest.getOffer();
        }
        if(statisticRequest.getFlow()!=0){
            requestURL = requestURL+"&flow="+statisticRequest.getFlow();
        }
        if(statisticRequest.getUtms().equals(" ")){
            requestURL = requestURL+"&utms="+statisticRequest.getUtms();
        }
        if(statisticRequest.getUtmc().equals(" ")){
            requestURL = requestURL+"&utmc="+statisticRequest.getUtmc();
        }
        if(statisticRequest.getUtmn().equals(" ")){
            requestURL = requestURL+"&utmn="+statisticRequest.getUtmn();
        }
        if(statisticRequest.getUtmt().equals(" ")){
            requestURL = requestURL+"&utmt="+statisticRequest.getUtmt();
        }
        if(statisticRequest.getUtmm().equals(" ")){
            requestURL = requestURL+"&utmm="+statisticRequest.getUtmm();
        }

        return restTemplate.getForObject(requestURL, String.class);
    }
}
