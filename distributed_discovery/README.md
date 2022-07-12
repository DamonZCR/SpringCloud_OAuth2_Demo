## 简介

注册中心。技术基于eureka实现。

- 项目是注册中心服务server端，其他微服务（授权服务、网关服务、和资源服务等）都是client端，需要加上相应的依赖；

  server端：

  ​	<img src="img/README/image-20220712153342340.png" alt="image-20220712153342340" style="zoom:67%;" />

  客户端：

  <img src="img/README/image-20220712153451105.png" alt="image-20220712153451105" style="zoom:67%;" />

- 注册中心配置简单。