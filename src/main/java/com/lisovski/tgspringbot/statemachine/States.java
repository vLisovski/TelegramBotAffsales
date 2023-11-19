package com.lisovski.tgspringbot.statemachine;

public enum States {

    ANSWER_AUTH_DATA,
        AUTH,
    LOGOUT,

    MAIN_MENU,

    ASK_POST_ID,
        GET_POST,

    ASK_OFFER_ID_FOR_FLOWS,
        GET_FLOWS,

    ASK_OFFER_ID_FOR_OFFERS,
        GET_OFFERS,

    ASK_OFFER_ID_FOR_CREATE_FLOW,
        CREATE_FLOW,

    ASK_FLOW_ID_FOR_DELETE_FLOW,
        DELETE_FLOW,

    ASK_FLOW_ID_FOR_UPDATE_FLOW,
        UPDATE_FLOW_MENU,
            ASK_FLOW_NAME,
                UPDATE_FLOW_NAME,
            ASK_FLOW_UTM_SOURCE,
                UPDATE_UTM_SOURCE,
            ASK_FLOW_UTM_CONTENT,
                UPDATE_UTM_CONTENT,
            ASK_FLOW_UTM_CAMPAIGN,
                UPDATE_UTM_CAMPAIGN,
            ASK_FLOW_UTM_TERM,
                UPDATE_UTM_TERM,
            ASK_FLOW_UTM_MEDIUM,
                UPDATE_UTM_MEDIUM,
        UPDATE_FLOW
}
