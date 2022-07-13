package com.damon.distributed.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/** 1、资源服务器配置
 * @author Administrator
 * @version 1.0
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启方法授权
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {
    //整个资源服务的ID
    public static final String RESOURCE_ID = "res1";
    @Autowired//JWT令牌的存储访问策略
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)//资源 id
                //.tokenServices(tokenService())//验证令牌的服务,服务方法实现在下方
                .tokenStore(tokenStore)//使用新的令牌校验服务；
                .stateless(true);
    }

    //配置资源服务的安全安全访问策略
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
            .authorizeRequests()
            .antMatchers("/**").access("#oauth2.hasScope('all')")
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
