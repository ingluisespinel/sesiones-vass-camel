package com.formadoreit.camel.routes;

import com.formadoreit.camel.aggregators.OrderAggregator;
import com.formadoreit.camel.domain.Order;
import com.formadoreit.camel.processors.ErrorHandler;
import com.formadoreit.camel.processors.OrderProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;

import java.util.UUID;

/**
 * A Camel Java DSL Router
 */
@Slf4j
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        errorHandler(deadLetterChannel("direct:errorHandler")
                .maximumRedeliveries(0));

        onException(IllegalStateException.class)
                .maximumRedeliveries(2)
                .redeliveryDelay(2000)
                .handled(true)
                .to("direct:errorHandler");

        from("timer:disparador?period={{app.timer-period}}")
                .routeId("disparador")
                .autoStartup(false)
                // Punto de procesamiento llamando a una clase Processor
                .process(new OrderProcessor())
                .split(body())
                    .to("direct:rutaUno")
                .end() // End para finalizar
                .to("direct:rutaDos");

        from("direct:rutaUno")
                .routeId("ruta-uno")
                // Punto de procesamiento llamando a una Lambda
                .process(exchange -> {
                    var body = exchange.getMessage().getBody(Order.class);
                    log.info("Body -> {}",body);
                    throw new RuntimeException("Algo pasó !");
                })
                .choice()
                    .when(simple("${body.amount} > 500"))
                        .log("Order ${body.id} es mayor a 500")
                        .to("direct:otroChoice")
                    .when(simple("${body.amount} >= 1200 && ${body.amount} < 2000"))
                        .log("Order ${body.id} monto entre 1200 y 2000")
                    .otherwise()
                        .log("Order ${body.id} es menor o igual a 500")
                .end();

        from("direct:rutaDos")
                .routeId("ruta-dos")
                .log("================================================")
                .log("Procesando desde ruta dos con Body: ${body}, Body Original ${header.BodyOriginal}")
                .setHeader("TotalOrders", simple("${body.size}"))
                .split().body()
                    .log("Fitrando orden ${body.amount}")
                    .filter(simple("${body.amount} > 1000"))
                        .to("direct:calcularImpuestos")
                    .end()
                    .log("Order ${body.id} finaliza procesamiento en ruta dos")
                .aggregate(constant(true), new OrderAggregator())
                .completionSize(simple("${header.TotalOrders}"))
                .log("Post Aggregation Body ${body}");

        from("direct:calcularImpuestos")
                .log("Order ${body.id} filtrada ")
                .doTry()
                    .setHeader("HeaderAdicional", constant("Hola"))
                    //.bean("taxCalculator", "calculate") Llamamos al Bean inyectando parámetros usando anotacionses propias de Camel
                    .bean("taxCalculator", "calculate( ${body} , ${header.HeaderAdicional} )")
                    .log("Impustos calculados ${body.tax}")
                .doCatch(IllegalArgumentException.class)
                    .log("Manejando Error IllegalArgumentException ${exception}")
                .doFinally()
                    .log("Ejecutando Finally");

        from("direct:otroChoice")
                .routeId("ruta-otro-choice")
                .log("Procesando en otro choice");

        from("direct:errorHandler")
                .log("Manejando error...")
                .process(new ErrorHandler())
                // Enviar mensajes a rutas definidas en Header Destinatarios
                .recipientList(header("Destinatarios"));

    }

}
