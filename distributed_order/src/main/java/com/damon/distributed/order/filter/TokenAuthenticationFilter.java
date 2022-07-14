package com.damon.distributed.order.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.damon.distributed.order.common.EncryptUtil;
import com.damon.distributed.order.model.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
            //解析出头中的token
        String token = httpServletRequest.getHeader("json-token");
        if(token != null){

            // 1、解析token
            String json = EncryptUtil.decodeUTF8StringBase64(token);
            //将token转成json对象
            JSONObject jsonObject = JSON.parseObject(json);
            //用户身份信息
            UserDTO userDTO = new UserDTO();
            String principal = jsonObject.getString("principal");
            userDTO.setUsername(principal);//只是得到用户信息
            //用户权限
            JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
            String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);

            System.out.println("\n访问的用户(非客户端)的信息: " + principal + "权限列表: " + Arrays.toString(authorities) + "\n");

            //2、新建并将用户信息和权限填充到用户身份token对象authenticationToken中,这才是被Spring Security识别的token
            UsernamePasswordAuthenticationToken authenticationToken//参数依次是：身份信息、凭证、权限，权限的话需要使用它提供工具类转为特定的格式；
                    = new UsernamePasswordAuthenticationToken(userDTO,null, AuthorityUtils.createAuthorityList(authorities));
            //Detail的一个属性，我们只需要根据原来的请求创建出来即可；
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            //3、将authenticationToken填充到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);//让过滤器继续往前走
    }
}
