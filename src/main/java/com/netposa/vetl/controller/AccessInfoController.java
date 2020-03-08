package com.netposa.vetl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.netposa.vetl.entity.AccessInfo;
import com.netposa.vetl.entity.PageRequest;
import com.netposa.vetl.entity.WebResult;
import com.netposa.vetl.service.AccessInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-08 11:52
 **/
@RestController
@RequestMapping("/access_info")
@Slf4j
public class AccessInfoController {

    @Autowired
    AccessInfoService accessInfoService;


    @PostMapping("/save")
    public WebResult save(@RequestBody AccessInfo accessInfo){
       try {
           accessInfoService.saveInfo(accessInfo);
           return WebResult.SUCCESS(accessInfo);
       }catch (Exception e){
           log.error(e.getMessage(),e);
           return WebResult.ERROR(e.getMessage());
       }

    }
    @PostMapping("/page/list")
    public WebResult getList(@RequestBody PageRequest<AccessInfo> accessInfoPageRequest){
        try {
            IPage<AccessInfo> accessInfoList = accessInfoService.getAccessInfoList(accessInfoPageRequest);
            return WebResult.SUCCESS(accessInfoList);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return WebResult.ERROR(e.getMessage());
        }
    }
    @GetMapping("/delete/{id}")
    public WebResult delete(@PathVariable("id") long id){
        try {
            accessInfoService.deleteInfo(id);
            return WebResult.SUCCESS(id);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return WebResult.ERROR(e.getMessage());
        }
    }


}
