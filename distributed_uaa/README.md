## 认证授权服务

这个项目基于JWT的客户端信息实现了简单的授权认证功能。

- 客户端也就是网站，如B站，黑马网站等。

- 用户就是我们使用者本身。

- 授权服务器就是第三方账号的服务器，如微信、QQ等；

```
我们想要登录客户端（B站），不想要注册账号，就想要使用第三方账号登录（微信）登录，当我们在客户端B站登录首页点击以微信账号登录时，B站就会将B站在第三方账号微信授权服务器上的备案的client_id发送给微信授权服务器，授权服务器收到以后就会将授权页面在浏览器网页上展示给用户，用户扫码或者密码登录，然后同意授权。
```

**这个项目就是要实现微信授权服务器的作用。**

项目配置在SpringCloud下，项目本身是一个授权微服务基于SpringBoot。配置了：OAuth2.0提供的四种模式。

**对应的项目：**

​	distributed_uaa + distributed_order资源服务

**实现（springcloud_ｕａａ）：**

- 实现基于JWT的令牌服务。授权服务器不需要保存生成的token，只需要根据客户端的信息 + 过期时间进行签名，客户端带着JWT访问资源服务，资源服务器验证签名信息，然后根据过期时间，就不必每次都要和授权服务器验证；
- 客户端信息保存至数据库。客户端c1的密码是secret，客户端c2的密码是secret2;
- 如果使用授权码模式的话，将授权码保存到数据库中。OAuth2.0会自动将生成的授权码保存到数据库中，只需要建好表即可；

**拓展：**

在原springcloud_uaa2的基础上配置分布式的UAA授权功能。

- 以eureka作为分布式注册中心。项目为：distributed_discovery
- 添加网关distributed_gateway;
