package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iidooo.cms.mapper.CmsSiteMapper;
import com.iidooo.cms.model.po.CmsSite;
import com.iidooo.cms.service.SiteService;

@Service
public class SiteServiceImpl implements SiteService {
    private static final Logger logger = Logger.getLogger(SiteServiceImpl.class);

    @Autowired
    private CmsSiteMapper siteMapper;
    
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
    
    
}
