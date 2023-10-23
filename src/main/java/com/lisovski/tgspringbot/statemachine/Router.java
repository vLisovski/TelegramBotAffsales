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
            default:
                return States.MAIN_MENU;
        }
    }

    public SendMessage getAnswer(long chatId, String messageText) {

        if (messageText.equals("/start")) {
            if (userService.existsByChatId(chatId)) {
                User user = userService.getStateAndToken(chatId);

                return routerMethodsLibrary.getMethodByState(user.getState())
                        .getMessage(chatId, user.getToken(), messageText);
            } else {
                int inserted = userService.insert(User.builder()
                        .chatId(chatId)
                        .state(States.ANSWER_AUTH_DATA.toString()).build());

                return routerMethodsLibrary.getMethodByState(States.ANSWER_AUTH_DATA.toString())
                        .getMessage(chatId, "", messageText);
            }
        } else {

            User user = userService.getStateAndToken(chatId);

            if (!user.getState().equals(States.MAIN_MENU.toString())) {
                return routerMethodsLibrary.getMethodByState(user.getState())
                        .getMessage(chatId, user.getToken(), messageText);
            } else {

                States state = routeCallback(messageText);

                if (state.toString().equals(States.MAIN_MENU.toString())) {
                    return routerMethodsLibrary.getMethodByState(user.getState())
                            .getMessage(chatId, user.getToken(), messageText);
                } else {
                    return routerMethodsLibrary.getMethodByState(state.toString())
                            .getMessage(chatId, user.getToken(), messageText);
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
