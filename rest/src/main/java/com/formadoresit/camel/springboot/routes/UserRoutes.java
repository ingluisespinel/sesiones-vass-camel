package com.formadoresit.camel.springboot.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        interceptFrom("activemq:queue:newUsers")
                .log("Nuevo mensaje interceptado")
                .to("micrometer:counter:newUsers?increment=1");

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
                .transacted()
                    .to("direct:insertUserJPA")
                    .to("direct:notificarNuevoUsuario")
                .end();

        from("direct:notificarNuevoUsuario")
                .routeId("notificar-usuario")
                .log("Notificando via Activemq ${body}")
                .to("activemq:queue:newUsers?disableReplyTo=true")
                .throwException(new RuntimeException("Error !"))
                .log("Mensaje entregado.");

        from("direct:insertUserJPA")
                .to("jpa://com.formadoresit.camel.springboot.domain.UserEntity");

        from("activemq:queue:newUsers")
                .log("Consumiendo mensaje newUsers ${body} de tipo ${body.class}");
    }
}
