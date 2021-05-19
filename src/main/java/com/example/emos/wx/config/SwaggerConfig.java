package com.example.emos.wx.config;

import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 齐春晖
 * @date Created in 9:19 2021/5/5
 * @description
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        Docket docket =new Docket(DocumentationType.SWAGGER_2);
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("EMOS在线办公系统");
        ApiInfo build = apiInfoBuilder.build();
        docket.apiInfo(build);

        // ApiSelectorBuilder 用来设置哪些类中的方法会生成到REST API中
        ApiSelectorBuilder selectorBuilder = docket.select();
        selectorBuilder.paths(PathSelectors.any()); //表示项目路径下所有的包下
        selectorBuilder.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));//使用@ApiOperation注解

        /*
     * 下面的语句是开启对JWT的支持，当用户用Swagger调用受JWT认证保护的方法，
     * 必须要先提交参数（例如令牌）
     */
        List<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("token","token","header"));//从请求头中获取token
        docket.securitySchemes(apiKeys);
        //如果用户JWT认证通过，则在Swagger中全局有效
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] scopes = {authorizationScope};
        //存储令牌和作用域
        SecurityReference reference = new SecurityReference("token",scopes);
        List<SecurityReference> references =new ArrayList<>();
        references.add(reference);
        SecurityContext context = SecurityContext.builder().securityReferences(references).build();
        List<SecurityContext> cxList = new ArrayList();
        cxList.add(context);
        docket.securityContexts(cxList);

        return docket;
    }

}
