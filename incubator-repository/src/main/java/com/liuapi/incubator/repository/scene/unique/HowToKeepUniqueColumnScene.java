package com.liuapi.incubator.repository.scene.unique;

import com.liuapi.incubator.repository.scene.model.Privilege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 如何保持字段的唯一不重复
 */
@Component
@Slf4j
public class HowToKeepUniqueColumnScene {
    @Resource
    private PrivilegeInsertMapper privilegeInsertMapper;
    /**
     *  给权限表的code字段添加了唯一索引
     *  alter table xx add unique uk_xx(xx)
     *
     *  同时插入新记录时需要考虑，如果插入重复记录的话，对新旧记录做如何取舍?
     *  1）直接忽略新记录 insert ignore
     *  2）用新纪录来更新旧记录 insert on duplicate key update
     *  3）删除旧记录，插入新记录 replace
     *  4）直接报错 insert
     */
    public void solutionAboutUniqueColumn(){
        /**
         * 1）直接忽略新记录
         */
        int affectedRows = privilegeInsertMapper.insertIgnore(new Privilege()
                .setPrivilege("超管")
                .setCode("super.admin")
        );
        /**
         * 2）用新纪录来更新旧记录
         */
        affectedRows = privilegeInsertMapper.insertOrUpdateVersion(new Privilege()
                .setPrivilege("超管")
                .setCode("super.admin")
        );
        /**
         * 3）删除旧记录，插入新记录
         */
        affectedRows = privilegeInsertMapper.replace(new Privilege()
                .setPrivilege("超管")
                .setCode("super.admin")
        );
    }

    /**
     *
     */
    public void sideEffectAboutNullValueOfUniqueColumn(){

    }
}
