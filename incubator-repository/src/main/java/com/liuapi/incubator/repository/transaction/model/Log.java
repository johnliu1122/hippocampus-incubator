package com.liuapi.incubator.repository.transaction.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Log {
    private Long id;
    private Long uid;
    private String value;
    private long createdAt;
}
