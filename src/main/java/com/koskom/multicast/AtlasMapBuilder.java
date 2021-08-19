package com.koskom.multicast;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class AtlasMapBuilder extends RouteBuilder {

    public void configure() throws Exception {
        rest("api").get().route()
                .routeId(getContext().getName() + ".REQ")
                .startupOrder(11)
                .log(LoggingLevel.INFO, "start ${body}\n${headers}")
                .to("direct:bar")
        ;

        //Готовим шаблон на прием из топика, сейчас в 'from' стоит direct (для тестирования), нужно его заменить на кафку
        routeTemplate("inTemplate")
                .templateParameter("topic.in")
                .templateParameter("direct.out")
                .from("direct:{{topic.in}}")
                    /*//перебираем исходящие директы,
                    в случае если нам необходимо будет отправлять сообщение в несколько топиков*/
                    .setHeader("outs", simple("{{direct.out}}"))
                    .log(LoggingLevel.INFO, "${routeId} start ${body}\n${headers}")
                    .recipientList(simple("${header.outs}"), ",")
        ;

        //Шаблон для каждого топика, в который будем отправлять ответ
        routeTemplate("outTemplate")
                .templateParameter("direct.in")
                .templateParameter("atlasTemplateName")
                .templateParameter("topic.out")
                .from("{{direct.in}}")
                .log(LoggingLevel.INFO, "atlasMapTemplate: {{atlasTemplateName}} send to {{topic.out}}")
                /*тут добавляешь логику сервиса по обращению в common-atlasmap
                 после обработки ответа кидаешь в topic.out*/
        ;
    }
}
