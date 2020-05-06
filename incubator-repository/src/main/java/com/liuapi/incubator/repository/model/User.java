package com.liuapi.incubator.repository.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String username;
    private String address;
}
