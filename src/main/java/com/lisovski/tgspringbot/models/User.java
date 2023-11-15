package com.lisovski.tgspringbot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "token")
    private String token;

    @Column(name = "state")
    private String state;

    @Column(name = "aff_id")
    private int aff_id;
}
