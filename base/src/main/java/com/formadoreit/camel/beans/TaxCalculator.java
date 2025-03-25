package com.formadoreit.camel.beans;

import com.formadoreit.camel.domain.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaxCalculator {
    private Float tax;

    public TaxCalculator(Float tax){
        this.tax = tax;
    }

    // Por default Camel pasa el Body como primer parámetro
    public Order calculate(Order order, String headerAdicional){
        log.info("Calculando Tax para orden de monto {}, headerAdicional {}", order.getAmount(), headerAdicional);
        order.setTax(order.getAmount() * tax);
        throw new IllegalStateException("No se pudo calcular impuestos");
        //return order;
    }
}
