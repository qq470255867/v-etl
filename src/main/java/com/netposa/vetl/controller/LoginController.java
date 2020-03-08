package com.netposa.vetl.controller;

import com.alibaba.fastjson.JSON;
import com.netposa.vetl.entity.WebResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-06 15:23
 **/
@RestController
public class LoginController {


    @PostMapping("/login")
    public WebResult foolLogin(@RequestBody Map<String,String> map, HttpServletRequest request){
        if ("admin".equals(map.get("username"))&&"admin".equals(map.get("password"))){
       //     request.getSession().setAttribute("login","admin");
            return WebResult.SUCCESS("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        }else {
            return WebResult.ERROR("用户名密码错误");
        }
    }
}
