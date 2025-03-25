package com.formadoreit.camel.routes;

import com.formadoreit.camel.processors.OrderProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public class CasosRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("timer:timer4?period=5s")
                .log("Timer 4 disparado")
                .to("direct:notificaciones")
                .to("direct:correos")
                .process(new OrderProcessor())
                .split(body())
                    .choice()
                        .when(simple("${body.paymentMethod} == 'Paypal'"))
                            .to("direct:procesarPaypal")
                        .when(simple("${body.paymentMethod} == 'Credito'"))
                            .to("direct:procesarCredito")
                        .otherwise()
                            .log(LoggingLevel.ERROR, "Metodo de pago desconocido")
                    .end()
                .end();

        from("direct:procesarPaypal")
                .log("Procesando Paypal");

        from("direct:procesarCredito")
                .log("Procesando Credito");

        from("direct:correos")
                .log("Procesando Corre")
                .setBody(constant("Esto es un correo"))
                .filter(body().contains("URGENTE"))
                    .log("Correo urgente, procesando...")
                .end();

        from("direct:notificaciones")
                .setBody(constant("sms,email"))
                .process(exchange -> {
                    var body = exchange.getMessage().getBody(String.class);
                    var canales = body.split(",");
                    var destinos = "";
                    for(var canal : canales){
                        destinos = destinos + "direct:"+canal+",";
                    }
                    exchange.getMessage().setHeader("Destinos", destinos);
                })
                .log("Enviando notificaciones a ${header.Destinos}")
                .recipientList(header("Destinos"));

        from("direct:sms")
                .log("enviando SMS");

        from("direct:email")
                .log("enviando Email");

    }
}
