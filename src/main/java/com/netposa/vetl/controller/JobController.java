package com.netposa.vetl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netposa.vetl.entity.JobEntity;
import com.netposa.vetl.entity.JobStatistics;
import com.netposa.vetl.entity.PageRequest;
import com.netposa.vetl.entity.WebResult;
import com.netposa.vetl.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @program: vetl-quartz-server
 * @description: 任务控制器
 * @author: kangyu
 * @create: 2020-01-06 15:22
 **/
@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {

    @Autowired
    JobService jobService;


    @PostMapping("/getAll")
    public WebResult getAllJob(@RequestBody PageRequest<JobEntity> pageRequest){
        try {
            List<JobEntity> jobList = jobService.getJobList(pageRequest.getParam());

            //逻辑分页
            List<JobEntity> page = jobList.stream().sorted(Comparator.comparing(JobEntity::getId))
                    .skip((pageRequest.getCurPage() - 1) * pageRequest.getPageSize())
                    .limit(pageRequest.getPageSize())
                    .collect(Collectors.toList());
            PageRequest<List<JobEntity>> result = new PageRequest<>();
            result.setObject(page);
            result.setTotal(jobList.size());
            return WebResult.SUCCESS(result);

        }catch (Exception e){
            log.error(e.getMessage(),e);
            return WebResult.ERROR(e.getMessage());
        }

    }
    @PostMapping("/update")
    public WebResult updateJob(@RequestBody JobEntity jobEntity){
        try {
            jobService.updateJob(jobEntity);
            return WebResult.SUCCESS(jobEntity);
        } catch (Exception e) {
            return WebResult.ERROR(e.getMessage());
        }
    }
    @GetMapping("/delete/{id}")
    public WebResult deleteJob(@PathVariable("id") long id){
        try {
            jobService.deleteJob(id);
            return WebResult.SUCCESS(id);
        }catch (Exception e){
            return WebResult.ERROR(e.getMessage());
        }
    }


    @PostMapping("/save")
    public WebResult saveJob(@RequestBody JobEntity entity){
        try {
            jobService.insertJob(entity);
            return WebResult.SUCCESS(entity);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return WebResult.ERROR(e.getMessage());
        }
    }









}
