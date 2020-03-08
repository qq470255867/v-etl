package com.netposa.vetl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netposa.vetl.entity.JobEntity;
import org.quartz.Job;

import java.util.List;
import java.util.Map;

public interface JobService {

    int updateJob(JobEntity jobEntity);

    int deleteJob(long jobId);

    int insertJob(JobEntity jobEntity);

    List<JobEntity> getJobList(Map<String,Object> queryMap);

    JobEntity getJobById(long id);

}
