package com.damon.springcloud.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** 使用方法授权，zhangsan账号只拥有p1权限
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class OrderController {

    @GetMapping(value = "/r1")
    @PreAuthorize("hasAuthority('p1')")//拥有p1权限方可访问此url
    public String r1(){
        //返回信息
        return "访问资源1";
    }

    @GetMapping(value = "/r2")
    @PreAuthorize("hasAuthority('p5')")//拥有p5权限方可访问此url
    public String r2(){
        //返回信息
        return "访问资源2";
    }

}