package com.iidooo.cms.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iidooo.cms.mapper.CmsSiteUserMapper;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.SiteUserService;

@Service
public class SiteUserServiceImpl implements SiteUserService {

    private static final Logger logger = Logger.getLogger(SiteUserServiceImpl.class);
    
    @Autowired
    private CmsSiteUserMapper siteUserMapper;
    
    @Override
    public int getSiteUserCount(SearchCondition condition) {        
        try {
            int result = siteUserMapper.selectCountForSearch(condition);
            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

}
