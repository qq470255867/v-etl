package com.netposa.vetl.init;

import com.alibaba.fastjson.JSON;
import com.netposa.vetl.core.AbstractJobApi;
import com.netposa.vetl.dao.JobEntityMapper;
import com.netposa.vetl.entity.JobEntity;
import com.netposa.vetl.entity.SysConf;
import com.netposa.vetl.service.SysConfService;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @program: vetl-quartz-server
 * @description: 任务初始化
 * @author: kangyu
 * @create: 2020-01-07 19:50
 **/
@Component
@Slf4j
public class PlatInit {

    @Autowired
    JobEntityMapper jobEntityMapper;

    @Autowired
    AbstractJobApi jobApi;

    @Autowired
    SysConfService sysConfService;

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;


    @PostConstruct
    public void jobInt(){
        List<JobEntity> jobAll = jobEntityMapper.selectList(null);
        jobAll.stream().filter(JobEntity::isOpen).forEach(j -> {
            log.info("初始化启动任务 {}",j.getName());
            try {
                log.info(JSON.toJSONString(schedulerFactoryBean.getScheduler().getMetaData()));
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            jobApi.refreshJob(j);
        });
    }

    @PostConstruct
    public void sysConfInit(){
        SysConf.SYS_CONF = sysConfService.getSysConf();
    }

}
