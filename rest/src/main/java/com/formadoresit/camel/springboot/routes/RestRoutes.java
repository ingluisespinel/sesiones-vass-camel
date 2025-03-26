package com.formadoresit.camel.springboot.routes;

import com.formadoresit.camel.springboot.components.User;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .contextPath("/api/v1/camel")
                .port(8080)
                .host("0.0.0.0")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/users")
                .get()
                    .produces("application/json")
                    .outType(User[].class)
                    .to("direct:getAllUsers")
                .get("/{userId}")
                    .produces("application/json")
                    .outType(User.class)
                    .to("direct:processGetUserById")
                .post()
                    .produces("application/json")
                    .type(User.class)
                    .outType(User.class)
                    .to("direct:createUser");

        from("direct:getAllUsers")
                .log("Procesando getAllUsers")
                .bean("fakeUserRepository", "getUsers");

        from("direct:processGetUserById")
                .to("direct:getUserById")
                .filter(body().isNull())
                        .log("No se encontró usuario")
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .end();

        from("direct:getUserById")
                .log("Buscando usuario id ${header.userId}")
                .bean("fakeUserRepository", "getUserById( ${header.userId} )");

        from("direct:createUser")
                .log("Creando usuario")
                .bean("fakeUserRepository", "save");
    }
}
