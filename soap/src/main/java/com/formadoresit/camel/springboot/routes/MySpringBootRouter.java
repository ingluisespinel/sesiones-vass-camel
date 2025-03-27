package com.formadoresit.camel.springboot.routes;

import org.apache.camel.Exchange;
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
        from("cxf:bean:cxfEndpoint")
                .log("Procesando requests con ${headers}")
                .setHeader(Exchange.BEAN_METHOD_NAME, simple("${header.operationName}"))
                .bean("orderServiceImp");
    }

}
