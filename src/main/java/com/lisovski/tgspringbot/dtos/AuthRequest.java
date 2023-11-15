package com.lisovski.tgspringbot.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRequest {
    private String in_user;
    private String in_pass;
}
