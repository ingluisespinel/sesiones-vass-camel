package com.formadoreit.camel.routes;

import org.apache.camel.builder.RouteBuilder;

public class AsyncRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:tercerTimer?period=1s")
                .autoStartup(false)
                .log("Disparando 3 timer")
                .setBody(constant("Hola,Mundo,Camel,Fin"))
                .split(body().tokenize(","))
                    .to("seda:rutaA")
                .end()
                //.to("direct:rutaB")
                .log("Finalizando flujo");

        from("seda:rutaA?concurrentConsumers=20")
                .log("Procesando ruta A ${body}")
                .process(exchange -> {
                    Thread.sleep(60000);
                    log.info("Finalizando A");
                });

        from("direct:rutaB")
                .log("Procesando ruta B")
                .process(exchange -> {
                    Thread.sleep(5000);
                    log.info("Finalizando B");
                });
    }
}
