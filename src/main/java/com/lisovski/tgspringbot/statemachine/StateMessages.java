package com.lisovski.tgspringbot.statemachine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lisovski.tgspringbot.api.AlterCPAWorker;
import com.lisovski.tgspringbot.dtos.*;
import com.lisovski.tgspringbot.models.Flow;
import com.lisovski.tgspringbot.models.Offer;
import com.lisovski.tgspringbot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class StateMessages {

    private final Map<String, Routable> messageMap;
    private final AlterCPAWorker alterCPAWorker;
    private final UserService userService;
    private final DataStorage dataStorage;

    @Autowired
    public StateMessages(UserService userService, AlterCPAWorker alterCPAWorker, DataStorage dataStorage) {
        //fields init
        this.userService = userService;
        this.alterCPAWorker = alterCPAWorker;
        this.dataStorage = dataStorage;
        //init map
        messageMap = new HashMap<>();
        //put messages to map:
        //main menu
        messageMap.put(States.MAIN_MENU.toString(), this::mainMenu);
        //auth
        messageMap.put(States.ANSWER_AUTH_DATA.toString(), this::answerAuthData);
        messageMap.put(States.AUTH.toString(), this::auth);
        messageMap.put(States.LOGOUT.toString(), this::logout);
        //get offers
        messageMap.put(States.ASK_OFFER_ID_FOR_OFFERS.toString(), this::askOfferIdForOffers);
        messageMap.put(States.GET_OFFERS.toString(), this::getOffers);
        //get flows
        messageMap.put(States.ASK_OFFER_ID_FOR_FLOWS.toString(), this::askOfferIdForGetFlows);
        messageMap.put(States.GET_FLOWS.toString(), this::getFlows);
        //create flow
        messageMap.put(States.ASK_OFFER_ID_FOR_CREATE_FLOW.toString(), this::askOfferIdForCreateFlow);
        messageMap.put(States.CREATE_FLOW.toString(), this::createFlow);
        //delete flow
        messageMap.put(States.ASK_FLOW_ID_FOR_DELETE_FLOW.toString(), this::askFlowIdForDeleteFlow);
        messageMap.put(States.DELETE_FLOW.toString(), this::deleteFlow);
        //update flow
        messageMap.put(States.ASK_FLOW_ID_FOR_UPDATE_FLOW.toString(), this::askFlowIdForUpdateFlow);
        messageMap.put(States.UPDATE_FLOW_MENU.toString(), this::updateFlowMenu);
        //update flow name
        messageMap.put(States.ASK_FLOW_NAME.toString(), this::askFlowNameForUpdateFlow);
        messageMap.put(States.UPDATE_FLOW_NAME.toString(), this::updateFlowName);
        //update utm_source
        messageMap.put(States.ASK_FLOW_UTM_SOURCE.toString(), this::askUtmSourceForUpdateFlow);
        messageMap.put(States.UPDATE_UTM_SOURCE.toString(), this::updateFlowUtmSource);
        //update utm_content
        messageMap.put(States.ASK_FLOW_UTM_CONTENT.toString(), this::askUtmContentForUpdateFlow);
        messageMap.put(States.UPDATE_UTM_CONTENT.toString(), this::updateFlowUtmContent);
        //update utm_campaign
        messageMap.put(States.ASK_FLOW_UTM_CAMPAIGN.toString(), this::askUtmCampaignForUpdateFlow);
        messageMap.put(States.UPDATE_UTM_CAMPAIGN.toString(), this::updateFlowUtmCampaign);
        //update utm_term
        messageMap.put(States.ASK_FLOW_UTM_TERM.toString(), this::askUtmTermForUpdateFlow);
        messageMap.put(States.UPDATE_UTM_TERM.toString(), this::updateFlowUtmTerm);
        //update utm_medium
        messageMap.put(States.ASK_FLOW_UTM_MEDIUM.toString(), this::askUtmMediumForUpdateFlow);
        messageMap.put(States.UPDATE_UTM_MEDIUM.toString(), this::updateFlowUtmMedium);
        messageMap.put(States.UPDATE_FLOW.toString(), this::updateFlow);
        //get statistic
        //ask statistic item
        messageMap.put(States.ASK_ITEM_FOR_STATISTIC.toString(), this::askItemForStatistic);
        messageMap.put(States.UPDATE_ITEM_FOR_STATISTIC.toString(), this::updateItemForStatistic);
        messageMap.put(States.UPDATE_FROM.toString(), this::updateFromForStatistic);
        messageMap.put(States.UPDATE_TO.toString(), this::updateToForStatistic);
        //statistic filters menu
        messageMap.put(States.STATISTIC_FILTERS_MENU.toString(), this::statisticFiltersMenu);
        //update offer id for statistic filter
        messageMap.put(States.ASK_OFFER_STAT.toString(), this::askOfferIdForStatisticFilter);
        messageMap.put(States.UPDATE_OFFER_STAT.toString(), this::updateOfferIdForStatisticFilter);
        //update flow id for statistic filter
        messageMap.put(States.ASK_FLOW_STAT.toString(), this::askFlowIdForStatisticFilter);
        messageMap.put(States.UPDATE_FLOW_STAT.toString(), this::updateFlowIdForStatisticFilter);
        //update utm_source for statistic filter
        messageMap.put(States.ASK_FLOW_UTM_SOURCE_STAT.toString(), this::askUtmSourceForStatisticFilter);
        messageMap.put(States.UPDATE_UTM_SOURCE_STAT.toString(), this::updateUtmSourceForStatisticFilter);
        //update utm_content for statistic filter
        messageMap.put(States.ASK_FLOW_UTM_CONTENT_STAT.toString(), this::askUtmContentForStatisticFilter);
        messageMap.put(States.UPDATE_UTM_CONTENT_STAT.toString(), this::updateUtmContentForStatisticFilter);
        //update utm_campaign for statistic filter
        messageMap.put(States.ASK_FLOW_UTM_CAMPAIGN_STAT.toString(), this::askUtmCampaignForStatisticFilter);
        messageMap.put(States.UPDATE_UTM_CAMPAIGN_STAT.toString(), this::updateUtmCampaignForStatisticFilter);
        //update utm_term for statistic filter
        messageMap.put(States.ASK_FLOW_UTM_TERM_STAT.toString(), this::askUtmTermForStatisticFilter);
        messageMap.put(States.UPDATE_UTM_TERM_STAT.toString(), this::updateUtmTermForStatisticFilter);
        //update utm_medium for statistic filter
        messageMap.put(States.ASK_FLOW_UTM_MEDIUM_STAT.toString(), this::askUtmMediumForStatisticFilter);
        messageMap.put(States.UPDATE_UTM_MEDIUM_STAT.toString(), this::updateUtmMediumForStatisticFilter);
        //get statistic
        messageMap.put(States.GET_STATISTIC.toString(), this::getStatistic);
    }

    public Routable getMethodByState(String state) {
        return messageMap.get(state);
    }

    private SendMessage answerAuthData(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите через пробел логин и пароль от личного кабинета AFFSALES, чтобы авторизоваться:");
        userService.updateStateByChatId(States.AUTH.toString(), chatId);
        return sendMessage;
    }

    private SendMessage auth(long chatId, String token, String messageText) {

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

            AuthAnswer authAnswer;

            try {
                authAnswer = alterCPAWorker.auth(authRequest);
            } catch (Exception e) {
                e.printStackTrace();
                authAnswer = new AuthAnswer();
                authAnswer.setStatus("error");
                sendMessage.setChatId(chatId);
                sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера." +
                        "\nВведите через пробел логин и пароль от личного кабинета AFFSALES, чтобы авторизоваться:");
                return sendMessage;
            }

            if (authAnswer.getStatus().equals("ok")) {

                sendMessage.setChatId(chatId);
                //проверяем, авторизован ли этот аккаунт в другом chatId
                if(userService.existsByToken(authAnswer.getApi())){
                    sendMessage.setText("Этот аккаунт уже авторизован в другом Telegram аккаунте. " +
                            "\nВведите через пробел логин и пароль от личного кабинета AFFSALES, чтобы авторизоваться:");
                    return sendMessage;
                }

                sendMessage.setText("Добро пожаловать, "+authAnswer.getName()+"!\nВы в главном меню:");

                ReplyKeyboards replyKeyboards = new ReplyKeyboards();
                sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
                userService.updateTokenAndStateAndIdByChatId(authAnswer.getApi(), States.MAIN_MENU.toString(), authAnswer.getId(), chatId);

            } else {
                sendMessage.setChatId(chatId);
                sendMessage.setText("Неверный email " + authRequest.getIn_user() + " или пароль " + authRequest.getIn_pass()
                        + "\nПовторите ввод email и пароля через пробел:");
            }
        }

        return sendMessage;
    }

    private SendMessage logout(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        sendMessage.setText("Вы вышли из аккаунта.\nВведите через пробел логин и пароль от личного кабинета AFFSALES, чтобы авторизоваться:");

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        replyKeyboardRemove.setSelective(false);

        sendMessage.setReplyMarkup(replyKeyboardRemove);

        userService.refreshUserByChatId(chatId);

        return sendMessage;
    }

    private SendMessage mainMenu(long chatId, String token, String messageText) {
        //попадание в главное меню чистит временное хранилище для редактирования потока
        if (dataStorage.get(chatId) != null) {
            dataStorage.delete(chatId);
        }
        //попадание в главное меню устанавливает State равным MAIN_MENU (нужно для работы кнопки "Назад")
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Вы отправили: " + messageText + "\nВы в главном меню:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));

        return sendMessage;
    }

    private SendMessage askOfferIdForOffers(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id офера, если нужна информация по одному офферу." +
                "\nЕсли хотите получить полный список офферов, введите 0.");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_OFFER_ID_FOR_OFFERS));

        userService.updateStateByChatId(States.GET_OFFERS.toString(), chatId);

        return sendMessage;
    }

    private SendMessage getOffers(long chatId, String token, String messageText) {

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

        String answer;

        try {
            answer = alterCPAWorker.getOffers(token, offerId);
        } catch (Exception e) {
            e.printStackTrace();

            sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера.\nВы в главном меню:");
            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setChatId(chatId);
            userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

            return sendMessage;
        }

        try {
            Gson gson = new Gson();

            Map<String, Offer> offerMap = gson.fromJson(answer, new TypeToken<HashMap<String, Offer>>() {
            }.getType());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ID").append("\t").append("НАИМЕНОВАНИЕ").append("\n");

            offerMap.forEach((key, value) -> {
                stringBuilder.append(value.getId()).append("\t").append(value.getName()).append("\n");
            });

            String sendMessageText = stringBuilder.toString();
            sendMessageText = sendMessageText.replaceAll("&#34;","\"");

            sendMessage.setText(sendMessageText);
        } catch (Exception e) {
            System.out.println("Ошибка парсинга списка офферов.");
            e.printStackTrace();
            sendMessage.setText("Ошибка. Неуданчый парсинг списка офферов(\nВы в главном меню:");
        }

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askOfferIdForGetFlows(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id офера, по которому нужно получить список потоков:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_OFFER_ID_FOR_FLOWS));

        userService.updateStateByChatId(States.GET_FLOWS.toString(), chatId);

        return sendMessage;
    }

    private SendMessage getFlows(long chatId, String token, String messageText) {

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

        try {
            answer = alterCPAWorker.getFlows(token, offerId);
        } catch (Exception e) {
            e.printStackTrace();

            sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера.\nВы в главном меню:");
            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setChatId(chatId);

            userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

            return sendMessage;
        }

        try {

            Gson gson = new Gson();

            Map<String, Flow> flowsMap = gson.fromJson(answer, new TypeToken<HashMap<String, Flow>>() {
            }.getType());

            StringBuilder stringBuilder = new StringBuilder();

            flowsMap.forEach((key, value) -> {

                String urlWithoutSpaceUrl = value.getUrl();
                String spaceUrl = value.getSpaceurl();
                if (spaceUrl.equals("false")) {
                    spaceUrl = " ";
                }
                String urlWithSpaceUrl = urlWithoutSpaceUrl.replaceAll("///", "//" + spaceUrl + "/");

                stringBuilder
                        .append("ID потока: ")
                        .append(value.getId())
                        .append("\n")

                        .append("URL: ")
                        .append(urlWithSpaceUrl)
                        .append("\n")

                        .append("Имя: ")
                        .append(value.getName())
                        .append("\n")

                        .append("Метки: utn_campaign=")
                        .append(value.getUtm_campaign())
                        .append(", utm_source=")
                        .append(value.getUtm_source())
                        .append(", utm_medium=")
                        .append(value.getUtm_medium())
                        .append(", utm_term=")
                        .append(value.getUtm_term())
                        .append(", utm_content=")
                        .append(value.getUtm_content())
                        .append("\n")
                        .append("\n")
                ;
            });

            sendMessage.setText(stringBuilder.toString());
        } catch (Exception e) {
            System.out.println("Ошибка при парсинге списка потоков");
            e.printStackTrace();
            sendMessage.setText("Ошибка. Неудачный парсинг списка потоков(\nВы в главном меню:");
        }

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askOfferIdForCreateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id оффера, по которому нужно создать поток:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_OFFER_ID_FOR_CREATE_FLOW));

        userService.updateStateByChatId(States.CREATE_FLOW.toString(), chatId);

        return sendMessage;
    }

    private SendMessage createFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        int offerId;

        try {
            offerId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id оффера, по которому нужно создать поток:");
            return sendMessage;
        }

        CreateFlowAnswer answer;

        try {
            answer = alterCPAWorker.createFlow(token, offerId);
        } catch (Exception e) {
            e.printStackTrace();

            sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера.\nВы в главном меню:");
            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setChatId(chatId);
            userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

            return sendMessage;
        }

        if (answer.getStatus().equals("ok")) {
            sendMessage.setText("Создан поток с id=" + answer.getId());
        } else {
            sendMessage.setText("Ошибка создания потока. Описание ошибки: " + answer.getError() + "\nВы в главном меню:");
        }

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askFlowIdForDeleteFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id потока, который нужно удалить:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_ID_FOR_DELETE_FLOW));

        userService.updateStateByChatId(States.DELETE_FLOW.toString(), chatId);

        return sendMessage;
    }

    private SendMessage deleteFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        int flowId;

        try {
            flowId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id потока, который нужно удалить.");
            return sendMessage;
        }

        DeleteFlowAnswer answer;

        try {
            answer = alterCPAWorker.deleteFlow(token, flowId);
        } catch (Exception e) {
            e.printStackTrace();

            sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера.\nВы в главном меню:");
            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setChatId(chatId);
            userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

            return sendMessage;
        }

        if (answer.getStatus().equals("ok")) {
            sendMessage.setText("Поток успешно удалён");
        } else {
            sendMessage.setText("Ошибка удаления потока. Описание ошибки: " + answer.getError() + "\nВы в главном меню:");
        }

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
        sendMessage.setChatId(chatId);
        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        return sendMessage;
    }

