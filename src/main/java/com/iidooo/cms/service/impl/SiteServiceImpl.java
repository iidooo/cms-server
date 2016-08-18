package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iidooo.cms.constant.CmsConstant;
import com.iidooo.cms.mapper.CmsChannelMapper;
import com.iidooo.cms.mapper.CmsSiteMapper;
import com.iidooo.cms.mapper.CmsSiteUserMapper;
import com.iidooo.cms.model.po.CmsChannel;
import com.iidooo.cms.model.po.CmsSite;
import com.iidooo.cms.model.po.CmsSiteUser;
import com.iidooo.cms.service.SiteService;
import com.iidooo.core.mapper.SecurityClientMapper;
import com.iidooo.core.model.po.SecurityClient;
import com.iidooo.core.util.StringUtil;

@Service
public class SiteServiceImpl implements SiteService {
    private static final Logger logger = Logger.getLogger(SiteServiceImpl.class);

    @Autowired
    private CmsSiteMapper siteMapper;
    
    @Autowired
    private CmsSiteUserMapper siteUserMapper;
    
    @Autowired
    private CmsChannelMapper channelMapper;
    
    @Autowired
    private SecurityClientMapper securityClientMapper;
    
    @Override
    public List<CmsSite> getSiteList(Integer userID) {
        List<CmsSite> result = new ArrayList<CmsSite>();
        try {
            result = siteMapper.selectByOwner(userID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public CmsSite getSite(Integer siteID) {
        CmsSite result = null;
        try {
            result = siteMapper.selectBySiteID(siteID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }    
    
    @Override
    public CmsSite getSiteByCode(String siteCode) {
        CmsSite result = null;
        try {
            result = siteMapper.selectBySiteCode(siteCode);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    @Transactional
    public CmsSite createSite(CmsSite site) {
        CmsSite result = null;
        try {
            if (siteMapper.insert(site) <= 0) {
                return null;
            }
            
            // 创建一个站点管理员
            CmsSiteUser siteUser = new CmsSiteUser();
            siteUser.setSiteID(site.getSiteID());
            siteUser.setUserID(site.getCreateUserID());
            siteUser.setRole("1");
            siteUser.setCreateUserID(site.getCreateUserID());
            siteUser.setCreateTime(new Date());
            siteUser.setUpdateUserID(site.getCreateUserID());            
            siteUserMapper.insert(siteUser);
            
            // 创建一个首页栏目
            CmsChannel channel = new CmsChannel();
            channel.setSiteID(site.getSiteID());
            channel.setChannelName(CmsConstant.CHANNEL_INDEX_NAME);
            channel.setChannelPath(CmsConstant.CHANNEL_INDEX_PATH);
            channel.setCreateUserID(site.getCreateUserID());
            channel.setCreateTime(new Date());
            channel.setUpdateUserID(site.getCreateUserID());    
            channelMapper.insert(channel);
            
            // 生成站点的AccessKeySecret
            SecurityClient securityClient = new SecurityClient();
            securityClient.setAppID(site.getSiteCode());
            securityClient.setSecret(StringUtil.getGUID());
            securityClient.setCreateUserID(site.getCreateUserID());
            securityClient.setCreateTime(new Date());
            securityClient.setUpdateUserID(site.getCreateUserID());    
            securityClientMapper.insert(securityClient);
            
            result = siteMapper.selectBySiteID(site.getSiteID());
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }        
        return result;
    }

    @Override
    public CmsSite updateSite(CmsSite site) {
        CmsSite result = null;
        try {
            if (siteMapper.updateBySiteID(site) <= 0) {
                return null;
            }
            result = siteMapper.selectBySiteID(site.getSiteID());
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }   
    
    
}
