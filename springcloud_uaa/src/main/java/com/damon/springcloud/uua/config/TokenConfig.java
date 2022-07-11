package com.damon.springcloud.uua.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/** 1、定义TokenConfig
 * @author Administrator
 * @version 1.0
 * 这个类就相当于一个配置文件，因为在SpringBoot中，一个@Configuration注解就相当于一个配置文件的存在；
 **/
@Configuration
public class TokenConfig {
   @Bean
    public TokenStore tokenStore() {
        //使用内存存储令牌（普通令牌）
        return new InMemoryTokenStore();
    }
}
