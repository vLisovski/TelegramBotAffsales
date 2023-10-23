package com.lisovski.tgspringbot.statemachine;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface Routable {
    SendMessage getMessage(long chatId,String token, String messageText);
}