//-------------------------------------------------update flow---------------------------------------------------------
    private SendMessage askFlowIdForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id потока, который нужно изменить:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_ID_FOR_UPDATE_FLOW));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowMenu(long chatId, String token, String messageText) {

        //попадание в меню редактирования потока устанавливает State равным UPDATE_FLOW_MENU (нужно для работы кнопки "Назад")
        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        //проверяем, собирается ли объект UpdateFLowRequest для текущего chatId
        if (dataStorage.get(chatId) == null) {

            int flowId;

            try {
                flowId = Integer.parseInt(messageText);
            } catch (Exception e) {
                sendMessage.setText("Введенный id не является целым числом." +
                        "\nВведите id потока, который нужно отредактировать.");
                return sendMessage;
            }

            UpdateFlowRequest updateFlowRequest = new UpdateFlowRequest();
            updateFlowRequest.setFlow(flowId);

            dataStorage.add(chatId, updateFlowRequest);
        }

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";

        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        return sendMessage;
    }

    private SendMessage askFlowNameForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите новое имя потока:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_NAME));

        userService.updateStateByChatId(States.UPDATE_FLOW_NAME.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowName(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        updateFlowRequest.setName(messageText);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";
        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmSourceForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите новое значение метки utm_source:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_SOURCE));

        userService.updateStateByChatId(States.UPDATE_UTM_SOURCE.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowUtmSource(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        updateFlowRequest.setUtms(messageText);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";
        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmContentForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите новое значение метки utm_content:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_CONTENT));

        userService.updateStateByChatId(States.UPDATE_UTM_CONTENT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowUtmContent(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        updateFlowRequest.setUtmn(messageText);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";
        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmCampaignForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите новое значение метки utm_campaign:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_CAMPAIGN));

        userService.updateStateByChatId(States.UPDATE_UTM_CAMPAIGN.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowUtmCampaign(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        updateFlowRequest.setUtmc(messageText);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";
        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmTermForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите новое значение метки utm_term:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_TERM));

        userService.updateStateByChatId(States.UPDATE_UTM_TERM.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowUtmTerm(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        updateFlowRequest.setUtmt(messageText);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";
        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmMediumForUpdateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите новое значение метки utm_medium:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_MEDIUM));

        userService.updateStateByChatId(States.UPDATE_UTM_MEDIUM.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowUtmMedium(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        updateFlowRequest.setUtmm(messageText);

        String sendMessageText = "ID редактируемого потока: " + updateFlowRequest.getFlow() + "\n";
        if(updateFlowRequest.getName()!=null){
            sendMessageText = sendMessageText + "Имя потока изменится на: " + updateFlowRequest.getName() + "\n";
        }else{
            sendMessageText = sendMessageText + "Имя потока изменится на:\n";
        }
        if(updateFlowRequest.getUtms()!=null){
            sendMessageText = sendMessageText + "utm_source метка примет значение: " + updateFlowRequest.getUtms() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_source метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmn()!=null){
            sendMessageText = sendMessageText + "utm_content метка примет значение: " + updateFlowRequest.getUtmn() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_content метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmc()!=null){
            sendMessageText = sendMessageText + "utm_campaign метка примет значение: " + updateFlowRequest.getUtmc() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_campaign метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmt()!=null){
            sendMessageText = sendMessageText + "utm_term метка примет значение: " + updateFlowRequest.getUtmt() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_term метка примет значение:\n";
        }
        if(updateFlowRequest.getUtmm()!=null){
            sendMessageText = sendMessageText + "utm_medium метка примет значение: " + updateFlowRequest.getUtmm() + "\n";
        }else{
            sendMessageText = sendMessageText + "utm_medium метка примет значение:\n";
        }
        sendMessageText = sendMessageText + "\nВыберите параметр потока, который нужно изменить:";
        sendMessage.setText(sendMessageText);

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

        userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlow(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        UpdateFlowRequest updateFlowRequest = (UpdateFlowRequest) dataStorage.get(chatId);
        UpdateFlowAnswer answer;

        if (updateFlowRequest.equals(
                UpdateFlowRequest.builder()
                        .flow(updateFlowRequest.getFlow())
                        .name(null)
                        .utms(null)
                        .utmc(null)
                        .utmn(null)
                        .utmm(null)
                        .utmt(null)
                        .build())
        ) {
            sendMessage.setText("Задайте хотя бы один параметр для изменения. " +
                    "Чтобы прекратить изменение и вернуться в главное меню, нажмите \"Назад\" ");

            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.UPDATE_FLOW_MENU));

            userService.updateStateByChatId(States.UPDATE_FLOW_MENU.toString(), chatId);

            return sendMessage;
        }

        try {
            answer = alterCPAWorker.updateFlow(token, updateFlowRequest);
        } catch (Exception e) {
            e.printStackTrace();

            sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера.\nВы в главном меню:");

            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setChatId(chatId);

            userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

            return sendMessage;
        }

        if (answer.getStatus().equals("ok")) {
            sendMessage.setText("Параметры потока успешно обновлены.\nВы в главном меню:");
        } else {
            sendMessage.setText("Ошибка обновления параметров потока. Описание ошибки: " + answer.getError() + "\nВы в главном меню:");
        }

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));

        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        dataStorage.delete(chatId);

        return sendMessage;
    }

