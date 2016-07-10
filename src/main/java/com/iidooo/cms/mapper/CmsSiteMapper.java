package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsSite;

public interface CmsSiteMapper {
    int deleteByPrimaryKey(Integer siteID);

    int insert(CmsSite record);

    int insertSelective(CmsSite record);

    CmsSite selectByPrimaryKey(Integer siteID);
    
    /**
     * 查询获取拥有者的站点列表
     * @param userID 拥有者ID
     * @return 站点列表
     */
    List<CmsSite> selectByOwner(Integer userID);

    int updateByPrimaryKeySelective(CmsSite record);

    int updateByPrimaryKey(CmsSite record);
}