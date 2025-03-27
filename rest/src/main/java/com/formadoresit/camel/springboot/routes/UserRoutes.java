package com.formadoresit.camel.springboot.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlConstants;
import org.springframework.stereotype.Component;

@Component
public class UserRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:insertUser")
                .log("Insertando usuario")
                .setHeader(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, constant(true))
                .to("sql:INSERT INTO usuario(name, age) VALUES(:#${body.name}, :#${body.age})")
                .log("Body ${body} de tipo ${body.class} ${headers}")
                .setBody(simple("${header.CamelSqlGeneratedKeyRows[0]['GENERATED_KEY']}"));

        from("direct:getUsersByAge")
                .log("Buscando usuarios por edad ${header.age}")
                .to("sql:SELECT * FROM usuario WHERE age = :#age?outputClass=com.formadoresit.camel.springboot.domain.User")
                .log("Body ${body} de tipo ${body[0].class} ${headers}");

        from("direct:getUsersByAgeRange")
                .log("Buscando usuarios por rango de edad ${header.fromAge} y ${header.toAge}")
                .to("sql:classpath:queries/queryAgeRange.sql");

        from("direct:getAllUsersWithJPA")
                .log("Obteniendo todos los usuarios")
                .to("jpa://com.formadoresit.camel.springboot.domain.UserEntity?query=SELECT u FROM UserEntity u");
    }
}