//--------------------------------------------STATISTIC----------------------------------------------------------------
    private SendMessage askItemForStatistic(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выберите элемент, по которому следует сгрупировать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_ITEM_FOR_STATISTIC));

        userService.updateStateByChatId(States.UPDATE_ITEM_FOR_STATISTIC.toString(), chatId);

        return sendMessage;
    }

    private String convertToStringItem(String messageText) {

        switch (messageText) {
            case "ID оффера":
                return "offer";
            case "ID потока":
                return "flow";
            case "utm_source":
                return "utms";
            case "utm_content":
                return "utmn";
            case "utm_campaign":
                return "utmc";
            case "utm_term":
                return "utmt";
            case "utm_medium":
                return "utmm";
            default:
                return "flow";
        }

    }

    private SendMessage updateItemForStatistic(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите дату в формате ГГГГ-ММ-ДД, от которой следует отобразить статистику." +
                "По умолчанию используется дата неделю назад. Для значения по умолчанию введите 0.");

        StatisticRequest statisticRequest = StatisticRequest.builder()
                .item(convertToStringItem(messageText))
                .from(" ")
                .to(" ")
                .offer(0)
                .flow(0)
                .utms(" ")
                .utmc(" ")
                .utmn(" ")
                .utmt(" ")
                .utmm(" ")
                .build();

        dataStorage.add(chatId, statisticRequest);

        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        replyKeyboardRemove.setSelective(false);

        sendMessage.setReplyMarkup(replyKeyboardRemove);

        userService.updateStateByChatId(States.UPDATE_FROM.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFromForStatistic(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        String regexp = "^(19|20)\\d\\d[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])$";//ГГГГ-ММ-ДД
        Pattern pattern = Pattern.compile(regexp);
        //проверка на оставление даты по умолчанию через ввод нуля
        try{
            int i;
            i = Integer.parseInt(messageText);
            if(i==0){
                statisticRequest.setFrom(" ");
                sendMessage.setText("Введите дату в формате ГГГГ-ММ-ДД, до которой следует отобразить статистику. По умолчанию используется сегодняшний день. Для значения по умолчанию введите 0.");
                userService.updateStateByChatId(States.UPDATE_TO.toString(), chatId);
                return sendMessage;
            }else {
                throw new Exception();
            }
        }catch (Exception e){
            sendMessage.setText("Неверный формат даты. Повторите ввод согласно формату ГГГГ-ММ-ДД. Для значения по умолчанию введите 0.");
        }

        if(messageText.matches(regexp)){
            sendMessage.setText("Введите дату в формате ГГГГ-ММ-ДД, до которой следует отобразить статистику. По умолчанию используется сегодняшний день. Для значения по умолчанию введите 0.");
            statisticRequest.setFrom(messageText);
            userService.updateStateByChatId(States.UPDATE_TO.toString(), chatId);
        }

        return sendMessage;
    }

    private SendMessage updateToForStatistic(long chatId, String token, String messageText) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        String regexp = "^(19|20)\\d\\d[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])$";//ГГГГ-ММ-ДД
        Pattern pattern = Pattern.compile(regexp);
        //проверка на оставление даты по умолчанию через ввод нуля
        try{
            int i;
            i = Integer.parseInt(messageText);
            if(i==0){
                statisticRequest.setTo(" ");
                String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
                if(!statisticRequest.getFrom().equals(" ")){
                    sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
                }else{
                    sendMessageText = sendMessageText + "От даты: неделю назад\n";
                }
                if(!statisticRequest.getTo().equals(" ")){
                    sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
                }else{
                    sendMessageText = sendMessageText + "До даты: сегодня\n";
                }
                if(statisticRequest.getOffer()!=0){
                sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
                }else{
                    sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
                }
                if(statisticRequest.getFlow()!=0){
                    sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
                }else{
                    sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
                }
                sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                        + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                        + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                        + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                        + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                        + "\nЗадайте фильтры, если требуется:"
                );
                userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

                ReplyKeyboards replyKeyboards = new ReplyKeyboards();
                sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

                return sendMessage;
            }else {
                throw new Exception();
            }
        }catch (Exception e){
            sendMessage.setText("Неверный формат даты. Повторите ввод согласно формату ГГГГ-ММ-ДД. Для значения по умолчанию введите 0.");
        }

        if(messageText.matches(regexp)){
            statisticRequest.setTo(messageText);
            String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
            if(!statisticRequest.getFrom().equals(" ")){
                sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
            }else{
                sendMessageText = sendMessageText + "От даты: неделю назад\n";
            }
            if(!statisticRequest.getTo().equals(" ")){
                sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
            }else{
                sendMessageText = sendMessageText + "До даты: сегодня\n";
            }
            if(statisticRequest.getOffer()!=0){
                sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
            }else{
                sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
            }
            if(statisticRequest.getFlow()!=0){
                sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
            }else{
                sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
            }
            sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                    + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                    + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                    + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                    + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                    + "\nЗадайте фильтры, если требуется:"
            );
            userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);
            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));
        }

        return sendMessage;
    }

    private SendMessage statisticFiltersMenu(long chatId, String token, String messageText){

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        return sendMessage;
    }

    private SendMessage askOfferIdForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id оффера, по которому нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_OFFER_STAT));

        userService.updateStateByChatId(States.UPDATE_OFFER_STAT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateOfferIdForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        int offerId;

        try {
            offerId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id оффера, по которому нужно фильтровать статистику:");
            return sendMessage;
        }

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setOffer(offerId);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askFlowIdForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите id потока, по которому нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_STAT));

        userService.updateStateByChatId(States.UPDATE_FLOW_STAT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateFlowIdForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        int flowId;

        try {
            flowId = Integer.parseInt(messageText);
        } catch (Exception e) {
            sendMessage.setText("Введенный id не является целым числом." +
                    "\nВведите id потока, по которому нужно фильтровать статистику:");
            return sendMessage;
        }

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setFlow(flowId);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmSourceForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите значение utm_source метки, по которой нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_SOURCE_STAT));

        userService.updateStateByChatId(States.UPDATE_UTM_SOURCE_STAT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateUtmSourceForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setUtms(messageText);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmContentForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите значение utm_content метки, по которой нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_CONTENT_STAT));

        userService.updateStateByChatId(States.UPDATE_UTM_CONTENT_STAT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateUtmContentForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setUtmn(messageText);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmCampaignForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите значение utm_campaign метки, по которой нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_CAMPAIGN_STAT));

        userService.updateStateByChatId(States.UPDATE_UTM_CAMPAIGN_STAT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateUtmCampaignForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setUtmc(messageText);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmTermForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите значение utm_term метки, по которой нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_TERM_STAT));

        userService.updateStateByChatId(States.UPDATE_UTM_TERM.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateUtmTermForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setUtmt(messageText);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage askUtmMediumForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите значение utm_medium метки, по которой нужно фильтровать статистику:");

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.ASK_FLOW_UTM_MEDIUM_STAT));

        userService.updateStateByChatId(States.UPDATE_UTM_MEDIUM_STAT.toString(), chatId);

        return sendMessage;
    }

    private SendMessage updateUtmMediumForStatisticFilter(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        statisticRequest.setUtmm(messageText);

        String sendMessageText = "Элемент группировки: " + statisticRequest.getItem() + "\n";
        if(!statisticRequest.getFrom().equals(" ")){
            sendMessageText = sendMessageText + "От даты: " + statisticRequest.getFrom() + "\n";
        }else{
            sendMessageText = sendMessageText + "От даты: неделю назад\n";
        }
        if(!statisticRequest.getTo().equals(" ")){
            sendMessageText = sendMessageText + "До даты: " + statisticRequest.getTo() + "\n";
        }else{
            sendMessageText = sendMessageText + "До даты: сегодня\n";
        }
        if(statisticRequest.getOffer()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID оффера = " + statisticRequest.getOffer() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID оффера =\n";
        }
        if(statisticRequest.getFlow()!=0){
            sendMessageText = sendMessageText + "Фильтрация: ID потока = " + statisticRequest.getFlow() + "\n";
        }else{
            sendMessageText = sendMessageText + "Фильтрация: ID потока =\n";
        }
        sendMessage.setText(sendMessageText + "Фильтрация: utm_source = " + statisticRequest.getUtms() + "\n"
                + "Фильтрация: utm_content = " + statisticRequest.getUtmn() + "\n"
                + "Фильтрация: utm_campaign = " + statisticRequest.getUtmc() + "\n"
                + "Фильтрация: utm_term = " + statisticRequest.getUtmt() + "\n"
                + "Фильтрация: utm_medium = " + statisticRequest.getUtmm() + "\n"
                + "\nЗадайте фильтры, если требуется:"
        );

        ReplyKeyboards replyKeyboards = new ReplyKeyboards();
        sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.STATISTIC_FILTERS_MENU));

        userService.updateStateByChatId(States.STATISTIC_FILTERS_MENU.toString(), chatId);

        return sendMessage;
    }

    private SendMessage getStatistic(long chatId, String token, String messageText){

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StatisticRequest statisticRequest = (StatisticRequest) dataStorage.get(chatId);
        String answer;

        try {
            answer = alterCPAWorker.getStatistic(token, statisticRequest);
        } catch (Exception e) {
            e.printStackTrace();

            sendMessage.setText("Ошибка. Превышено время ожидания ответа сервера.\nВы в главном меню:");

            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setChatId(chatId);

            userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

            return sendMessage;
        }

        if(answer.length()>0){
            try {
                Gson gson = new Gson();

                Map<String, StatisticAnswer> statisticMap = gson.fromJson(answer, new TypeToken<HashMap<String, StatisticAnswer>>() {
                }.getType());

                StringBuilder stringBuilder = new StringBuilder();

                statisticMap.forEach((key, value) -> {

                    stringBuilder
                            .append("ID элемента групировки: ")
                            .append(value.getId())
                            .append("\n")

                            .append("Название элемента группировки(если применимо):")
                            .append(value.getName())
                            .append("\n")

                            .append("Количество кликов: ")
                            .append(value.getClicks())
                            .append("\n")

                            .append("Количество уникальных кликов: ")
                            .append(value.getUnique())
                            .append("\n")

                            .append("Общее количество валидных лидов: ")
                            .append(value.getCt())
                            .append("\n")

                            .append("Общая сумма по валидным лидам: ")
                            .append(value.getMt())
                            .append("\n")

                            .append("Количество лидов в статусе «Принят»: ")
                            .append(value.getCa())
                            .append("\n")

                            .append("Сумма по лидам в статусе «Принят»: ")
                            .append(value.getMa())
                            .append("\n")

                            .append("Количество лидов в статусе «Ожидает»: ")
                            .append(value.getCw())
                            .append("\n")

                            .append("Сумма по лидам в статусе «Ожидает»: ")
                            .append(value.getMw())
                            .append("\n")

                            .append("Количество лидов в статусе «Отменён»: ")
                            .append(value.getCc())
                            .append("\n")
                            .append("\n")
                    ;
                });

                sendMessage.setText(stringBuilder.toString());

            } catch (Exception e) {
                System.out.println("Ошибка при парсинге статистики");
                e.printStackTrace();
                sendMessage.setText("Ошибка. Неудачный парсинг статистики(\nВы в главном меню:");
            }

            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));

        }else{
            ReplyKeyboards replyKeyboards = new ReplyKeyboards();
            sendMessage.setReplyMarkup(replyKeyboards.getKeyboardByState(States.MAIN_MENU));
            sendMessage.setText("Статистика по заданным параметрам отсутствует.\nВы в главном меню:");

        }

        userService.updateStateByChatId(States.MAIN_MENU.toString(), chatId);

        dataStorage.delete(chatId);

        return sendMessage;
    }
}
