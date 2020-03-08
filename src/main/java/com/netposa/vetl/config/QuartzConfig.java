package com.netposa.vetl.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.netposa.vetl.domain.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @program: vetl
 * @description:
 * @author: kangyu
 * @create: 2020-01-26 21:22
 **/
@Component
@Slf4j
@AutoConfigureAfter(MybatisPlusConfig.class)
public class QuartzConfig {


    @Autowired
    DataSource dataSource;

    @Autowired
    ApplicationContext applicationContext;

    //@Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws Exception {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        Properties properties = new Properties();
        //认为最大并发数 == 逻辑cpu数量
        Server server = new Server();
        server.copyTo();
        int cpuNum = server.getCpu().getCpuNum();
        properties.load(this.getClass().getResourceAsStream("/quartz.properties"));
        properties.setProperty("org.quartz.threadPool.threadCount",cpuNum+"");
        log.info("自动配置 SchedulerFactoryBean 默认操作系统支持 {} 并发",cpuNum);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setApplicationContext(applicationContext);
        return new SchedulerFactoryBean();
    }

}
