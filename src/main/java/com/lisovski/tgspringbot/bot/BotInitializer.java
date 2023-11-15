package com.lisovski.tgspringbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    private final Bot bot;

    @Autowired
    public BotInitializer(Bot bot) {
        this.bot = bot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot);
            System.out.println("Имя и токен бота верны");
        } catch (TelegramApiException e) {
            System.out.println("Ошибка. Имя и токен бота не верны или такой бот уже запущен.");
        }
    }
}
