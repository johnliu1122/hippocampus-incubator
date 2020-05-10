package com.liuapi.incubator.repository.transaction.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserOpLog {
    private Long id;
    private Long uid;
    private String op;
    private long createdAt;
}
