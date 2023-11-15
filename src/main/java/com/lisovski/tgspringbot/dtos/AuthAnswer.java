package com.lisovski.tgspringbot.dtos;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class AuthAnswer {
    private String status;
    private int id;
    private String api;
    private String name;
    private String email;
    private boolean ban;
}
