package com.formadoresit.camel.springboot.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ObjectItem {
    private String id;
    private String name;
    private Map<String, Object> data;
    private String createdAt;
}
