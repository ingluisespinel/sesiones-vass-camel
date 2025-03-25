package com.formadoreit.camel.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class Order {
    private String id;
    private Double amount;
    private Double tax;
    private String paymentMethod;

}
