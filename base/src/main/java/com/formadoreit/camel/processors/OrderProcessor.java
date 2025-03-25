package com.formadoreit.camel.processors;

import com.formadoreit.camel.domain.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.UUID;

public class OrderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        var order1 = Order.builder()
                .id(UUID.randomUUID().toString())
                .amount(10000d)
                .tax(0d)
                .paymentMethod("Paypal")
                .build();

        var order2 = Order.builder()
                .id(UUID.randomUUID().toString())
                .amount(1200d)
                .tax(0d)
                .paymentMethod("Credito")
                .build();

        var order3 = Order.builder()
                .id(UUID.randomUUID().toString())
                .amount(50d)
                .tax(0d)
                .paymentMethod("OTRO METODO")
                .build();
        var orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        exchange.getMessage().setHeader("HeaderUno", "Mi Header");
        exchange.getMessage().setBody(orders);
    }
}
