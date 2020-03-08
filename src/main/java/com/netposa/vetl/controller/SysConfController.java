package com.netposa.vetl.controller;

import com.netposa.vetl.domain.Server;
import com.netposa.vetl.entity.SysConf;
import com.netposa.vetl.entity.WebResult;
import com.netposa.vetl.service.SysConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-20 15:41
 **/
@RestController
@RequestMapping("sys")
@Slf4j
public class SysConfController {

    @Autowired
    SysConfService sysConfService;


    @RequestMapping("/get_conf")
    public WebResult getConf(){
        try {
            return WebResult.SUCCESS(SysConf.SYS_CONF);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return WebResult.ERROR(e.getMessage());
        }
    }

    @RequestMapping("/update_conf")
    public WebResult updateConf(@RequestBody SysConf sysConf){
        try {
            sysConfService.updateSysConf(sysConf);
            SysConf.SYS_CONF = sysConf;
            return WebResult.SUCCESS(sysConf);
        }catch (Exception e){
            return WebResult.ERROR(e.getMessage());
        }
    }


    @GetMapping("/get_info")
    public Server getServer() throws Exception {
        Server server = new Server();
        server.copyTo();
        return server;
    }
}
