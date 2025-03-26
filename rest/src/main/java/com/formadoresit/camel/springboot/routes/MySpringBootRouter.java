package com.formadoresit.camel.springboot.routes;

import com.formadoresit.camel.springboot.dto.ObjectItem;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:mainTimer?period={{timer.period}}")
                .routeId("main-timer")
                .autoStartup(false)
                .log("Procesando TIMER")
                .to("direct:crearObject");

        from("direct:getObjects")
                .log("Llamando API REST")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .to("https://api.restful-api.dev/objects")
                .unmarshal().json(ObjectItem[].class) // Indicar que tenemos una lista
                .log("Body tipo ${body.class}")
                .split(body())
                    .log("Object ${body} de tipo ${body.class}")
                .end();

        from("direct:crearObject")
                .log("creando objeto")
                .process(exchange -> {
                    var item = ObjectItem.builder()
                            .name("Objeto Ejemplo")
                            .data(Map.of("Key1", "Val1"))
                            .build();
                    exchange.getMessage().setBody(item);
                })
                .marshal().json()
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("https://api.restful-api.dev/objects?throwExceptionOnFailure=false")
                .choice()
                    .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo(200))
                        .unmarshal().json(ObjectItem.class)
                        .log("Exito, objeto creado: ${body}")
                    .otherwise()
                        .log(LoggingLevel.ERROR, "Respuesta API ERROR, ${body}")
                .end();
    }

}
