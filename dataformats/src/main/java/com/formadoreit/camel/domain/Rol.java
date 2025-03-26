package com.formadoreit.camel.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "rol")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rol {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;
}
