package com.liuapi.incubator.repository.scene.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Privilege {
    private Long id;
    private String privilege;
    private String code;
    private int version;
    private long gmtCreate;
    private long gmtModified;
}
