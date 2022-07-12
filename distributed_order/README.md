## 简介

项目基于springcloud_order2,原项目简单实现了基于JWT的OAuth2.0协议，与springcloud_uaa2配合使用。

此项目部署到分布式中：

- 仍是由springcloud_uaa2改造来的distributed_uaa提供授权认证。
- distributed_gateway提供网关服务；
- distributed_discover提供注册发现服务；