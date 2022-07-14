package com.damon.distributed.order.controller;

import com.alibaba.fastjson.JSON;
import com.damon.distributed.order.model.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // 获得用户信息，获得的信息全存储在getUsername()中，其他属性全为空，bug之一
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDTO.getFullname();
        //返回信息
        return name + "访问资源1 - 权限资源 - 对应权限 p1";
    }

    @GetMapping(value = "/r2")
    @PreAuthorize("hasAuthority('p2')")//拥有p5权限方可访问此url
    public String r2(){
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDTO.getFullname();
        //返回信息
        return name + "访问资源2 权限资源 对应权限 p2";
    }

    @GetMapping(value = "/r3")
    //无任何权限都可访问此 url
    public String r3(){
        // 获得用户信息
        UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = userDTO.getFullname();
        //返回信息
        return name + "访问资源3 匿名资源 对应权限 无";
    }
}