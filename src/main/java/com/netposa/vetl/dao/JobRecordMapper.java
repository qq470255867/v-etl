package com.netposa.vetl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.netposa.vetl.entity.DataVolume;
import com.netposa.vetl.entity.JobRecord;
import com.netposa.vetl.entity.JobStatistics;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRecordMapper extends BaseMapper<JobRecord> {


    @Select("SELECT\n" +
            "\ta.job_id jobId,\n" +
            "\tCOUNT( a.id ) runNum,\n" +
            "\tMAX( a.job_date ) lastDate,\n" +
            "\tAVG( a.cost_time ) avgCost,\n" +
            "\tb.NAME jobName,\n" +
            " case  \n" +
            "  When\n" +
            "\t sum(a.data_volume) is null  then 'N/A'\n" +
            "\t when\n" +
            "\t sum(a.data_volume) = 0 then 'N/A'\n" +
            "\t else\n" +
            "\t\t sum(a.data_volume)\n" +
            "\t end 'dataVolume'\n" +
            "FROM\n" +
            "\tjob_record a\n" +
            "\tLEFT JOIN job_entity b ON a.job_id = b.id \n" +
            "GROUP BY\n" +
            "\ta.job_id")
    public List<JobStatistics> getJobStatistics();


    @Select("SELECT\n" +
            "\tjob_id as jobId,\n" +
            "\tDATE_FORMAT( job_date, '%Y-%m-%d' ) AS date,\n" +
            "\tsum( data_volume ) AS volume ,\n" +
            "\tcase \n" +
            "\t  When\n" +
            "\t sum(data_volume) is null then 0\n" +
            "\t else\n" +
            "\t\t 1\n" +
            "\t end 'hasVolume'\n" +
            "FROM\n" +
            "\tjob_record \n" +
            "GROUP BY\n" +
            "\tjob_id,\n" +
            "\tDATE_FORMAT( job_date, '%Y-%m-%d' ) \n" +
            "HAVING\n" +
            "\tjob_id = #{jobId}")
    public List<DataVolume> getDataVolume(long jobId);

}
