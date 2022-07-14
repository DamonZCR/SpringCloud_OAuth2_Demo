## 简介

项目基于springcloud_order2,原项目简单实现了基于JWT的OAuth2.0协议，与springcloud_uaa2配合使用。

此项目部署到分布式中：

- 仍是由springcloud_uaa2改造来的distributed_uaa提供授权认证。
- distributed_gateway提供网关服务；
- distributed_discover提供注册发现服务；

**实现**

- 网关对访问该资源服务的请求进行拦截，如果客户端的携带的token的scope和网关中定义的此资源服务的scope一致就放行，注意：此项目中也定义了scope，所以，必须三者一致时才放行；
- 网关中的Spring Security对客户端请求校验成功后，网关对请求此资源服务的请求token进行解析，解析出用户信息和权限列表后转发给此资源服务（在网管项目AuthFilter.java中实现），发送的形式是放到json-token里。
- 此项目将网关发送来的token解析出来，获得用户信息，从本项目的Contrller可以看出，当从Authention中取出用户信息时，出现错误，主要原因是网关放入principal后，本身就是无规则的。所以此处需要改进；
- 问题：整个分布式项目的整体实现了：网关对客户端请求的token解析出用户信息，然后将用户信息发送给资源服务，这个过程网关要解析token，然后取出用户信息，然后将用户信息打包为token，发送给资源服务后，资源服务再将token解析得到用户信息，不如资源服务解析第一次的token了。所以改进之处可以在：网关对客户端请求初次筛选后，如果权限通过就放心，将token由资源自己解析；

**下一步改善**

- 由资源服务进行第一次的token解析；