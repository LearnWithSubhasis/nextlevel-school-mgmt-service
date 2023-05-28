//package org.nextlevel.security;
//
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("")
//                .allowedOrigins("")
//                .allowedHeaders("*")
////                .allowedHeaders("Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method",
////                        "Access-Control-Request-Headers")
//                .allowCredentials(true)
//                .maxAge(300);
//
//
////        registry.addMapping("/**")
////                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH", "HEAD")
////                //.allowedOrigins("")
////                .allowedOrigins("https://org.nextlevel.com/", "http://ip/",
////                        "http://localhost:3000/")
////                .allowedHeaders("Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method",
////                        "Access-Control-Request-Headers")
////                .allowCredentials(true)
////                .maxAge(300);
//
//    }
//}
