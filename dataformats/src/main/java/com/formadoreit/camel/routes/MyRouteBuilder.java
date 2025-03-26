package com.formadoreit.camel.routes;

import com.formadoreit.camel.domain.Rol;
import com.formadoreit.camel.domain.User;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;

import java.util.List;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {
    private final CsvDataFormat csvDataFormat;

    public MyRouteBuilder(){
        csvDataFormat = new CsvDataFormat();
        csvDataFormat.setUseMaps(true);
        csvDataFormat.setSkipHeaderRecord(true);
    }

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("file:files?moveFailed=.errors&move=.procesados")
                .to("direct:procesarArchivos");


        from("sftp:{{app.sftp.uri}}&move=.procesados&moveFailed=.fallidos")
                .to("direct:procesarArchivos");

        from("direct:procesarArchivos")
                .log("Procesando archivo ${headers}")
                .choice()
                    .when(header("CamelFileName").regex("^.*\\.json"))
                        .log("Procesando JSON")
                        .to("direct:convertirJsonAObject")
                    .when(header("CamelFileName").regex("^.*\\.xml"))
                        .log("Procesando XML")
                        .to("direct:convertirXmlAObject")
                    .when(header("CamelFileName").regex("^.*\\.csv"))
                        .log("Procesando CSV")
                        .to("direct:convertCSVToObject")
                    .otherwise()
                    .throwException(new RuntimeException("Archivo no soportado"))
                .end();

        from("direct:convertirAJson")
                .marshal().json()
                .log("Body JSON: ${body.toString} de tipo ${body.class}")
                .split().jsonpath("$.roles[*]")
                    .log("Split body: ${body}")
                .end();

        from("direct:convertirJsonAObject")
                .unmarshal().json(User.class)
                .log("Body Object: ${body.toString} de tipo ${body.class}");

        from("direct:convertirObjectAXml")
                .marshal().jaxb()
                .log("Body Object: ${body} de tipo ${body.class}")
                .split().xpath("//roles")
                    .log("Body Split XML ${body}")
                .end();

        from("direct:convertirXmlAObject")
                .log("Convirtiendo body XML a Objeto: ${body}")
                .unmarshal().jaxb(User.class.getPackageName())
                .log("Body final: ${body}")
                .process(exchange -> {
                    Object body = exchange.getMessage().getBody();
                    if(body instanceof User user){
                        log.info("Body si es de tipo User {}", user);
                    }else{
                        log.info("Body NO es de tipo User, es {}", body.getClass());
                    }
                });

        from("direct:convertCSVToObject")
                .log("Convirtiendo CSV")
                .unmarshal(csvDataFormat)
                .log("Body ${body} de tipo ${body.class}")
                .split(body())
                    .log("Procesado ${body['name']}");

    }

}
