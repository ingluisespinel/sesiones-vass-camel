package com.formadoreit.camel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;

@Slf4j
public class OtroRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:timer2?period=10s")
                .autoStartup(false)
                .log("Timer 2 disparado")
                .wireTap("direct:enviarCorreo")
                .log("Llamando ruta Y")
                .setBody(constant("Hola,Mundo"))
                .multicast().parallelProcessing()
                    .to("direct:rutaZ", "direct:rutaY")
                .end();

        from("direct:enviarCorreo")
                .log("Procesando ruta enviarCorreo")
                .process(exchange -> {
                    Thread.sleep(5000);
                    log.info("Finalizando ruta enviarCorreo");
                });

        from("direct:rutaY")
                .log("Procesando ruta Y con Body ${body}")
                .split(body().tokenize(",")).parallelProcessing()
                    .process(exchange -> {
                        Thread.sleep(3000);
                        var body = exchange.getMessage().getBody(String.class);
                        log.info("Finalizando rutaY con body {}", body);
                    })
                .end();

        from("direct:rutaZ")
                .log("Procesando ruta Z");

    }
}
