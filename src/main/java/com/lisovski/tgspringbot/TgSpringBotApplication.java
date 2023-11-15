package com.lisovski.tgspringbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TgSpringBotApplication {

    //TODO удалить поток
    //TODO редактировать поток
    //TODO кнопки "Назад"
    //TODO админские команды (на удаление пользователя, на получение числа пользователей, на получение числа неактивных пользователей)
    //TODO логгирование всех действий (сообщение,состояние,вызов функции обработчика с сотоянием, смена состояния, ответ, дата действия, chat_id, aff_user_id)
    //TODO многопоточность
    //TODO запустить на сервере
    public static void main(String[] args){
        SpringApplication.run(TgSpringBotApplication.class, args);
    }

}
