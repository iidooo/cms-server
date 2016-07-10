package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsChannel;

public interface CmsChannelMapper {
    int deleteByPrimaryKey(Integer channelID);

    int insert(CmsChannel record);

    int insertSelective(CmsChannel record);
    
    /**
     * 查询栏目一览
     * @param siteCode 站点Code
     * @return 得到栏目一览列表
     */
    List<CmsChannel> selectChannelList(String siteCode);
    
    /**
     * 通过站点ID查询栏目一览
     * @param siteID 站点ID
     * @return 得到的栏目一览列表
     */
    List<CmsChannel> selectBySiteID(Integer siteID);

    int updateByPrimaryKeySelective(CmsChannel record);

    int updateByPrimaryKey(CmsChannel record);
}