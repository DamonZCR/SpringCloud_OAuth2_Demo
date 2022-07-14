package com.damon.distributed.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/** 1、定义TokenConfig
 * @author Administrator
 * @version 1.0
 * 这个类就相当于一个配置文件，因为在SpringBoot中，一个@Configuration注解就相当于一个配置文件的存在；
 **/
@Configuration
public class TokenConfig {

    //JWT采用对称加密的方式，以下是密钥；
    private String SIGNING_KEY = "uaa123";

    //定义令牌的存储访问策略
    @Bean
    public TokenStore tokenStore() {
        //JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }
    //设置生成JWT的方法
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY); //对称秘钥，资源服务器使用该秘钥来验证
        return converter;
    }
}

