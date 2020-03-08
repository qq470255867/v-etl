package com.netposa.vetl.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-08 12:10
 **/
@Data
public class PageRequest<T> {

    private int curPage;

    private int pageSize;

    private long total;

    private T object;

    private Map<String,Object> param;
}
