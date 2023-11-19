package com.lisovski.tgspringbot.statemachine;

import com.lisovski.tgspringbot.models.User;
import com.lisovski.tgspringbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class Router {

    private final UserService userService;

    private final StateMessages stateMessages;

    @Autowired
    public Router(UserService userService, StateMessages stateMessages) {
        this.userService = userService;
        this.stateMessages = stateMessages;
    }

    private States routeCallback(String callback, String currentState) {
        switch (callback) {
            case "Получить пост":
                return States.ASK_POST_ID;
            case "Редактировать поток":
                return States.ASK_FLOW_ID_FOR_UPDATE_FLOW;

            case "Имя потока":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_NAME;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_source":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_SOURCE;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_content":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_CONTENT;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_campaign":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_CAMPAIGN;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_term":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_TERM;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_medium":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_MEDIUM;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "Подтвердить":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.UPDATE_FLOW;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "Назад":
            {
                if(currentState.equals("UPDATE_FLOW_NAME") || currentState.equals("UPDATE_UTM_SOURCE")
                || currentState.equals("UPDATE_UTM_CONTENT") || currentState.equals("UPDATE_UTM_CAMPAIGN")
                || currentState.equals("UPDATE_UTM_TERM") || currentState.equals("UPDATE_UTM_MEDIUM")){
                    return States.UPDATE_FLOW_MENU;
                }else {
                    return States.MAIN_MENU;
                }
            }
            case "Выйти из аккаунта":
                return States.LOGOUT;
            case "Список офферов":
                return States.ASK_OFFER_ID_FOR_OFFERS;
            case "Список потоков":
                return States.ASK_OFFER_ID_FOR_FLOWS;
            case "Создать поток":
                return States.ASK_OFFER_ID_FOR_CREATE_FLOW;
            case "Удалить поток":
                return States.ASK_FLOW_ID_FOR_DELETE_FLOW;

            default:
                return States.MAIN_MENU;
        }
    }

    public SendMessage getAnswer(long chatId, String messageText) {

        if (messageText.equals("/start")) {
            if (userService.existsByChatId(chatId)) {
                User user = userService.getStateAndToken(chatId);

                return stateMessages.getMethodByState(user.getState())
                        .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
            } else {

                int inserted = userService.insert(User.builder()
                        .chatId(chatId)
                        .token("skibidi")
                        .aff_id(0)
                        .state(States.ANSWER_AUTH_DATA.toString()).build());

                return stateMessages.getMethodByState(States.ANSWER_AUTH_DATA.toString())
                        .getMessage(chatId, "skibidi", messageText);
            }
        } else {

            User user = userService.getStateAndToken(chatId);

            //Проверка на нажатие кнопки "Назад" в стейтах ввода данных (нужно для работы кнопки "Назад")
            if(messageText.equals("Назад")){
                States state = routeCallback(messageText, user.getState());
                return stateMessages.getMethodByState(state.toString())
                        .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
            }

            if (!user.getState().equals(States.MAIN_MENU.toString()) && !user.getState().equals(States.UPDATE_FLOW_MENU.toString())) {
                return stateMessages.getMethodByState(user.getState())
                        .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
            } else {

                States state = routeCallback(messageText, user.getState());

                if (state.toString().equals(States.MAIN_MENU.toString())) {
                    return stateMessages.getMethodByState(user.getState())
                            .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
                } else {
                    return stateMessages.getMethodByState(state.toString())
                            .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
                }
            }
        }
    }
}
