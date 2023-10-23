package com.lisovski.tgspringbot.statemachine;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Keyboards {

    private Map<States, ReplyKeyboardMarkup> keyboardMap;

    public Keyboards(){
        keyboardMap = new HashMap<>();
        keyboardMap.put(States.MAIN_MENU,createMainMenuKeyboard());
    }

    public ReplyKeyboardMarkup getKeyboardByState(States state){
        return keyboardMap.get(state);
    }

    private ReplyKeyboardMarkup createMainMenuKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add("Получить пост");
//        keyboardFirstRow.add(getForecastCommand(language));
//        KeyboardRow keyboardSecondRow = new KeyboardRow();
//        keyboardSecondRow.add(getSettingsCommand(language));
//        keyboardSecondRow.add(getRateCommand(language));
        keyboard.add(keyboardFirstRow);
//        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
