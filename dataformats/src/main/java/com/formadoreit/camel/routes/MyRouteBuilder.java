package com.formadoreit.camel.routes;

import com.formadoreit.camel.domain.Rol;
import com.formadoreit.camel.domain.User;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("timer:mainTimer?period=5s")
                .process(exchange -> {
                    var rol = Rol.builder()
                            .id(100)
                            .name("ADMIN")
                            .build();
                    var rol2 = Rol.builder()
                            .id(100)
                            .name("BASIC")
                            .build();
                    var user = User.builder()
                            .id(10)
                            .name("Luis")
                            .roles(List.of(rol, rol2))
                            .build();

                    exchange.getMessage().setBody(user);
                })
                .log("Body : ${body}")
                .to("direct:convertirAJson")
                .to("direct:convertirJsonAObject")
                .to("direct:convertirObjectAXml")
                .to("direct:convertirXmlAObject");

        from("direct:convertirAJson")
                .marshal().json()
                .log("Body JSON: ${body} de tipo ${body.class}")
                .split().jsonpath("$.roles[*]")
                    .log("Split body: ${body}")
                .end();

        from("direct:convertirJsonAObject")
                .unmarshal().json(User.class)
                .log("Body Object: ${body} de tipo ${body.class}");

        from("direct:convertirObjectAXml")
                .marshal().jaxb()
                .log("Body Object: ${body} de tipo ${body.class}")
                .split().xpath("//roles")
                    .log("Body Split XML ${body}")
                .end();

        from("direct:convertirXmlAObject")
                .log("Convirtiendo body a XML: ${body}")
                .unmarshal().jaxb(User.class.getPackageName())
                .log("Body final: ${body}");

    }

}
