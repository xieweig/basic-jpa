package org.grow.basicjpa.infrastructure;

import org.grow.basicjpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author : xieweig
 * Date : 18-7-5
 * <p>
 * Description:
 */
@Repository
public interface JpaMember extends JpaRepository<Member, Long> ,JpaSpecificationExecutor<Member> {

    /**
    **
    * xieweig notes: 添加几个主键查询方法，findBy开始 有提示的
    */
    Member findByMemberCode(String memberCode);

    List<Member> findAllByMemberCodeIn(List<String> memberCodes);

    void deleteByMemberCode(String memberCode);

    /**
    **
    * xieweig notes: 动态查询有jpa specification
    */
}
