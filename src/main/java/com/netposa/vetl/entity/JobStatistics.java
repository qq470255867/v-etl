package com.netposa.vetl.entity;

import lombok.Data;

/**
 * @program: SpringBoot-Quartz
 * @description:
 * @author: kangyu
 * @create: 2019-12-22 14:47
 **/
@Data
public class JobStatistics {

    private int jobId;

    private long runNum;

    private String lastDate;

    private float avgCost;

    private String jobName;

    private String status;

    // 数据量  包含数据 N/A 所以不用整形
    private String dataVolume;

    // 数据量按日期统计

}
