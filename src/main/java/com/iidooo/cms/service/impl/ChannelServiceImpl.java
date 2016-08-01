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
    public CmsChannel createChannel(CmsChannel channel) {
        CmsChannel result = null;
        try {
            if(cmsChannelMapper.insert(channel) <= 0){
                return result;
            }
            result = cmsChannelMapper.selectByChannelID(channel.getChannelID());
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public CmsChannel updateChannel(CmsChannel channel) {
        CmsChannel result = null;
        try {
            if(cmsChannelMapper.updateByChannelID(channel) <= 0){
                return result;
            }
            result = cmsChannelMapper.selectByChannelID(channel.getChannelID());
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public CmsChannel getChannel(Integer channelID) {
        CmsChannel result = null;
        try {
            result = cmsChannelMapper.selectByChannelID(channelID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public CmsChannel getChannelByPath(CmsChannel channel) {
        CmsChannel result = null;
        try {
            result = cmsChannelMapper.selectByChannelPath(channel);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

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

    @Override
    public boolean deleteChannel(CmsChannel channel) { 
        try {
            if(cmsChannelMapper.deleteByChannelID(channel) <= 0){
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.fatal(e);
            return false;
        }
    }

}
