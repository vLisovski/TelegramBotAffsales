package com.lisovski.tgspringbot.statemachine;

import com.lisovski.tgspringbot.models.User;
import com.lisovski.tgspringbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class Router {

    private final UserService userService;

    private final RouterMethodsLibrary routerMethodsLibrary;

    @Autowired
    public Router(UserService userService, RouterMethodsLibrary routerMethodsLibrary) {
        this.userService = userService;
        this.routerMethodsLibrary = routerMethodsLibrary;
    }

    private States routeCallback(String callback) {
        switch (callback) {
            case "Получить пост":
                return States.ASK_POST_ID;
            case "Рандомная паста":
                return States.ANSWER_AUTH_DATA;
            case "Список офферов":
                return States.ASK_OFFER_ID_FOR_OFFERS;
            case "Список потоков":
                return States.ASK_OFFER_ID_FOR_FLOWS;
            case "Создать поток":
                return States.ASK_OFFER_ID_FOR_CREATE_FLOW;
            default:
                return States.MAIN_MENU;
        }
    }

    public SendMessage getAnswer(long chatId, String messageText) {

        if (messageText.equals("/start")) {
            if (userService.existsByChatId(chatId)) {
                User user = userService.getStateAndToken(chatId);

                return routerMethodsLibrary.getMethodByState(user.getState())
                        .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
            } else {

                int inserted = userService.insert(User.builder()
                        .chatId(chatId)
                        .token("skibidi")
                        .aff_id(0)
                        .state(States.ANSWER_AUTH_DATA.toString()).build());

                return routerMethodsLibrary.getMethodByState(States.ANSWER_AUTH_DATA.toString())
                        .getMessage(chatId, "skibidi", messageText);
            }
        } else {

            User user = userService.getStateAndToken(chatId);

            if (!user.getState().equals(States.MAIN_MENU.toString())) {
                return routerMethodsLibrary.getMethodByState(user.getState())
                        .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
            } else {

                States state = routeCallback(messageText);

                if (state.toString().equals(States.MAIN_MENU.toString())) {
                    return routerMethodsLibrary.getMethodByState(user.getState())
                            .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
                } else {
                    return routerMethodsLibrary.getMethodByState(state.toString())
                            .getMessage(chatId, user.getAff_id()+"-"+user.getToken(), messageText);
                }
            }
        }
    }

//    public SendMessage getAnswerCallback(long chatId, String callback) {
//
//        States state = routeCallback(callback);
//        int updated = userService.updateStateByChatId(state.toString(), chatId);
//        String token = userService.getToken(chatId);
//
//        return routerMethodsLibrary.getMethodByState(state.toString())
//                .getMessage(chatId, token, "");
//    }
}
