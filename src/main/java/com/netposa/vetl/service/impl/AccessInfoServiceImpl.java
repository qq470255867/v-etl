package com.netposa.vetl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netposa.vetl.dao.AccessInfoJobMapMapper;
import com.netposa.vetl.dao.AccessInfoMapper;
import com.netposa.vetl.entity.AccessInfo;
import com.netposa.vetl.entity.AccessInfoJobMap;
import com.netposa.vetl.entity.PageRequest;
import com.netposa.vetl.service.AccessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @program: vetl-quartz-server
 * @description:
 * @author: kangyu
 * @create: 2020-01-08 12:03
 **/
@Service
public class AccessInfoServiceImpl implements AccessInfoService {

    @Autowired
    AccessInfoMapper accessInfoMapper;

    @Autowired
    AccessInfoJobMapMapper accessInfoJobMapMapper;

    @Override
    public int saveInfo(AccessInfo accessInfo) {
        return accessInfoMapper.insert(accessInfo);
    }

    @Override
    public void deleteInfo(long id) throws Exception {
        QueryWrapper<AccessInfoJobMap> accessInfoJobMapQueryWrapper = new QueryWrapper<>();
        accessInfoJobMapQueryWrapper.eq("access_info_id",id);
        List<AccessInfoJobMap> accessInfoJobMaps = accessInfoJobMapMapper.selectList(accessInfoJobMapQueryWrapper);
        if (CollectionUtils.isEmpty(accessInfoJobMaps)){
            accessInfoMapper.deleteById(id);
        }else throw new Exception("接入源已被使用，无法删除");

    }

    @Override
    public IPage<AccessInfo> getAccessInfoList(PageRequest<AccessInfo> accessInfo) {
        Page<AccessInfo> page = new Page<>();
        page.setCurrent(accessInfo.getCurPage());
        page.setSize(accessInfo.getPageSize());
        QueryWrapper<AccessInfo> wrapper = new QueryWrapper<>();
        if (accessInfo.getObject()!=null){
            wrapper.eq("access_type",accessInfo.getObject().getAccessType());
        }
        return accessInfoMapper.selectPage(page,wrapper);
    }

    @Override
    public int updateAccessInfo(AccessInfo accessInfo) {
        return accessInfoMapper.updateById(accessInfo);
    }

}
