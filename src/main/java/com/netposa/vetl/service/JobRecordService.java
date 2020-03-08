package com.netposa.vetl.service;


import com.netposa.vetl.entity.DataVolume;
import com.netposa.vetl.entity.JobRecord;
import com.netposa.vetl.entity.JobStatistics;

import java.util.List;

public interface JobRecordService {


    List<JobRecord> getJobLogsById(long jobId);

    List<JobStatistics> getJobStatistics();

    List<DataVolume> getDataVolume(long jobId);

    void insertRecord(JobRecord record);

    List<JobRecord> getJobRecord(long jobId);

}
