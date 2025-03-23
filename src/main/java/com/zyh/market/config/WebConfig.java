package com.zyh.market.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.zyh.market.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SecurityProperties securityProps;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("http://zjutyycs.com","http://zjutyycs.com:8200","http://124.222.121.87:8200", "http://124.222.121.87:8201","http://localhost:8080")
          .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*") // 允许的请求头
          .maxAge(3600)
          .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加需要放行的接口路径
        List<String> excludePaths = Arrays.asList(
                "/product/info/page",
                "/product/info/detail",
                "/product/type"
        );

        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePaths)
                .excludePathPatterns(securityProps.getWhiteList());
    }
}
