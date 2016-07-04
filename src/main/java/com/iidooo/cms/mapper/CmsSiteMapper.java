package com.iidooo.cms.mapper;

import com.iidooo.cms.model.po.CmsSite;

public interface CmsSiteMapper {
    int deleteByPrimaryKey(Integer siteID);

    int insert(CmsSite record);

    int insertSelective(CmsSite record);

    CmsSite selectByPrimaryKey(Integer siteID);

    int updateByPrimaryKeySelective(CmsSite record);

    int updateByPrimaryKey(CmsSite record);
}