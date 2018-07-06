package org.grow.basicjpa.domain;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

/**
 * Author : xieweig
 * Date : 18-7-5
 * <p>
 * Description:嵌入VO的好处是方便后期扩展，我们只需要更改VO 就好了
 */
@Data
@Embeddable
public class BusinessContent {
    /**
    **
    * xieweig notes: 不需要做配置 可以直接映射
    */
    private LocalDate birthday;
    /**
    **
    * xieweig notes: 此处要着重说明一点，数据库保存时候尽量不要用中文内容，但是有时候业务逻辑不可避免，
     *          那么要确定前端到后端尽量都保证utf-8编码，
     *          尤其是数据库创建的时候，mysql为例子,不能简单　create databases my_db;
     *          而是要　百度　“create databases utf-8”,查询到语句，其实最关键的就是指定数据库默认字符集
     *          那么该数据库下所有表都是utf-8
     *          CREATE DATABASE IF NOT EXISTS my_db default charset utf8 COLLATE utf8_general_ci;
     *          collate　utf8_general_ci 可能跟查询排序有关
    */
    private String memberName = "unknown";
    /**
    **
    * xieweig notes: enum类型映射到数据库要用特有注解
    */
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.java11;

    public enum Grade {
       os5, os6, java11
    }

    /**
    **
    * xieweig notes: 此处还可以做oneToMany映射等 实现关系表
    */
}
