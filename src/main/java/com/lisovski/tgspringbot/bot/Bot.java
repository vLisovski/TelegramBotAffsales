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
        if (update.hasMessage() && update.getMessage().hasText()) {

//          SendMessage message = new SendMessage();
//          message.setChatId(update.getMessage().getChatId());

            SendMessage message = router.getAnswer(update.getMessage().getChatId(),update.getMessage().getText());

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else if (update.hasCallbackQuery()) {

            SendMessage message = router.getAnswer(update.getMessage().getChatId()
                    ,update.getCallbackQuery().getMessage().getText());

//            SendMessage message = new SendMessage();
//            message.setChatId(update.getMessage().getChatId());
//            message.setText("Callback!");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
