package org.nextlevel;
//
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import springfox.documentation.builders.PathSelectors;
////import springfox.documentation.builders.RequestHandlerSelectors;
////import springfox.documentation.service.ApiInfo;
////import springfox.documentation.service.Contact;
////import springfox.documentation.spi.DocumentationType;
////import springfox.documentation.spring.web.plugins.Docket;
////
////import java.util.Collections;
////
////@Configuration
////public class SwaggerConfig {
////
////    private ApiInfo apiInfo() {
////        return new ApiInfo("NextLevel School Mgmt Rest APIs",
////                "APIs for NextLevel School Mgmt App.",
////                "1.0",
////                "Terms of service",
////                new Contact("NextLevel", "www.nextlevel.org", "schoolmgmt@nextlevel.org"),
////                "License of API",
////                "API license URL",
////                Collections.emptyList());
////    }
////
////    @Bean
////    public Docket api() {
////        return new Docket(DocumentationType.OAS_30)
////                .apiInfo(apiInfo())
////                .select()
////                .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.any())
////                .build();
////    }
////}
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//public class SwaggerConfiguration {
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//
//    private ApiInfo apiInfo() {
//        return new ApiInfo("MyApp Rest APIs",
//                "APIs for MyApp.",
//                "1.0",
//                "Terms of service",
//                new Contact("test", "www.org.com", "test@emaildomain.com"),
//                "License of API",
//                "API license URL",
//                Collections.emptyList());
//    }
//
//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(apiKey()))
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiKey apiKey() {
//        return new ApiKey(AUTHORIZATION_HEADER, "JWT", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .build();
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope
//                = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
//    }
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("org.nextlevel"))
                //.paths(PathSelectors.ant("/api/v1/*"))
                .paths(PathSelectors.any())
                .build();
    }

}