package com.lisovski.tgspringbot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Offer {
    private int id;
    private String name;
    //private int cid;
    //private String cat;
    //private double epc;
    //private int cr;
}
