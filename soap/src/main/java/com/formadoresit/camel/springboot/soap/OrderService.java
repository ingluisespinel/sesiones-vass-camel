package com.formadoresit.camel.springboot.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface OrderService {

    @WebMethod
    String hola(String name);

}
