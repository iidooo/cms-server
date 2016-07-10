package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iidooo.cms.mapper.CmsChannelMapper;
import com.iidooo.cms.model.po.CmsChannel;
import com.iidooo.cms.service.ChannelService;

@Service
public class ChannelServiceImpl implements ChannelService {

    private static final Logger logger = Logger.getLogger(ChannelServiceImpl.class);

    @Autowired
    private CmsChannelMapper cmsChannelMapper;

    @Override
    public List<CmsChannel> getChannelList(String siteCode) {
        List<CmsChannel> result = new ArrayList<CmsChannel>();
        try {
            result = cmsChannelMapper.selectChannelList(siteCode);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public List<CmsChannel> getChannelList(Integer siteID) {
        List<CmsChannel> result = new ArrayList<CmsChannel>();
        try {
            result = cmsChannelMapper.selectBySiteID(siteID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

}
