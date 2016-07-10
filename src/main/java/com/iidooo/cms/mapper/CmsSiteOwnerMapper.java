package com.iidooo.cms.mapper;

import com.iidooo.cms.model.po.CmsSiteOwner;

public interface CmsSiteOwnerMapper {
    int deleteByPrimaryKey(Integer relID);

    int insert(CmsSiteOwner record);

    int insertSelective(CmsSiteOwner record);

    CmsSiteOwner selectByPrimaryKey(Integer relID);

    int updateByPrimaryKeySelective(CmsSiteOwner record);

    int updateByPrimaryKey(CmsSiteOwner record);
}