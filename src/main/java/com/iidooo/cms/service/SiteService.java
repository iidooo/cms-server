package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsSite;

public interface SiteService {
    List<CmsSite> getSiteList(Integer ownerID);
    
    CmsSite getSite(Integer siteID);
}
