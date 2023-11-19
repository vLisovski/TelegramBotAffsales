package com.lisovski.tgspringbot.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateFlowRequest {
    private int flow;//ID потока для изменения
    private String name;//Новое название потока
   // private String site;//Идентификатор лендинга
   // private String space;//Идентификатор прелендинга (ноль - не требуется)
    //private String drt;//Идентификатор паркованного домена для перенаправления (ноль - не требуется)
    //private String dst;//Идентификатор паркованного домена лендингов (ноль - не требуется)
    //private String dsp;//Идентификатор паркованного домена прелендингов (ноль - не требуется)
    //private String url;//Ссылка трафбека, куда перенаправляются пользователи, не подходящие по ГЕО
   // private String pbu;//Ссылка для отправки PostBack-запросов
   // private String mtrk;//Идентификатор счётчика Яндекс.Метрика
   // private String ga;//Идентификатор счётчика Google Tag Manager
   // private String vk;//Идентификатор пикселя VKcom
   // private String fb;//Идентификатор пикселя Facebook
    private String utms;//UTM-метка utm_source
    private String utmc;//UTM-метка utm_campaign
    private String utmn;//UTM-метка utm_content
    private String utmt;//UTM-метка utm_term
    private String utmm;//UTM-метка utm_medium
}
