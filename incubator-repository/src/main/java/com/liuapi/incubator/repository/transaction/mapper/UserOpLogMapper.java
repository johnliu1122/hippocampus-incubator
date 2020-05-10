package com.liuapi.incubator.repository.transaction.mapper;

import com.liuapi.incubator.repository.transaction.model.UserOpLog;

public interface UserOpLogMapper {
    int addLog(UserOpLog userOpLog);
}
