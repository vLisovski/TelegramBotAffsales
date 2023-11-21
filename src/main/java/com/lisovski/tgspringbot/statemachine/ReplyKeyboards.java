package com.lisovski.tgspringbot.statemachine;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReplyKeyboards {

    private Map<States, ReplyKeyboardMarkup> keyboardMap;

    public ReplyKeyboards() {
        keyboardMap = new HashMap<>();
        keyboardMap.put(States.MAIN_MENU, createMainMenuKeyboard());
        keyboardMap.put(States.UPDATE_FLOW_MENU, createUpdateFlowMenuKeyboard());

        keyboardMap.put(States.ASK_FLOW_ID_FOR_UPDATE_FLOW, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_NAME, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_SOURCE, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_CONTENT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_MEDIUM, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_CAMPAIGN, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_TERM, askDataKeyboard());

        keyboardMap.put(States.ASK_OFFER_ID_FOR_CREATE_FLOW, askDataKeyboard());
        keyboardMap.put(States.ASK_OFFER_ID_FOR_OFFERS, askDataKeyboard());
        keyboardMap.put(States.ASK_OFFER_ID_FOR_FLOWS, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_ID_FOR_DELETE_FLOW, askDataKeyboard());

        keyboardMap.put(States.ASK_ITEM_FOR_STATISTIC, askItemForStatisticKeyboard());
        keyboardMap.put(States.STATISTIC_FILTERS_MENU, statisticFiltersMenuKeyboard());

        keyboardMap.put(States.ASK_OFFER_STAT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_STAT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_SOURCE_STAT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_CONTENT_STAT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_CAMPAIGN_STAT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_TERM_STAT, askDataKeyboard());
        keyboardMap.put(States.ASK_FLOW_UTM_MEDIUM_STAT, askDataKeyboard());
    }

    public ReplyKeyboardMarkup getKeyboardByState(States state) {
        return keyboardMap.get(state);
    }

    private ReplyKeyboardMarkup createMainMenuKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Статистика по кликам");
        keyboardFirstRow.add("Редактировать поток");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Список офферов");
        keyboardSecondRow.add("Список потоков");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Создать поток");
        keyboardThirdRow.add("Удалить поток");

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Выйти из аккаунта");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup createUpdateFlowMenuKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Имя потока");
        keyboardFirstRow.add("utm_source");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("utm_content");
        keyboardSecondRow.add("utm_campaign");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("utm_term");
        keyboardThirdRow.add("utm_medium");

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Подтвердить");
        keyboardFourthRow.add("Назад");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup askDataKeyboard() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Назад");

        keyboard.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup askItemForStatisticKeyboard(){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("ID оффера");
        keyboardFirstRow.add("ID потока");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("utm_source");
        keyboardSecondRow.add("utm_content");
        keyboardSecondRow.add("utm_campaign");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("utm_term");
        keyboardThirdRow.add("utm_medium");
        keyboardThirdRow.add("Назад");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup statisticFiltersMenuKeyboard(){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("ID оффера");
        keyboardFirstRow.add("ID потока");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("utm_source");
        keyboardSecondRow.add("utm_content");
        keyboardSecondRow.add("utm_campaign");

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("utm_term");
        keyboardThirdRow.add("utm_medium");

        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Назад");
        keyboardFourthRow.add("Получить статистику");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardFourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
