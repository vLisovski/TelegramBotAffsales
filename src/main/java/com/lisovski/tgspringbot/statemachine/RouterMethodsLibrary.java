package com.lisovski.tgspringbot.statemachine;

import com.lisovski.tgspringbot.api.ApiWorker;
import com.lisovski.tgspringbot.models.Post;
import com.lisovski.tgspringbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

@Component
public class RouterMethodsLibrary {

    private final Map<String, Routable> messageMap;
    private final ApiWorker apiWorker;
    private final UserService userService;

    @Autowired
    public RouterMethodsLibrary(UserService userService, ApiWorker apiWorker ) {
        this.userService = userService;
        this.apiWorker = apiWorker;
        messageMap = new HashMap<>();
        messageMap.put(States.MAIN_MENU.toString(), this::mainMenuMessage);
        messageMap.put(States.ANSWER_AUTH_DATA.toString(), this::answerAuthDataMessage);
        messageMap.put(States.AUTH.toString(), this::authMessage);
        messageMap.put(States.ASK_POST_ID.toString(), this::askPostIdMessage);
        messageMap.put(States.GET_POST.toString(),this::getPostMessage);
    }

    public Routable getMethodByState(String state){
        return messageMap.get(state);
    }

    private SendMessage answerAuthDataMessage(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите логин и пароль через пробел, чтобы авторизоваться: ");
        int updated = userService.updateStateByChatId(States.AUTH.toString(),chatId);
        return sendMessage;
    }

    private SendMessage authMessage(long chatId, String token, String messageText){

        //спрашиваем API с логином и паролем
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Вы успешно авторизованы как: "+messageText);
        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));
        userService.updateStateByChatId(States.MAIN_MENU.toString(),chatId);
        return sendMessage;
    }

    private SendMessage mainMenuMessage(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите действие: " + "\nВы отправили: " + messageText);
        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));
        return sendMessage;
    }

    private SendMessage askPostIdMessage(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id поста: ");

        userService.updateStateByChatId(States.GET_POST.toString(),chatId);

        return sendMessage;
    }

    private SendMessage getPostMessage(long chatId, String token, String messageText) {

        //ApiWorker apiWorker = ApiWorker.getApiWorker();

        Post post = apiWorker.getPost(Integer.parseInt(messageText));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(post.getBody() + "\nВыберите действие: ");

        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));

        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

}
