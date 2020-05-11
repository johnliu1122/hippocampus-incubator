package com.liuapi.incubator.repository.transaction.service;

import com.liuapi.incubator.repository.transaction.mapper.LogMapper;
import com.liuapi.incubator.repository.transaction.model.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class LogService {
    @Resource
    private LogMapper logMapper;

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void insertLogWithRequireTransaction(Long uid, String op) {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void insertLogWithNewTransaction(Long uid, String op) {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED,rollbackFor = Exception.class)
    public void insertLogNotSupportTransaction(Long uid, String op) {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
    }
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    public void insertLogSupportTransaction(Long uid, String op) {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
    }

    @Transactional(propagation = Propagation.NESTED,rollbackFor = Exception.class)
    public void insertLogAfterSavepoint(Long uid, String op) throws Exception {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
        throw new Exception("occur exception in the nested transaction");
    }

    /**
     * 必须要有一个已存在的事务，否则报错
     * @param uid
     * @param op
     */
    @Transactional(propagation = Propagation.MANDATORY,rollbackFor = Exception.class)
    public void insertLogMustNeedExistingTransactionOrThrowException(Long uid, String op) {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
    }

    @Transactional(propagation = Propagation.NEVER,rollbackFor = Exception.class)
    public void insertLogNeverUseTransactionOrThrowException(Long uid, String op) {
        logMapper.addLog(new Log()
                .setUid(uid)
                .setValue(op)
        );
    }


}
