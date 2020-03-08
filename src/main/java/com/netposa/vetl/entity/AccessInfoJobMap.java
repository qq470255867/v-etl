package com.netposa.vetl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-08 16:29
 **/
@TableName("access_job_map")
@Data
public class AccessInfoJobMap {

    @TableId(type = IdType.AUTO)
    private long id;

    private long jobId;

    private long accessInfoId;

}
