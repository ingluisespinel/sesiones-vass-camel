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
                .amount(100d)
                .build();

        var order2 = Order.builder()
                .id(UUID.randomUUID().toString())
                .amount(1200d)
                .build();

        var order3 = Order.builder()
                .id(UUID.randomUUID().toString())
                .amount(50d)
                .build();
        var orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        exchange.getMessage().setHeader("HeaderUno", "Mi Header");
        exchange.getMessage().setBody(orders);
    }
}
