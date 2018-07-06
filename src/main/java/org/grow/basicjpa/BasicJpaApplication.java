package org.grow.basicjpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Random;
/**
**
* xieweig notes: 此处可以开启一些jar包功能 标志是添加@EnableXXX标签。例如这里添加swagger2
 * 每一个Enable对应 .xml中的一个功能标签，例如<context component-scan/>是注解扫描的标签，springboot默认有开启扫描，
 * 范围是此主类的下级目录里的所有。所以我们建类都要在主类的目录下级
 * EnableXXX 还额外自动提供一些bean的实例化，
 * 譬如在当前工程，你可以在任意地方 @Resource or @Autowire private ApplicationContext c; 而c 不会为空；
*/
@EnableSwagger2
//@EnableWebMvc 这个不能乱开
@SpringBootApplication
public class BasicJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicJpaApplication.class, args);
    }
    /**
    **
    * xieweig notes: 主配置类，可以在这里做一系列配置，一个方法代表一个受spring管理的bean，可以在工程其他地方通过@Resource等注入。
     *              方法的返回值 == .xml中的class属性,方法的传入参数是其它springBean
    */
    @Bean(name = "random")
    public Random random(){
        return new Random();
    }
    /**
    **
    * xieweig notes: 最后我们建立三个文件夹/目录，位置与主类平级，这样目录里的所有类都在注解扫描范围内
     * 其中config做配置 补充主类的配置功能，之后其他的配置类可以都放在config中
     * domain 领域 行业 是实体信息包
     * infrastructure 基础设施 存放面向过程的封装的单例工具类，用来处理实体类，例如repository。
     * 额外，可以建立一个controller包和manager包作为调用工具类完成逻辑业务 ，这里由于示例，先只用一个controller
    */

    /**
    **
    * xieweig notes: swagger是锦上添花的，请放在最后阅读  重点只有设置basePackage，其他默认即可
    */
   @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.grow.basicjpa.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("Spring Boot中使用Swagger构建Rest Api version Member")
                .version("1.0").build();
    }

}
