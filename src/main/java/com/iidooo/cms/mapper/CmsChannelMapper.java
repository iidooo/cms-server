package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsChannel;

public interface CmsChannelMapper {

    /**
     * 插入一个栏目对象
     * @param channel 该栏目对象会被插入
     * @return 插入成功所返回的影响行数
     */
    int insert(CmsChannel channel);
    
    /**
     * 通过ChannelPath获取CmsChannel对象
     * @param channel 参数
     * @return 所获得的栏目对象
     */
    CmsChannel selectByChannelPath(CmsChannel channel);
    
    /**
     * 通过ChannelID获取CmsChannel对象
     * @param channelID 栏目ID
     * @return 所获的的CmsChannel对象
     */
    CmsChannel selectByChannelID(Integer channelID);
    
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

    /**
     * 通过栏目ID更新栏目
     * @param channel 要更新的栏目对象
     * @return 更新所影响的行数
     */
    int updateByChannelID(CmsChannel channel);
    
    /**
     * 通过栏目ID删除栏目
     * @param channel 要删除的栏目对象
     * @return 所影响的行数
     */
    int deleteByChannelID(CmsChannel channel);
}