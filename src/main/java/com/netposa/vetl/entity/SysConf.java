package com.netposa.vetl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-20 15:39
 **/
@Data
@TableName("sys_conf")
public class SysConf {

    private String platName;

    private String jarPath;

    private String srcPath;

    private Long logInterval;

    private String dataStatistical;

    public static SysConf SYS_CONF = null;

}
