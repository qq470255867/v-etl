package com.netposa.vetl.controller;

import com.netposa.vetl.entity.JobEntity;
import com.netposa.vetl.entity.SysConf;
import com.netposa.vetl.entity.WebResult;
import com.netposa.vetl.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: vetl-quartz-server
 * @description: 文件上传
 * @author: kangyu
 * @create: 2020-01-06 17:23
 **/
@RestController
@RequestMapping("/fileupload")
public class FileController {



    @Autowired
    JobService jobService;


    @PostMapping("/upload/jar")
    public WebResult uploadJar(MultipartFile file){
        if (!file.getOriginalFilename().endsWith("jar")){
            return WebResult.ERROR("不支持文件类型");
        }
        try {
            String filePath = SysConf.SYS_CONF.getJarPath() +voidSameFileName(file.getOriginalFilename());
            File dest = new File(filePath);
            file.transferTo(dest);
            return WebResult.SUCCESS(filePath);
        }catch (Exception e){
            return WebResult.ERROR(e.getMessage());
        }

    }
    @PostMapping("/upload/src")
    public WebResult uploadSrc(MultipartFile file){
        try {
            if (!file.getOriginalFilename().endsWith("zip")){
                return WebResult.ERROR("不支持文件类型");
            }
            String filePath = SysConf.SYS_CONF.getSrcPath() +voidSameFileName(file.getOriginalFilename());
            File dest = new File(filePath);
            file.transferTo(dest);
            return WebResult.SUCCESS(filePath);
        }catch (Exception e){
            return WebResult.ERROR(e.getMessage());
        }
    }

    @GetMapping("/download_demo_code")
    public WebResult downloadDemoCode(HttpServletRequest request, HttpServletResponse response){
        String fileDownName ="vetl-mini-job.zip";
        try {
            InputStream fis = FileController.class.getResourceAsStream("/template/vetl-mini-job.zip");
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.setContentType("bin");
            String fileNames = fileDownName;
            String agent = request.getHeader("USER-AGENT");
            String codedfilename = "";
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

                String name = java.net.URLEncoder.encode(fileNames, "UTF8");

                codedfilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }
            response.addHeader("Content-Disposition", "attachment; filename=\"" + codedfilename + "\"");
            response.getOutputStream().write(buffer);
            return WebResult.SUCCESS(null);
        } catch (IOException e) {
            return WebResult.ERROR(e.getMessage());
        }
    }

    @GetMapping("/download_src_code/{id}")
    public WebResult downloadSrcCode(@PathVariable("id") long id, HttpServletRequest request, HttpServletResponse response){
        try {
            JobEntity jobEntity = jobService.getJobById(id);
            FileInputStream fileInputStream = new FileInputStream(new File(jobEntity.getSrcPath()));
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            response.reset();
            response.setContentType("bin");
            String fileNames = jobEntity.getName()+".zip";
            String agent = request.getHeader("USER-AGENT");
            String codedfilename = "";
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

                String name = java.net.URLEncoder.encode(fileNames, "UTF8");

                codedfilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }
            response.addHeader("Content-Disposition", "attachment; filename=\"" + codedfilename + "\"");
            response.getOutputStream().write(buffer);

            return WebResult.SUCCESS(null);
        }catch (Exception e){

            return WebResult.ERROR(e.getMessage());
        }
    }

    String voidSameFileName(String name){
        String[] split = name.split("\\.");
        if (split.length==1){
            return name;
        }else {
            String prefix = name.replaceAll("\\." + split[split.length - 1], "");
            return prefix+System.currentTimeMillis()+"."+split[split.length - 1];
        }
    }






}
