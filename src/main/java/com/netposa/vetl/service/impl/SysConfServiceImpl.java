package com.netposa.vetl.service.impl;

import com.netposa.vetl.dao.SysConfMapper;
import com.netposa.vetl.entity.SysConf;
import com.netposa.vetl.service.SysConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-20 15:43
 **/
@Service
public class SysConfServiceImpl implements SysConfService {


    @Autowired
    SysConfMapper sysConfMapper;


    @Override
    public SysConf getSysConf() {
        return sysConfMapper.selectOne(null);
    }


    @Override
    public void updateSysConf(SysConf sysConf) {
        sysConfMapper.update(sysConf,null);
    }
}
