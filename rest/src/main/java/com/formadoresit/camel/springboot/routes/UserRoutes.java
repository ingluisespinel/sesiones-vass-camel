package com.formadoresit.camel.springboot.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        interceptFrom("activemq:queue:newUsers")
                .log("Nuevo usuario interceptado ${body}")
                .to("micrometer:counter:newUsers");

        from("direct:getUsersByAge")
                .log("Consultando por edad ${header.age}")
                .to("sql:SELECT * FROM usuario WHERE age = :#age");

        from("direct:getUsersByAgeRange")
                .log("Consultando por edad ${header.fromAge} to ${header.toAge}")
                .to("sql:classpath:queries/queryByAgeRange.sql?outputClass=com.formadoresit.camel.springboot.components.User")
                .log("Body ${body.class}");

        from("direct:getUsersWithJPA")
                .to("jpa://com.formadoresit.camel.springboot.domain.UserEntity?query=SELECT u FROM UserEntity u");

        from("direct:procesarNuevoUsuario")
                .doTry()
                    .to("bean-validator:validacionUsuario")
                    .to("direct:crearUsuarioTrx")
                .endDoTry()
                .doCatch(BeanValidationException.class)
                    .log("Controlando Exception de validacion de datos, body: ${body}")
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                    .process(exchange -> {
                        var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BeanValidationException.class);
                        //log.info("exception {}", exception);
                        log.info("Exception class {}", exception.getClass());
                        Map<String, String> responseBody =new HashMap<>();
                        responseBody.put("message", exception.getMessage());
                        exchange.getMessage().setBody(responseBody);
                    })
                .end();

        from("direct:crearUsuarioTrx")
                .transacted()
                    .to("direct:insertUserJPA")
                    .to("direct:notificarNuevoUsuario")
                .end();

        from("direct:notificarNuevoUsuario")
                .log("Notificando")
                .to("activemq:queue:newUsers?disableReplyTo=true");
                //.throwException(new RuntimeException("No se pudo notificar"));

        from("activemq:queue:newUsers")
                .log("Nuevo Usuario Notificado ${body} de tipo ${body.class}");

        from("direct:insertUserJPA")
                .to("jpa://com.formadoresit.camel.springboot.domain.UserEntity");
    }
}
