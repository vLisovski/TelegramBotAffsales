package com.lisovski.tgspringbot.statemachine;

public enum States {

    ANSWER_AUTH_DATA,
        AUTH,

    MAIN_MENU,

    ASK_POST_ID,
        GET_POST,

    ASK_OFFER_ID_FOR_FLOWS,
        GET_FLOWS,

    ASK_OFFER_ID_FOR_OFFERS,
        GET_OFFERS,

    ASK_OFFER_ID_FOR_CREATE_FLOW,
        CREATE_FLOW,

    ASK_OFFER_ID_FOR_DELETE_FLOW,
        DELETE_FLOW,

    ASK_OFFER_ID_FOR_UPDATE_FLOW,
        UPDATE_FLOW
}
