package com.netposa.vetl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netposa.vetl.dao.JobEntityMapper;
import com.netposa.vetl.dao.JobRecordMapper;
import com.netposa.vetl.entity.DataVolume;
import com.netposa.vetl.entity.JobRecord;
import com.netposa.vetl.entity.JobStatistics;
import com.netposa.vetl.service.JobRecordService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-06 17:36
 **/
@Service
public class JobRecordServiceImpl implements JobRecordService {

    @Autowired
    JobRecordMapper jobRecordMapper;

    @Autowired
    JobEntityMapper jobEntityMapper;

    @Override
    public List<JobRecord> getJobLogsById(long jobId) {
        QueryWrapper<JobRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id",jobId);
        queryWrapper.orderByDesc("job_date");
        queryWrapper.last("limit 50");

        return jobRecordMapper.selectList(queryWrapper);
    }

    @Override
    public List<JobStatistics> getJobStatistics() {
        List<JobStatistics> jobStatistics = jobRecordMapper.getJobStatistics();
        jobStatistics.forEach(a -> {
            QueryWrapper<JobRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_id", a.getJobId()).orderByDesc("job_date").last("limit 1");
            jobRecordMapper.
                    selectList(queryWrapper).
                    stream().findFirst().
                    ifPresent(jobRecord ->
                            a.setStatus(analiseLog(jobRecord.getLog())));
            if (!jobEntityMapper.selectById(a.getJobId()).isOpen()){
                String status = new StringBuilder().append("<p style=\"color:grey;\">").append("停用").append("</p>").toString();
                a.setStatus(status);
            }
        });

        return jobStatistics;
    }

    @Override
    public List<DataVolume> getDataVolume(long jobId) {
        return jobRecordMapper.getDataVolume(jobId);
    }

    @Override
    public void insertRecord(JobRecord record) {
        jobRecordMapper.insert(record);
    }

    @Override
    public List<JobRecord> getJobRecord(long jobId) {
        QueryWrapper<JobRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id",jobId);
        queryWrapper.orderByDesc("job_date");
        queryWrapper.last("limit 100");
        return  jobRecordMapper.selectList(queryWrapper);
    }

    String analiseLog(String log){
        String status = "";
        if (log.contains("error")||
                log.contains("Error")||
                log.contains("ERROR")||
                log.contains("Exception")||
                log.contains("exception")
        ){
            status = new StringBuilder().append("<p style=\"color:red;\">").append("异常").append("</p>").toString();

        }else {
            status = new StringBuilder().append("<p style=\"color:green;\">").append("正常").append("</p>").toString();

        }
        return status;
    }




}
