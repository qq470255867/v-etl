package com.netposa.vetl.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.netposa.vetl.entity.*;
import com.netposa.vetl.service.JobRecordService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @program: vetl-quartz-server
 * @description: 任务日志
 * @author: kangyu
 * @create: 2020-01-06 17:34
 **/
@RestController
@RequestMapping("/record")
public class JobRecordController {


    @Autowired
    JobRecordService jobRecordService;


    @PostMapping("/statistics/list")
    public PageRequest<List<JobStatistics>> getJobStatistics(@RequestBody PageRequest pageRequest){
        //任务数量不大，可以采用逻辑分页
        List<JobStatistics> list = jobRecordService.getJobStatistics().stream()
                .filter(
                        jobStatistics -> {
                            //指定详情
                            if (!StringUtils.isEmpty(pageRequest.getParam().get("jobId"))){
                                return jobStatistics.getJobId() == Integer.parseInt(pageRequest.getParam().get("jobId").toString());
                            }else {
                                return true;
                            }
                        }).collect(Collectors.toList());
        List<JobStatistics> model = list.stream()
                .sorted(Comparator.comparing(JobStatistics::getJobName))
                .skip((pageRequest.getCurPage() - 1 ) * pageRequest.getPageSize())
                .limit(pageRequest.getPageSize())
                .collect(Collectors.toList());
        PageRequest<List<JobStatistics>> result = new PageRequest<>();
        result.setTotal(list.size());
        result.setObject(model);
        return result;
    }

    @GetMapping("/log/{id}")
    List<JobRecord> getJobRecord(@PathVariable("id") long id){

        return jobRecordService.getJobLogsById(id);

    }

    @GetMapping("/volume/{jobId}")
    public HashMap<String, Object> getDataVolume(@PathVariable("jobId") long jobId){

        List<DataVolume> dataVolume = jobRecordService.getDataVolume(jobId).
                stream().
                filter(DataVolume::isHasVolume).
                sorted(Comparator.comparing(DataVolume::getDate)).
                collect(Collectors.toList());

        HashMap<String, Object> map = new HashMap<>();
        //日期
        List<String> dateList = dataVolume.stream().map(DataVolume::getDate).collect(Collectors.toList());
        map.put("dateList",dateList);

        //数量
        List<String> volumeList = dataVolume.stream().map(DataVolume::getVolume).collect(Collectors.toList());
        map.put("volumeList",volumeList);

        return map;
    }

    @GetMapping("/cost_time/{id}")
    public WebResult getCostTimeById(@PathVariable("id") long id){

        List<JobRecord> jobRecords = jobRecordService.getJobRecord(id).stream().sorted(Comparator.comparing(JobRecord::getJobDate)).collect(Collectors.toList());

        //日期
        List<String> dateList = jobRecords.stream().map(JobRecord::getJobDate).collect(Collectors.toList());

        //时长
        List<Long> timeList = jobRecords.stream().map(JobRecord::getCostTime).collect(Collectors.toList());


        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("dateList",dateList);
        resultMap.put("timeList",timeList);

        return WebResult.SUCCESS(resultMap);

    }
}
