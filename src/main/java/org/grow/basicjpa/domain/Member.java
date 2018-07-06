package org.grow.basicjpa.domain;

import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Author : xieweig
 * Date : 18-7-5
 * <p>
 * Description:
 */
@Component
@Data
@Entity
/**
**
* xieweig notes: 此注解是非单例bean交给spring管理，暂时不用理会，我的domain模板会默认添加这四个注解，
 * Entity是jpa映射，Data是Lombok添加 set get 方法，Component是交给spring管理
*/
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Member {
    /**
    **
    * xieweig notes: 这些字段起名要认真，因为前段同事也需要使用
    */
    @Id@GeneratedValue
    private Long id;
    /**
    **
    * xieweig notes: 业务主键，通常不推荐直接用id作为增删改查的主键。也称为业务流水号，UUID
    */
    @Column(updatable = false, nullable = false, unique = true)
    private String memberCode;
    /**
    **
    * xieweig notes: 为了避免空指针异常或者操作方便，对于实体类，契合ＵＭＬ组合关系　同生共死的理念，最好直接ｎｅｗ出；
    */
    /**
    **
    * xieweig notes: 前段基本的录入信息，需要持久化记录的静态信息
    */
    private BusinessContent businessContent = new BusinessContent();
   /**
   **
   * xieweig notes: 后端自动生成的或与前端行为相关的属性，例如某单据审核后盖章（ｓｕｂｍｉｔ＝》ａｕｄｉｔｅｄ　状态改变）还有审核备注信息
    *   往往是单据的动态信息，单据流转的流水线上有不同的操作人对他进行操作。
   */
    private BusinessLogic businessLogic = new BusinessLogic();


}
