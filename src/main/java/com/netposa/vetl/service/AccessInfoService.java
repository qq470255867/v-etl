package com.netposa.vetl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.netposa.vetl.entity.AccessInfo;
import com.netposa.vetl.entity.PageRequest;

import java.util.List;

public interface AccessInfoService {

    public int saveInfo(AccessInfo accessInfo);

    public void deleteInfo(long id) throws Exception;

    public IPage<AccessInfo> getAccessInfoList(PageRequest<AccessInfo> accessInfo);

    public int updateAccessInfo(AccessInfo accessInfo);

}
