package com.lisovski.tgspringbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TgSpringBotApplication {
    //TODO логи
    //TODO запустить на сервере
    public static void main(String[] args){
        SpringApplication.run(TgSpringBotApplication.class, args);
        System.out.println("Бот запущен");
    }
}
