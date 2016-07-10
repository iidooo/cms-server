package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsChannel;

public interface ChannelService {
    List<CmsChannel> getChannelList(String siteCode);
    
    List<CmsChannel> getChannelList(Integer siteID);
}
