package com.formadoresit.camel.springboot.routes;

import com.formadoresit.camel.springboot.components.User;
import com.formadoresit.camel.springboot.domain.UserEntity;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
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
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES")
                .clientRequestValidation(true);

        rest("/users")
                .get()
                    .produces("application/json")
                    .outType(User[].class)
                    .to("direct:getUsersWithJPA")
                .get("/{userId}")
                    .produces("application/json")
                    .outType(User.class)
                    .to("direct:processGetUserById")
                .get("/filters/by-age/{age}")
                    .produces("application/json")
                    .outType(User.class)
                    .to("direct:getUsersByAge")
                .get("/filters/by-age-range")
                    .param().name("fromAge").type(RestParamType.query).required(true).endParam()
                    .param().name("toAge").type(RestParamType.query).required(true).endParam()
                    .produces("application/json")
                    .outType(User.class)
                    .to("direct:getUsersByAgeRange")
                .post()
                    .produces("application/json")
                    .type(UserEntity.class)
                    .outType(UserEntity.class)
                    .to("direct:procesarNuevoUsuario");

        rest("processors")
                .post("proxy")
                    .to("direct:processProxy");

        from("direct:processProxy")
                .log("Ejecutando Proxy: ${body}")
                .setHeader("attribute_to_save", simple("${body['attribute_to_save']}"))
                .log("Nuevos headers ${headers}")
                .setHeader(Exchange.HTTP_METHOD, simple("${body['method']}"))
                //.marshal().json()
                //.log("Body para API ${body}")
                .toD("${body['url']}?bridgeEndpoint=true")
                .unmarshal().json()
                .log("Body response ${body}")
                .log("Nuevos headers ${headers}")
                .filter(simple("${header.attribute_to_save} != null"))
                    .setBody(simple("${body[ ${header.attribute_to_save} ]}"))
                .end()
                .to("direct:saveInMongo");

        from("direct:saveInMongo")
                .to("mongodb:mongodb?database=test&collection=data&operation=insert");

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
