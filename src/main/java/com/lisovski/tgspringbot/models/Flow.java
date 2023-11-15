package com.lisovski.tgspringbot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flow {
    private int id;
    private String url;
//  private int offer;
//  private String offername;
    private String name;
//  private double epc;
//  private double cr;
//  private int total;
//  private int site;
//  private String siteurl;
//  private int space;
    private String spaceurl;
//  private String traffback;
//  private String postback;
//  private String metrika;
//  private String google;
//  private String vkcom;
//  private String facebook;
    private String utm_source;
    private String utm_campaign;
    private String utm_content;
    private String utm_term;
    private String utm_medium;
}
