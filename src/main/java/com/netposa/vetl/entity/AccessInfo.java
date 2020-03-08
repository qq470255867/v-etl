package com.netposa.vetl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * @program: vetl-quartz-server
 * @description: 接入类型
 * @author: kangyu
 * @create: 2020-01-08 11:42
 **/
@TableName("access_info")
@Data
public class AccessInfo {

    public static final int INPUT = 1;

    public static final int OUTPUT = 2;

    @TableId(type = IdType.AUTO)
    private long id;

    private int accessType;

    private String name;

    private String type;

    private String ipPort;
}
