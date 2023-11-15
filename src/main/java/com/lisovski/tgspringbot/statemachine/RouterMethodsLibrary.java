package com.lisovski.tgspringbot.statemachine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lisovski.tgspringbot.api.AlterCPAWorker;
import com.lisovski.tgspringbot.api.ApiWorker;
import com.lisovski.tgspringbot.dtos.AuthAnswer;
import com.lisovski.tgspringbot.dtos.AuthRequest;
import com.lisovski.tgspringbot.dtos.CreateFlowAnswer;
import com.lisovski.tgspringbot.models.Flow;
import com.lisovski.tgspringbot.models.Offer;
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
    private final AlterCPAWorker alterCPAWorker;
    private final UserService userService;


    @Autowired
    public RouterMethodsLibrary(UserService userService, ApiWorker apiWorker, AlterCPAWorker alterCPAWorker) {
        this.userService = userService;
        this.apiWorker = apiWorker;
        this.alterCPAWorker = alterCPAWorker;
        messageMap = new HashMap<>();
        messageMap.put(States.MAIN_MENU.toString(), this::mainMenuMessage);

        messageMap.put(States.ANSWER_AUTH_DATA.toString(), this::answerAuthDataMessage);
        messageMap.put(States.AUTH.toString(), this::authMessage);

        messageMap.put(States.ASK_POST_ID.toString(), this::askPostIdMessage);
        messageMap.put(States.GET_POST.toString(), this::getPostMessage);

        messageMap.put(States.ASK_OFFER_ID_FOR_OFFERS.toString(), this::askOfferIdMessageForOffers);
        messageMap.put(States.GET_OFFERS.toString(), this::getOffersMessage);

        messageMap.put(States.ASK_OFFER_ID_FOR_FLOWS.toString(), this::askOfferIdMessageForGetFlows);
        messageMap.put(States.GET_FLOWS.toString(), this::getFlowsMessage);

        messageMap.put(States.ASK_OFFER_ID_FOR_CREATE_FLOW.toString(), this::askOfferIdForCreateFlow);
        messageMap.put(States.CREATE_FLOW.toString(), this::createFlowMessage);
    }

    public Routable getMethodByState(String state) {
        return messageMap.get(state);
    }

    private SendMessage answerAuthDataMessage(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите через пробел логин и пароль от личного кабинета AFFSALES, чтобы авторизоваться: ");
        int updated = userService.updateStateByChatId(States.AUTH.toString(), chatId);
        return sendMessage;
    }

    private SendMessage authMessage(long chatId, String token, String messageText) {

        final int MIN_VALID_AUTH_MESSAGE_TEXT_LENGTH = 10;

        SendMessage sendMessage = new SendMessage();

        //проверяем сообщение на общую корректность (фильтруем треш)
        if (!messageText.contains("@") || !messageText.contains(" ") || messageText.length() < MIN_VALID_AUTH_MESSAGE_TEXT_LENGTH) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Отсутствует email, пробел или данные некорректны.\nПовторите ввод email и пароля через пробел:");
        } else {
            //спрашиваем API с логином и паролем
            String[] emailAndPasswordAsArray = messageText.split(" ");

            AuthRequest authRequest = AuthRequest.builder()
                    .in_user(emailAndPasswordAsArray[0])
                    .in_pass(emailAndPasswordAsArray[1])
                    .build();

            AuthAnswer authAnswer = alterCPAWorker.auth(authRequest);

            if (authAnswer.getStatus().equals("ok")) {

                sendMessage.setChatId(chatId);
                sendMessage.setText("Вы успешно авторизованы, " + authAnswer.getName() + "!");

                Keyboards keyboards = new Keyboards();
                sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));
                int i = userService.updateTokenAndStateAndIdByChatId(authAnswer.getApi(), States.MAIN_MENU.toString(), authAnswer.getId(), chatId);

            } else {
                sendMessage.setChatId(chatId);
                sendMessage.setText("Неверный email " + authRequest.getIn_user() + " или пароль " + authRequest.getIn_pass()
                        + "\nПовторите ввод email и пароля через пробел:");
            }
        }

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

    private SendMessage askPostIdMessage(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id поста: ");

        userService.updateStateByChatId(States.GET_POST.toString(), chatId);

        return sendMessage;
    }

    private SendMessage getPostMessage(long chatId, String token, String messageText) {

        Post post = apiWorker.getPost(Integer.parseInt(messageText));

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(post.getTitle() + "\nВыберите действие: ");

        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));

        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askOfferIdMessageForOffers(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id офера, если нужна информация по одному офферу." +
                "\nЕсли хотите получить полный список офферов, введите 0.");

        userService.updateStateByChatId(States.GET_OFFERS.toString(), chatId);

        return sendMessage;
    }

    private SendMessage getOffersMessage(long chatId, String token, String messageText) {

        int offerId;
        SendMessage sendMessage = new SendMessage();

        try {
            offerId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id офера, если нужна информация по одному офферу." +
                    "\nЕсли хотите получить полный список офферов, введите 0.");
            return sendMessage;
        }

        try {
            String answer = alterCPAWorker.getOffers(token, offerId);

            Gson gson = new Gson();

            Map<String, Offer> offerMap = gson.fromJson(answer, new TypeToken<HashMap<String, Offer>>() {
            }.getType());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\t").append("ID").append("\t").append("НАИМЕНОВАНИЕ").append("\n");

            offerMap.forEach((key, value) -> {
                stringBuilder.append("\t").append(value.getId()).append("\t").append(value.getName()).append("\n");
            });

            sendMessage.setText(stringBuilder.toString());
        } catch (Exception e) {
            System.out.println("мы обосрались, оно не мапится");
            e.printStackTrace();
            sendMessage.setText("GSON");
        }

        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askOfferIdMessageForGetFlows(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id офера, по которому нужно получить список потоков:");

        userService.updateStateByChatId(States.GET_FLOWS.toString(), chatId);

        return sendMessage;
    }

    private SendMessage getFlowsMessage(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        int offerId;

        try {
            offerId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id офера, если нужна информация по одному офферу." +
                    "\nЕсли хотите получить полный список офферов, введите 0.");
            return sendMessage;
        }

        String answer;

        try{
            answer = alterCPAWorker.getFlows(token, offerId);
        }catch (Exception e){
            answer = "";
            e.printStackTrace();
        }

        try {

            Gson gson = new Gson();

            Map<String, Flow> flowsMap = gson.fromJson(answer, new TypeToken<HashMap<String, Flow>>() {
            }.getType());

            StringBuilder stringBuilder = new StringBuilder();

            flowsMap.forEach((key, value) -> {

                String urlWithoutSpaceUrl = value.getUrl();
                String spaceUrl = value.getSpaceurl();
                if(spaceUrl.equals("false")){
                    spaceUrl = " ";
                }
                String urlWithSpaceUrl = urlWithoutSpaceUrl.replaceAll("///","//"+spaceUrl+"/");

                stringBuilder
                        .append("ID потока: ")
                        .append(value.getId())
                        .append("\n")

                        .append("URL: ")
                        .append(urlWithSpaceUrl)

                        .append(" Имя: ")
                        .append(value.getName())
                        .append("\n")

                        .append("Метки: utn_campaign=")
                        .append(value.getUtm_campaign())
                        .append(" utm_source=")
                        .append(value.getUtm_source())
                        .append(" utm_medium=")
                        .append(value.getUtm_medium())
                        .append(" utm_term=")
                        .append(value.getUtm_term())
                        .append(" utm_content=")
                        .append(value.getUtm_content())
                        .append("\n")
                        .append("\n")
                ;
            });

            sendMessage.setText(stringBuilder.toString());
        } catch (Exception e) {
            System.out.println("Ошибка при парсинге");
            e.printStackTrace();
            sendMessage.setText("GSON OR SERVER");
        }

        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askOfferIdForCreateFlow(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id офера, по которому нужно создать поток:");

        userService.updateStateByChatId(States.CREATE_FLOW.toString(), chatId);

        return sendMessage;
    }

    private SendMessage createFlowMessage(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        int offerId;

        try {
            offerId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id офера, если нужна информация по одному офферу." +
                    "\nЕсли хотите получить полный список офферов, введите 0.");
            return sendMessage;
        }

        CreateFlowAnswer answer;

        try{
            answer = alterCPAWorker.createFlow(token, offerId);
        }catch (Exception e){
            answer = new CreateFlowAnswer();
            e.printStackTrace();
        }

        if(answer.getStatus().equals("ok")){
            sendMessage.setText("Создан поток с id="+answer.getId());
        }else{
            sendMessage.setText("Ошибка создания потока. Описание ошибки: "+answer.getError());
        }

        Keyboards keyboards = new Keyboards();
        sendMessage.setReplyMarkup(keyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askFlowIdForDeleteFlow(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id офера, по которому нужно создать поток:");

        userService.updateStateByChatId(States.CREATE_FLOW.toString(), chatId);

        return sendMessage;
    }

}
