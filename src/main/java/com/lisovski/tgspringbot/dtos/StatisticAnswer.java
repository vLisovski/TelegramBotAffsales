package com.lisovski.tgspringbot.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticAnswer {
    private int id;	//Идентификатор элемента группировки
    private String name; 	//Название элемента группировки, если применимо
    //private int spaces;	//Количество кликов по прелендингам
    //private int suni;	//Количество уникальных кликов по прелендингам
    //private int sgood;	//Количество успешных визитов на прелендинг
    //private long stime;	//Среднее время пользователя на прелендинге в секундах
    private int clicks;	//Количество кликов по лендингам
    private int unique;	//Количество уникальных кликов по лендингам
    //private int good;	//Количество успешных визитов на лендинг
    //private long time;	//Среднее время пользователя на лендинге в секундах
    private int ct;	//Общее количество заказов без учёта треша
    private int mt;	//Общая сумма по лидам без учёта треша
    private int ca;	//Количество заказов в статусе «Принят»
    private int ma;	//Сумма по лидам в статусе «Принят»
    private int cc;	//Количество заказов в статусе «Отменён» без учёта треша
    // private int mc;	//Сумма по лидам в статусе «Отменён» без учёта треша
    private int cw;	//Количество заказов в статусе «Ожидает»
    private int mw;	//Сумма по лидам в статусе «Ожидает»
    //private int ch;	//Количество заказов в статусе «Холд»
    //private int mh;	//Сумма по лидам в статусе «Холд»
    //private int cx;	//Количество невалидных заказов (треш)
    //private int mx;	//Сумма по невалидным лидам (треш)
}
