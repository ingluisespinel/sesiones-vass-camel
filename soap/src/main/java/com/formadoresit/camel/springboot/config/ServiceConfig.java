package com.formadoresit.camel.springboot.config;


import com.formadoresit.camel.springboot.soap.OrderService;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CxfEndpoint cxfEndpoint() {
        final CxfEndpoint result = new CxfEndpoint();
        result.setServiceClass(OrderService.class);
        result.setDataFormat(DataFormat.POJO);
        result.setMtomEnabled(true);
        result.setAddress("/ws/soap");
        return result;
    }
}
