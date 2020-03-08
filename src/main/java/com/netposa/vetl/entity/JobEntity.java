package com.netposa.vetl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


@Data
@TableName("job_entity")
public class JobEntity {

    @TableId(type = IdType.AUTO)
    private long id;
    private String name;          //job名称
    private String jobGroup;      //job组名
    private String cron;          //执行的cron
    private String parameter;     //job的参数
    private String description;   //job描述信息
    private String vmParam;       //vm参数
    private String jarPath;       //job的jar路径
    private boolean isOpen;

    private String srcPath;

    @TableField(exist = false)
    private boolean hasSrcCode;
    @TableField(exist = false)
    private int accessType;
    @TableField(exist = false)
    private int outputType;

    @TableField(exist = false)
    private List<AccessInfo> accessInfos;
    @TableField(exist = false)
    private AccessInfo accessInfo;
    @TableField(exist = false)
    private AccessInfo outputInfo;

    @TableField(exist = false)
    private boolean executed;

}
