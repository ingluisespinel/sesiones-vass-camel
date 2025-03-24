package com.formadoreit.camel;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main(MainApp.class);
        // Esto registra manualmente las rutas de integración
        // main.configure().addRoutesBuilder(new MyRouteBuilder());
        main.run(args);
    }

}

