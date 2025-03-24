package com.formadoreit.camel.aggregators;

import com.formadoreit.camel.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.Objects;

@Slf4j
public class OrderAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Order order = newExchange.getMessage().getBody(Order.class);
        log.info("Agregando impuestos de orden {} con valor {}", order.getId(), order.getTax());
        var orderTax = order.getTax();
        if(Objects.isNull(oldExchange)){
            newExchange.getMessage().setBody(orderTax);
        }else{
            var oldTax = oldExchange.getMessage().getBody(Double.class);
            newExchange.getMessage().setBody(orderTax + oldTax);
        }
        return newExchange;
    }
}
