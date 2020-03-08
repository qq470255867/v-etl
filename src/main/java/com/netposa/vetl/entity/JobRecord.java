package com.netposa.vetl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: SpringBoot-Quartz
 * @description:
 * @author: kangyu
 * @create: 2019-12-22 12:51
 **/
@Data
@TableName("job_record")
public class JobRecord {

    @TableId(type = IdType.AUTO)
    private long id;

    private int jobId;

    private Long costTime;

    private String log;

    private String jobDate;

    private int dataVolume;
}
