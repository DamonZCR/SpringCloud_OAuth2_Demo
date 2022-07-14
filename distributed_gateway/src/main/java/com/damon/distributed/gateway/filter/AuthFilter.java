package com.damon.distributed.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.damon.distributed.gateway.common.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Zuul过滤器的方式实现网关转发明文token给微服务
 * @author Administrator
 * @version 1.0
 * 目的是让下游微服务能够很方便的获取到当前的登录用户信息（明文token）。因为网关已经解析了JWT的token，
 * 并且根据资源服务里配置，匹配了访问这个资源所需的权限，所以可以通过网关到达服务了；
 * （1）实现Zuul前置过滤器，完成当前登录用户信息提取，并放入转发微服务的request中
 **/
public class AuthFilter extends ZuulFilter {
    //以下方法都是ZuuFilter中的抽象方法，根据选择实现；
    @Override//想要实现过滤所以设置为true
    public boolean shouldFilter() {
        return true;
    }

    @Override//过滤类型，设置为在请求具体资源服务之前拦截
    public String filterType() {
        return "pre";
    }

    @Override//过滤优先级，越小越优先
    public int filterOrder() {
        return 0;
    }

    @Override//这里面实现具体的工作
    public Object run() throws ZuulException {

        /*1、获取令牌内容*/
        RequestContext ctx = RequestContext.getCurrentContext();//得到request，这个方法由网关提供；
        //从安全上下文中拿到用户身份对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 无token访问网关内资源的情况，目前仅有uaa服务直接访问
        if(!(authentication instanceof OAuth2Authentication)){
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        //取出用户身份信息
        String principal = userAuthentication.getName();

        /*2、组装成明文token，转发给微服务，放入header，名称为json-token*/
        //取出用户权限
        List<String> authorities = new ArrayList<>();
        //从userAuthentication取出权限，放在authorities
        userAuthentication.getAuthorities().stream().forEach(c->authorities.add(((GrantedAuthority) c).getAuthority()));

        /*3、这个令牌中可能还有一些其他的信息参数，所以可以放进Map中一并打包*/
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String,Object> jsonToken = new HashMap<>(requestParameters);
        if(userAuthentication != null){
            jsonToken.put("principal",principal);//用户身份
            jsonToken.put("authorities",authorities);//用户权限
        }

        //把身份信息和权限信息放在json中，加入http的header中,转发给微服务。编码格式Base64
        ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));

        return null;
    }
}
