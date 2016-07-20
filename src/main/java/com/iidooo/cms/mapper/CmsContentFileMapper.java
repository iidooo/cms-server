package com.iidooo.cms.mapper;

import com.iidooo.cms.model.po.CmsContentFile;

public interface CmsContentFileMapper {
    int deleteByPrimaryKey(Integer contentID);

    int insert(CmsContentFile record);

    int insertSelective(CmsContentFile record);

    CmsContentFile selectByPrimaryKey(Integer contentID);

    int updateByPrimaryKeySelective(CmsContentFile record);

    int updateByPrimaryKey(CmsContentFile record);
}