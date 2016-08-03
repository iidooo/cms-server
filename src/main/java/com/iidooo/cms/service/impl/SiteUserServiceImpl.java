package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iidooo.cms.mapper.CmsSiteUserMapper;
import com.iidooo.cms.model.po.CmsSiteUser;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.SiteUserService;
import com.iidooo.core.model.Page;

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

    @Override
    public List<CmsSiteUser> getSiteUserList(SearchCondition condition, Page page) {
        List<CmsSiteUser> result = new ArrayList<CmsSiteUser>();
        try {
            result = siteUserMapper.selectForSearch(condition, page);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    
}
