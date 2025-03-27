package com.formadoresit.camel.springboot.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
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
                .log("Notificando")
                .throwException(new RuntimeException("No se pudo notificar"));

        from("direct:insertUserJPA")
                .to("jpa://com.formadoresit.camel.springboot.domain.UserEntity");
    }
}
