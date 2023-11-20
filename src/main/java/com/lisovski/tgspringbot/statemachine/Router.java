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

    private States parseState(String state){
        switch (state){
            case "ANSWER_AUTH_DATA": return States.ANSWER_AUTH_DATA;
            case "AUTH": return States.AUTH;
            case "LOGOUT": return States.LOGOUT;
            case "MAIN_MENU": return States.MAIN_MENU;
            case "ASK_POST_ID": return States.ASK_POST_ID;
            case "GET_POST": return States.GET_POST;
            case "ASK_OFFER_ID_FOR_FLOWS": return States.ASK_OFFER_ID_FOR_FLOWS;
            case "GET_FLOWS": return States.GET_FLOWS;
            case "ASK_OFFER_ID_FOR_OFFERS": return States.ASK_OFFER_ID_FOR_OFFERS;
            case "GET_OFFERS": return States.GET_OFFERS;
            case "ASK_OFFER_ID_FOR_CREATE_FLOW": return States.ASK_OFFER_ID_FOR_CREATE_FLOW;
            case "CREATE_FLOW": return States.CREATE_FLOW;
            case "ASK_FLOW_ID_FOR_DELETE_FLOW": return States.ASK_FLOW_ID_FOR_DELETE_FLOW;
            case "DELETE_FLOW": return States.DELETE_FLOW;
            case "ASK_FLOW_ID_FOR_UPDATE_FLOW": return States.ASK_FLOW_ID_FOR_UPDATE_FLOW;
            case "UPDATE_FLOW_MENU": return States.UPDATE_FLOW_MENU;
            case "ASK_FLOW_NAME": return States.ASK_FLOW_NAME;
            case "UPDATE_FLOW_NAME": return States.UPDATE_FLOW_NAME;
            case "ASK_FLOW_UTM_SOURCE": return States.ASK_FLOW_UTM_SOURCE;
            case "UPDATE_UTM_SOURCE": return States.UPDATE_UTM_SOURCE;
            case "ASK_FLOW_UTM_CONTENT": return States.ASK_FLOW_UTM_CONTENT;
            case "UPDATE_UTM_CONTENT": return States.UPDATE_UTM_CONTENT;
            case "ASK_FLOW_UTM_CAMPAIGN": return States.ASK_FLOW_UTM_CAMPAIGN;
            case "UPDATE_UTM_CAMPAIGN": return States.UPDATE_UTM_CAMPAIGN;
            case "ASK_FLOW_UTM_TERM": return States.ASK_FLOW_UTM_TERM;
            case "UPDATE_UTM_TERM": return States.UPDATE_UTM_TERM;
            case "ASK_FLOW_UTM_MEDIUM": return States.ASK_FLOW_UTM_MEDIUM;
            case "UPDATE_UTM_MEDIUM": return States.UPDATE_UTM_MEDIUM;
            case "UPDATE_FLOW": return States.UPDATE_FLOW;
            case "ASK_ITEM_FOR_STATISTIC": return States.ASK_ITEM_FOR_STATISTIC;
            case "UPDATE_ITEM_FOR_STATISTIC": return States.UPDATE_ITEM_FOR_STATISTIC;
            case "UPDATE_FROM": return States.UPDATE_FROM;
            case "UPDATE_TO": return States.UPDATE_TO;
            case "STATISTIC_FILTERS_MENU": return States.STATISTIC_FILTERS_MENU;
            case "ASK_OFFER_STAT": return States.ASK_OFFER_STAT;
            case "UPDATE_OFFER_STAT": return States.UPDATE_OFFER_STAT;
            case "ASK_FLOW_STAT": return States.ASK_FLOW_STAT;
            case "UPDATE_FLOW_STAT": return States.UPDATE_FLOW_STAT;
            case "ASK_FLOW_UTM_SOURCE_STAT": return States.ASK_FLOW_UTM_SOURCE_STAT;
            case "UPDATE_UTM_SOURCE_STAT": return States.UPDATE_UTM_SOURCE_STAT;
            case "ASK_FLOW_UTM_CONTENT_STAT": return States.ASK_FLOW_UTM_CONTENT_STAT;
            case "UPDATE_UTM_CONTENT_STAT": return States.UPDATE_UTM_CONTENT_STAT;
            case "ASK_FLOW_UTM_CAMPAIGN_STAT": return States.ASK_FLOW_UTM_CAMPAIGN_STAT;
            case "UPDATE_UTM_CAMPAIGN_STAT": return States.UPDATE_UTM_CAMPAIGN_STAT;
            case "ASK_FLOW_UTM_TERM_STAT": return States.ASK_FLOW_UTM_TERM_STAT;
            case "UPDATE_UTM_TERM_STAT": return States.UPDATE_UTM_TERM_STAT;
            case "ASK_FLOW_UTM_MEDIUM_STAT": return States.ASK_FLOW_UTM_MEDIUM_STAT;
            case "UPDATE_UTM_MEDIUM_STAT": return States.UPDATE_UTM_MEDIUM_STAT;
            case "GET_STATISTIC": return States.GET_STATISTIC;
            default: return States.MAIN_MENU;
        }
    }

    private States routeCallback(String callback, String currentState) {
        switch (callback) {
            case "Получить пост":
                return States.ASK_POST_ID;
            case "Статистика по кликам":
                return States.ASK_ITEM_FOR_STATISTIC;
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
            case "ID оффера":
            {
                if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_OFFER_STAT;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "ID потока":
            {
                if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_FLOW_STAT;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_source":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_SOURCE;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_FLOW_UTM_SOURCE_STAT;
                }else if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_content":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_CONTENT;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_FLOW_UTM_CONTENT_STAT;
                }else if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_campaign":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_CAMPAIGN;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_FLOW_UTM_CAMPAIGN_STAT;
                }else if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_term":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_TERM;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_FLOW_UTM_TERM_STAT;
                }else if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "utm_medium":
            {
                if(currentState.equals("UPDATE_FLOW_MENU")){
                    return States.ASK_FLOW_UTM_MEDIUM;
                }else if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.ASK_FLOW_UTM_MEDIUM_STAT;
                }else if(currentState.equals("UPDATE_ITEM_FOR_STATISTIC")){
                    return States.UPDATE_ITEM_FOR_STATISTIC;
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
                }else if(currentState.equals("UPDATE_FLOW_STAT") || currentState.equals("UPDATE_OFFER_STAT") || currentState.equals("UPDATE_UTM_SOURCE_STAT")
                        || currentState.equals("UPDATE_UTM_CONTENT_STAT") || currentState.equals("UPDATE_UTM_CAMPAIGN_STAT")
                        || currentState.equals("UPDATE_UTM_TERM_STAT") || currentState.equals("UPDATE_UTM_MEDIUM_STAT")){
                    return States.STATISTIC_FILTERS_MENU;
                }else{
                    return States.MAIN_MENU;
                }
            }
            case "В главное меню":
                if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.MAIN_MENU;
                }else{
                    return parseState(currentState);
                }
            case "Получить статистику":
            {
                if(currentState.equals("STATISTIC_FILTERS_MENU")){
                    return States.GET_STATISTIC;
                }else{
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

            if(user.getState().equals("UPDATE_ITEM_FOR_STATISTIC")){
                if(messageText.equals("utm_source") || messageText.equals("utm_content")
                || messageText.equals("utm_campaign") || messageText.equals("utm_term")
                || messageText.equals("utm_medium") || messageText.equals("ID оффера")
                || messageText.equals("ID потока")){
                    return stateMessages.getMethodByState(user.getState())
                            .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
                }else{
                    return stateMessages.getMethodByState(States.ASK_ITEM_FOR_STATISTIC.toString())
                            .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
                }
            }

            if (!user.getState().equals(States.MAIN_MENU.toString()) && !user.getState().equals(States.UPDATE_FLOW_MENU.toString())
                    && !user.getState().equals(States.STATISTIC_FILTERS_MENU.toString())) {
                System.out.println("IF BLOCK");
                return stateMessages.getMethodByState(user.getState())
                        .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
            } else {
                System.out.println("ELSE BLOCK");
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
