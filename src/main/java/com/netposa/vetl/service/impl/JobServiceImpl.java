package com.netposa.vetl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netposa.vetl.core.AbstractJobApi;
import com.netposa.vetl.dao.AccessInfoJobMapMapper;
import com.netposa.vetl.dao.AccessInfoMapper;
import com.netposa.vetl.dao.JobEntityMapper;
import com.netposa.vetl.dao.JobRecordMapper;
import com.netposa.vetl.entity.AccessInfo;
import com.netposa.vetl.entity.AccessInfoJobMap;
import com.netposa.vetl.entity.JobEntity;
import com.netposa.vetl.entity.JobRecord;
import com.netposa.vetl.service.JobService;
import com.netposa.vetl.utils.StringUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-06 15:43
 **/
@Component
public class JobServiceImpl implements JobService {

    @Autowired
    JobEntityMapper jobEntityMapper;

    @Autowired
    AbstractJobApi jobApi;

    @Autowired
    AccessInfoJobMapMapper accessInfoJobMapMapper;

    @Autowired
    AccessInfoMapper accessInfoMapper;

    @Autowired
    JobRecordMapper jobRecordMapper;

    @Override
    public int updateJob(JobEntity jobEntity) {
        jobApi.refreshJob(jobEntity);
        return jobEntityMapper.updateById(jobEntity);
    }

    @Override
    public int deleteJob(long jobId) {
        JobEntity jobEntity = jobEntityMapper.selectById(jobId);
        jobApi.deleteJob(jobEntity);
        new File(jobEntity.getJarPath()).delete();
        new File(jobEntity.getSrcPath()).delete();
        return jobEntityMapper.deleteById(jobId);
    }

    @Override
    public int insertJob(JobEntity jobEntity) {
        jobEntity.setJobGroup(jobEntity.getAccessType()+"_"+jobEntity.getOutputType());
        int i = jobEntityMapper.insert(jobEntity);
        jobApi.refreshJob(jobEntity);
        saveAccessTypeMap(jobEntity.getAccessType(),jobEntity.getOutputType(),jobEntity.getId());
        return i;
    }

    @Override
    public List<JobEntity> getJobList(Map<String,Object> queryMap) {
        QueryWrapper<JobEntity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(queryMap.get("content"))){
            queryWrapper.like("name",(String)queryMap.get("content"));
        }

        List<JobEntity> jobEntities = jobEntityMapper.selectList(queryWrapper);


        jobEntities.forEach(j -> {
            List<AccessInfoJobMap> accessInfoJobMaps = accessInfoJobMapMapper.selectList(new QueryWrapper<AccessInfoJobMap>().eq("job_id", j.getId()));
            if (!CollectionUtils.isEmpty(accessInfoJobMaps)){
                QueryWrapper<AccessInfo> wrapper = new QueryWrapper<>();
                List<Long> accessInfoIds = accessInfoJobMaps.stream().map(AccessInfoJobMap::getAccessInfoId).collect(Collectors.toList());
                wrapper.in("id",accessInfoIds);
                List<AccessInfo> accessInfos = accessInfoMapper.selectList(wrapper);
                accessInfos.forEach(accessInfo -> {
                    if (AccessInfo.INPUT==accessInfo.getAccessType()){
                        j.setAccessInfo(accessInfo);
                    }else if (AccessInfo.OUTPUT==accessInfo.getAccessType()){
                        j.setOutputInfo(accessInfo);
                    }
                });
                j.setAccessInfos(accessInfos);
            }

            List<JobRecord> jobRecord = jobRecordMapper.selectList(new QueryWrapper<JobRecord>().eq("job_id", j.getId()).last("limit 1"));
            if (!CollectionUtils.isEmpty(jobRecord)){
                j.setExecuted(true);
            }

        });
        List<JobEntity> result = new ArrayList<>(jobEntities);
        if (!StringUtils.isEmpty(queryMap.get("input"))){
            result =  jobEntities.stream()
                    .filter(jobEntity -> jobEntity.getAccessInfo().getId() == Long.parseLong(queryMap.get("input")+"") ).collect(Collectors.toList());
        }

        if (!StringUtils.isEmpty(queryMap.get("output"))){
            return result.stream().filter(jobEntity -> jobEntity.getOutputInfo().getId() == Long.parseLong(queryMap.get("output")+"")).collect(Collectors.toList());
        }

        return result;
    }

    @Override
    public JobEntity getJobById(long id) {
        return jobEntityMapper.selectById(id);
    }


    public void saveAccessTypeMap(int accessType, int outputType,long jobId){
        AccessInfoJobMap accessInfoJobMap = new AccessInfoJobMap();
        accessInfoJobMap.setAccessInfoId(accessType);
        accessInfoJobMap.setJobId(jobId);
        AccessInfoJobMap outputJobMap = new AccessInfoJobMap();
        outputJobMap.setAccessInfoId(outputType);
        outputJobMap.setJobId(jobId);
        accessInfoJobMapMapper.insert(accessInfoJobMap);
        accessInfoJobMapMapper.insert(outputJobMap);

    }

}
