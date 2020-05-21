package com.liuapi.incubator.repository.scene.unique;

import com.liuapi.incubator.repository.scene.model.Privilege;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 *  insert基本用法
 *  insert into privilege (`id`, `privilege`, `code`) values (),(),(),();
 *  insert into privilege (`id`, `privilege`, `code`) select xx,xx,xx from yy;
 *  insert into privilege select xx from yy;
 *
 */
public interface PrivilegeInsertMapper {
    /**
     * 新纪录与旧记录重复，则忽略新记录
     * code被添加了唯一索引
     * @param privilege
     * @return 返回受影响的行数
     */
    @Insert("insert ignore into `privilege` (`id`, `privilege`, `code`, `version`, `gmt_create`,`gmt_modified`)" +
            " VALUES (NULL, #{p.privilege}, #{p.code}, 1, now(),null)")
    int insertIgnore(@Param("p") Privilege privilege);

    /**
     * 新纪录与旧记录重复，则更新旧记录
     * code被添加了唯一索引
     * @param privilege
     * @return 如果插入了一条新纪录则返回1，如果更新了原有的记录则返回2,如果更新的数据和已有的数据一模一样，则受影响的行数是0
     */
    @Insert("insert into `privilege` (`id`, `privilege`, `code`, `version`, `gmt_create`,`gmt_modified`)" +
            " VALUES (NULL, #{p.privilege}, #{p.code}, 1, now(),null)" +
            " on duplicate key update version=version+1,gmt_modified=now()")
    int insertOrUpdateVersion(@Param("p") Privilege privilege);

    /**
     * 新纪录与旧记录重复，则删除旧记录并插入新记录
     * code被添加了唯一索引
     * 当插入新记录发现之前已有记录，则replace into会删除旧记录再插入新记录
     * @param privilege
     * @return 返回受影响的行数（包括删除的行数与插入的行数）
     */
    @Insert("replace into `privilege` (`id`, `privilege`, `code`, `version`, `gmt_create`,`gmt_modified`)" +
            " VALUES (NULL, #{p.privilege}, #{p.code}, 1, now(),null)")
    int replace(@Param("p") Privilege privilege);
}
