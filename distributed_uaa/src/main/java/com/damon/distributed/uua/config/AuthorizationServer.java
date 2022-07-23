package com.damon.distributed.uua.config;

import com.damon.distributed.uua.service.SpringDataUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/** 授权服务配置
 * @author Administrator
 * @version 1.0
 * 需要配置AuthorizationServerConfigurerAdapter类下的三个方法，这三个方法下就是对应的三个类；
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired//令牌的访问策略
    private TokenStore tokenStore;

    @Autowired//客户端的详情服务
    private ClientDetailsService clientDetailsService;

    @Autowired//配置授权码服务
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired//配置认证管理器
    private AuthenticationManager authenticationManager;

    @Autowired//对JWT生成的方式进行注入
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired//注入密码编码器
    private PasswordEncoder passwordEncoder;

    @Autowired//因为实现了UserDetailsService，对于密码模式需要所以在第二个类中配置；
    private SpringDataUserDetailsService springDataUserDetailsService;

    //实现将客户端信息存储到数据库,以及从数据库中查询客户端信息；
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }
    //启动对第一个类，客户端详情服务的配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    // 启动对第二个类，管理令牌类的构建
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)//认证管理器
                .userDetailsService(springDataUserDetailsService)//密码模式需要的用户详情，发现不配置也可以；
                .authorizationCodeServices(authorizationCodeServices)//授权码服务
                .tokenServices(tokenService())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //启动对第三个类，安全约束的配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security
                .tokenKeyAccess("permitAll()")//针对的是：JwtTokenStore，这个Url: oauth/token_key是公开的
                .checkTokenAccess("permitAll()")//令牌解析端点，针对资源服务对token进行合法性检验的，oauth/check_token公开
                .allowFormAuthenticationForClients()//允许表单认证（就是允许通过表单申请令牌）
        ;
    }

    //第二类使用，令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);//客户端详情服务
        service.setSupportRefreshToken(true);//是否支持刷新令牌
        service.setTokenStore(tokenStore);//令牌存储、访问策略，这里使用基于内存的
        //令牌增强，即表明JWT的生成策略；
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);

        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }

    //授权码保存到数据库中，原来保存到内存中。使用JDBC的方式保存
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//设置授权码模式的授权码如何存取
    }
}
