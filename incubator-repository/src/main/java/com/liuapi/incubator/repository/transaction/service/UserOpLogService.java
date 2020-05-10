package com.liuapi.incubator.repository.transaction.service;

import com.liuapi.incubator.repository.transaction.mapper.UserOpLogMapper;
import com.liuapi.incubator.repository.transaction.model.UserOpLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserOpLogService {
    @Resource
    private UserOpLogMapper userOpLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertLogAndCreateNewTransaction(Long uid, String op) {
        userOpLogMapper.addLog(new UserOpLog()
                .setUid(uid)
                .setOp(op)
        );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertLogAndIncludedIntoExistingTransaction(Long uid, String op) {
        userOpLogMapper.addLog(new UserOpLog()
                .setUid(uid)
                .setOp(op)
        );
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void insertLogWithoutTransaction(Long uid, String op) {
        userOpLogMapper.addLog(new UserOpLog()
                .setUid(uid)
                .setOp(op)
        );
    }

}
