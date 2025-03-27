package com.formadoresit.camel.springboot;

import com.formadoresit.camel.springboot.soap.Hola;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that triggers from a timer and calls a bean and prints to system out.
 * <p/>
 * Use <tt>@Component</tt> to make Camel auto detect this route when starting.
 */
@Component
public class MySpringBootRouter extends RouteBuilder {

    @Override
    public void configure() {
        from("timer:hello?period={{timer.period}}").routeId("hello")
                .log("Llamando servicio SOAP")
                .process(exchange -> {
                    exchange.getMessage().setBody("John Doe");
                })
                .to("cxf://http://127.0.0.1:8080/services/ws/soap?serviceClass=com.formadoresit.camel.springboot.soap.OrderService&wsdlURL=serviceSoap.wsdl")
                .log("Body response '${body}'");
    }

}
