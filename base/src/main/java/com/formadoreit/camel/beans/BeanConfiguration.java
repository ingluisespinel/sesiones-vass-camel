package com.formadoreit.camel.beans;

import org.apache.camel.BindToRegistry;
import org.apache.camel.Configuration;
import org.apache.camel.PropertyInject;

@Configuration
public class BeanConfiguration {

    @BindToRegistry()
    public TaxCalculator taxCalculator(@PropertyInject("app.tax-value") Float taxValue){
        return new TaxCalculator(taxValue);
    }
}
