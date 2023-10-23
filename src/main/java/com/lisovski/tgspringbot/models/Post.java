package com.lisovski.tgspringbot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private int userId;
    private int id;
    private String title;
    private String body;
}
