package org.grow.basicjpa.domain;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * Author : xieweig
 * Date : 18-7-5
 * <p>
 * Description:此类将嵌入到包含它的实体类中，将会把属性平铺到表中，此处表示业务逻辑的状态属性，通常会由一些自定义Enum状态或boolean组成。
 */
@Data
@Embeddable
public class BusinessLogic {

    private Boolean isFrozen = false;

    private SubmitContent submitContent = new SubmitContent();

}
