package com.lisovski.tgspringbot.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticRequest {
    private String item;
    private String from;
    private String to;
    private int offer;
    private int flow;
    private String utms;
    private String utmc;
    private String utmn;
    private String utmt;
    private String utmm;
}
