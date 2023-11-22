package com.lisovski.tgspringbot.bot;

import com.lisovski.tgspringbot.config.BotConfig;
import com.lisovski.tgspringbot.statemachine.Router;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final Router router;

    private class SessionThread extends Thread {

        private final Router router;
        private final Update update;
        private final TelegramLongPollingBot bot;

        public SessionThread(Router router, Update update, TelegramLongPollingBot bot) {
            this.router = router;
            this.update = update;
            this.bot = bot;
        }

        @Override
        public void run(){
            if (update.hasMessage() && update.getMessage().hasText()) {

                SendMessage message = router.getAnswer(update.getMessage().getChatId()
                        ,update.getMessage().getText());

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (update.hasCallbackQuery()) {

                SendMessage message = router.getAnswer(update.getMessage().getChatId()
                        ,update.getCallbackQuery().getMessage().getText());

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        SessionThread sessionThread = new SessionThread(router, update, this);
        sessionThread.start();
    }
}
