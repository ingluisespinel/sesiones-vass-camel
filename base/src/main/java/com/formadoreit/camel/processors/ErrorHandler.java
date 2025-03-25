package com.formadoreit.camel.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class ErrorHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.info("Exception type {} con Mensaje {}", exception.getClass(), exception.getMessage());
        exchange.getMessage().setHeader("Destinatarios", "direct:rutaX,direct:rutaY");
    }
}
