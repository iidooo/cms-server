package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsChannel;

public interface ChannelService {

    /**
     * 创建栏目对象
     * @param channel 栏目对象
     * @return 所创建的CmsChannel对象
     */
    CmsChannel createChannel(CmsChannel channel);
    
    /**
     * 更新栏目对象
     * @param channel 栏目对象
     * @return 更新的CmsChannel对象
     */
    CmsChannel updateChannel(CmsChannel channel);
    
    /**
     * 根据ChannelID获取CmsChannel对象
     * @param channelID 栏目ID
     * @return 所获的的CmsChannel对象
     */
    CmsChannel getChannelByPath(CmsChannel channel);
    
    /**
     * 根据ChannelID获取CmsChannel对象
     * @param channelID 栏目ID
     * @return 所获的的CmsChannel对象
     */
    CmsChannel getChannel(Integer channelID);
    
    List<CmsChannel> getChannelList(String siteCode);
    
    List<CmsChannel> getChannelList(Integer siteID);
    
    /**
     * 删除栏目对象
     * @param channel 要删除的栏目对象信息
     * @return 删除成功与否
     */
    boolean deleteChannel(CmsChannel channel);
}
