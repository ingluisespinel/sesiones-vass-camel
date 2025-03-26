package com.formadoreit.camel.domain;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
    @XmlElementWrapper(name = "roles") // Indica que es una lista dentro de un nodo
    @XmlElement(name = "rol")  // Nombre de cada elemento dentro de la lista
    private List<Rol> roles;


    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', roles=" + roles + "}";

    }

}
