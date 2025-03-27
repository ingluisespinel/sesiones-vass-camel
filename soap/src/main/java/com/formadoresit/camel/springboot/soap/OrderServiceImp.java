package com.formadoresit.camel.springboot.soap;

import org.springframework.stereotype.Component;

@Component
public class OrderServiceImp implements OrderService{
    @Override
    public String hola(String name) {
        return "Hola " + name;
    }
}
