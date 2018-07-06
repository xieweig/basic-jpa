package org.grow.basicjpa.domain;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Author : xieweig
 * Date : 18-7-5
 * <p>
 * Description:
 */
@Data
@Embeddable
public class SubmitContent {

    /**
    **
    * xieweig notes: 每一次状态变化 都会加入一些注释，通过embeddable注解，实现了树形的Entity 映射成为 平面化的关系型数据库表结构
     * 方便前段直接绑定一个小对象VO，例如此类就会在controller修改操作时候直接绑定
    */
    private String remarks;
    @Enumerated(EnumType.STRING)
    private SubmitState submitState = SubmitState.not_save;

    public enum SubmitState{
        not_save ,just_save, submit, audit_success, audit_failure
    }


}
