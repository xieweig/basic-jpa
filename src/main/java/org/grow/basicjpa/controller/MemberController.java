package org.grow.basicjpa.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.grow.basicjpa.domain.BusinessContent;
import org.grow.basicjpa.domain.Member;
import org.grow.basicjpa.domain.SubmitContent;
import org.grow.basicjpa.infrastructure.JpaMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Author : xieweig
 * Date : 18-7-5
 * <p>
 * Description:
 */
@CrossOrigin
@RequestMapping(value = "/member")
@RestController
public class MemberController {
    @Resource
    private JpaMember jpaMember;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public Member save(@RequestBody Member member) {
        member.setMemberCode(UUID.randomUUID().toString().substring(20));

        return jpaMember.save(member);
    }

    /**
     * *
     * xieweig notes: 更新主要分为两类，一类是对于传入数据信任，requestBody直接保存，另一类只允许修改特别
     * 字段，传入字段数量很少,先find 后set最后save ,其实这是http Patch请求，可以先略过不看,　当成post 处理
     */
    @RequestMapping(value = "/{memberCode}", method = RequestMethod.POST)
    public Member submit(@PathVariable String memberCode, @RequestBody SubmitContent submitContent) {

        /**
         **
         * xieweig notes: 请熟练使用 if return 或者　if throw　这种逻辑和套路　很常用。
         */
        if (StringUtils.isEmpty(memberCode) || submitContent == null)
            throw new RuntimeException(this.getClass().getSimpleName());

        Member member = jpaMember.findByMemberCode(memberCode);
        /**
         **
         * xieweig notes: post 请求概念上是要求如果find不到，那么就报错，不会新建
         */
        if (member == null)
            throw new RuntimeException(this.getClass().getSimpleName() + member);
        member.getBusinessLogic().setSubmitContent(submitContent);

        return jpaMember.save(member);

    }

    @RequestMapping(value = "/{memberCode}", method = RequestMethod.GET)
    public Member find(@PathVariable String memberCode) {
        return jpaMember.findByMemberCode(memberCode);
    }

    @RequestMapping(value = "/findAll/{pageNumber}", method = RequestMethod.GET)
    public List<Member> findAll(@PathVariable int pageNumber) {
        /**
         **
         * xieweig notes: 一页10行，根据memberCode排序，这些参数页可从前段获取
         *              顺便请熟悉page对象的几个要素，通常我们看第一页，实际上是第0页
         */

        Pageable pageable = PageRequest.of
                (pageNumber - 1, 10, Sort.by
                        (Sort.Direction.ASC, "memberCode")
                );
        return jpaMember.findAll(pageable).getContent();
    }

    @Transactional
    @RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
    public void deleteAll() {
        jpaMember.deleteAll();
    }

    @Transactional
    @RequestMapping(value = "/{memberCode}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String memberCode) {
        jpaMember.deleteByMemberCode(memberCode);
    }

    @RequestMapping(value = "/findByDynamicMemberName", method = RequestMethod.POST)
    public Page<Member> findByDynamicExample(@RequestBody BusinessContent businessContent) {
        /**
         **
         * xieweig notes: 动态查询主要就是准备两个对象　一个是pagable, 包含几个数字，一个是specification,　包含查询的条件
         */
        Pageable pageable = PageRequest
                .of(0, 10, Sort
                        .by(Sort.Direction.ASC, "memberCode"));
        /**
         **
         * xieweig notes: 先看一个简单的示例　制作一个ｓｐｅ
         */
        Specification<Member> spe = new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //new Member().getBusinessContent().getMemberName() 对比下面一行，勿省略了嵌套而出错
                criteriaQuery.where(cb.like(root.get("businessContent").get("memberName").as(String.class),
                        "%" + businessContent.getMemberName() + "%"));
                /**
                 **
                 * xieweig notes: 翻译成ｓｑｌ语句为　
                 * select * from member where member.memberName like：?1 ;其中?1＝businessContent.getMemberName()
                 * Query 主动ｗｈｅｒｅ之后不需要返回值，正常不是这样使用的，而是如下面例子，这个例子仅帮助大家熟悉root ,query, cb
                 * ｒｏｏｔ　是代表聚合表，如果没有一对多等表关联，就是仅代表那张表，此例子就是ｍｅｍｂｅｒ类，可以ｇｅｔ到相应字段。
                 * ｑｕｅｒｙ是ｊｐａ　的查询类引用，可以执行ｗｈｅｒｅ查询
                 *　ｃｂ里面包含了各种谓词，条件，断言，有点类似junit里面的hamcrest匹配器Matchers, 有　与或非逻辑　有数字比较　有字符串匹配
                 * 对应的ｓｑｌ关键字 and or > < like％％ 等
                 */


                return null;
            }
        };
        return jpaMember.findAll(spe, pageable);

    }

    @RequestMapping(value = "/findByDynamic", method = RequestMethod.POST)
    public Page<Member> findByDynamicNormal(@RequestBody BusinessContent businessContent) {
        /**
        **
        * xieweig notes: 为了方便用户，通常会选择有一个最近访问时间字段作为一个排序依据，本表没有设置最近访问时间，用生日替代
        */
        Pageable pageable = PageRequest
                .of(0, 10, Sort
                        .by(Sort.Direction.ASC, "birthday"));
        /**
        **
        * xieweig notes: 基本形式，熟练后可以直接用ｌａｍｂｄａ表达式，
         * ｌａｍｂｄａ表达式为匿名函数，缺点是不能复用，优点是代码简单，思路清晰，所谓面向函数的编程
        */
        return jpaMember.findAll((root, query, cb) -> {
            /**
            **
            * xieweig notes: 第一步，传销三连，无脑使用就好，最终获取一个ｅｘｐｒｅｓｓｉｏｎｓ容器，我们根据业务，把动态查询内容放进去即可
            */
            query.distinct(true);
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            //expressions.add()
            /**
            **
            * xieweig notes: 但是在添加时候要判断是否为空，为空不加入查询条件中
            */
            if (!StringUtils.isEmpty(businessContent.getMemberName())){
                expressions.add(cb.like(root.get("businessContent").get("memberName").as(String.class), businessContent.getMemberName()));
            }
                /**
                **
                * xieweig notes: 实际业务不会是这样判断时间　而是会有起始时间　和　终止时间　因此要求　绑定一个专门的查询条件对象，跟实体类类似　单不完全一样
                */
            if (!StringUtils.isEmpty(businessContent.getBirthday())){
                expressions.add(cb.equal(root.get("businessContent").get("birthday").as(LocalDate.class), businessContent.getBirthday()));
            }

            if (!StringUtils.isEmpty(businessContent.getGrade())){
                expressions.add(cb.equal(root.get("businessContent").get("grade").as(BusinessContent.Grade.class), businessContent.getGrade()));
            }

            return predicate;
        }, pageable);

    }
}